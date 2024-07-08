package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItemError
import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.time.Instant

object ErrMessages {
    const val ORDER_PAID = "The order can not be deleted because it has been paid"
    const val ORDER_NOT_EXISTS = "The order does not exist"
    const val ORDER_NO_ITEMS = "No items in order"

    const val PRODUCT_HAS_PAID_ORDER = "Product can not be updated, it is used by a paid Order"
    const val PRODUCT_NOT_EXISTS = "The product does not exist or has already been deleted"
    const val PRODUCT_NAME_EXISTS = "Valid product with name '{}' already exists"
}

@Singleton
open class ProductOrderService(
    private val productRepository: ProductRepository, private val orderRepository: OrderRepository, private val productOrderRepository: ProductOrderRepository
) {

    open fun findAllProducts(): List<Product> {
        return productRepository.findAllOrdered()
    }

    open fun findAllValidProducts(): List<Product> {
        return productRepository.findAllValid()
    }

    @Transactional
    open fun createProduct(product: Product): Product {
        // if exists valid Product with the same name -> error
        if (productRepository.existsValidWithName(product.name)) {
            throw Exception(ErrMessages.PRODUCT_NAME_EXISTS.replace("{}", product.name))
        }
        product.deleted = null
        return productRepository.save(product)
    }

    open fun deleteProduct(productId: Long): Product {
        val p = productRepository.findValidById(productId)
        if (p == null) {
            throw Exception(ErrMessages.PRODUCT_NOT_EXISTS)
        }
        productRepository.softDelete(productId)
        return p
    }

    @Transactional
    open fun updateProduct(product: Product): Product {
        // check existence
        val dbProduct = productRepository.findValidById(product.id!!)
        if (dbProduct == null) {
            throw Exception(ErrMessages.PRODUCT_NOT_EXISTS)
        }
        // check Product is not used by paid Order
        if (productRepository.paidOrderExists(dbProduct.id!!)) {
            throw Exception(ErrMessages.PRODUCT_HAS_PAID_ORDER)
        }
        // check there is no valid Product with the new name
        if (productRepository.existsValidWithNameExceptId(product.name, dbProduct.id!!)) {
            throw Exception(ErrMessages.PRODUCT_NAME_EXISTS.replace("{}", product.name))
        }
        // update properties except id and deleted
        dbProduct.name = product.name
        dbProduct.quantity = product.quantity
        dbProduct.price = product.price
        return productRepository.update(dbProduct)
    }

    // TODO isolation level
    @Transactional
    open fun createOrder(orderWithItems: OrderWithItems): Order {
        // check empty items
        if (orderWithItems.items.isEmpty()) {
            throw Exception(ErrMessages.ORDER_NO_ITEMS)
        }
        // save order row
        val order = orderWithItems.order
        order.paid = false
        order.created = Instant.now()
        val dbOrder = orderRepository.save(order)
        // save all order items
        val itemErrors: MutableList<OrderItemError> = mutableListOf()
        for (oi in orderWithItems.items) {
            // check quantity > 0
            if (oi.quantity <= 0) {
                itemErrors.add(OrderItemError(productId = oi.productId, missingProduct = null, invalidQuantity = true, missingQuantity = null))
                continue
            }
            // check valid product
            val product = productRepository.findValidById(oi.productId)
            if (product == null) {
                itemErrors.add(OrderItemError(productId = oi.productId, missingProduct = true, invalidQuantity = null, missingQuantity = null))
                continue
            }
            // check quantity
            val missingQuantity = oi.quantity - product.quantity
            if (missingQuantity > 0) {
                itemErrors.add(OrderItemError(productId = oi.productId, missingProduct = null, invalidQuantity = null, missingQuantity = missingQuantity))
                continue
            }
            productRepository.updateQuantity(product.id!!, -(oi.quantity))
            productOrderRepository.save(ProductOrder(productId = oi.productId, orderId = dbOrder.id!!, quantity = oi.quantity))
        }
        // if order item error -> exception
        if (itemErrors.isNotEmpty()) {
            throw OrderItemException(itemErrors)
        }
        return dbOrder
    }

    @Transactional
    open fun deleteOrderWithItems(orderId: Long) {
        val o = orderRepository.findById(orderId)
        if (o.isEmpty) {
            throw Exception(ErrMessages.ORDER_NOT_EXISTS)
        }
        // check paid
        if (o.get().paid) {
            throw Exception(ErrMessages.ORDER_PAID)
        }
        // delete order items:
        // return quantity to the valid product with the same name as current product;
        // if valid product not found, return quantity to the current product
        val items = productOrderRepository.findOrderItems(orderId)
        for (oi in items) {
//            val p = productRepository.findById(oi.productId).get()
            // TODO cablo
//            productRepository.findValidByName(p.name!!)
            productRepository.updateQuantity(oi.productId, oi.quantity)
        }
        productOrderRepository.deleteOrderItems(orderId)
        orderRepository.deleteById(orderId)
    }

    @Transactional
    open fun updateOrder(orderWithItems: OrderWithItems): Order {
        val orderId = orderWithItems.order.id ?: throw Exception(ErrMessages.ORDER_NOT_EXISTS)
        deleteOrderWithItems(orderId)
        return createOrder(orderWithItems)
    }
}