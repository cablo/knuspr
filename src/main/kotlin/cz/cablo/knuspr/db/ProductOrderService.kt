package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
open class ProductOrderService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val productOrderRepository: ProductOrderRepository
) {

    @Transactional
    open fun findAllValidProducts(): List<Product> {
        return productRepository.findAllValid()
    }

    @Transactional
    open fun createProduct(product: Product): Product {
        // if exists valid Product with the same name -> error
        if (productRepository.existsValidWithName(product.name)) {
            throw Exception("Valid Product with name ${product.name} already exists")
        }
        product.deleted = null
        return productRepository.save(product)
    }

    open fun deleteProduct(productId: Long): Product {
        val p = productRepository.findValidById(productId)
        if (p == null) {
            throw Exception("The product does not exist or has already been deleted")
        }
        productRepository.softDelete(productId)
        return p
    }

    @Transactional
    open fun updateProduct(product: Product): Product {
        // check existence
        val dbProduct = productRepository.findValidById(product.id!!)
        if (dbProduct == null) {
            throw Exception("The product does not exist or has already been deleted")
        }
        // check Product is not used by paid Order
        if (productRepository.payedOrderExists(dbProduct.id!!)) {
            throw Exception("Product can not be updated, it is used by a paid Order")
        }
        // check there is no valid Product with the new name
        if (productRepository.existsValidWithNameExceptId(product.name, dbProduct.id!!)) {
            throw Exception("Valid Product with name '${product.name}' already exists")
        }
        // update properties except id and deleted
        dbProduct.name = product.name
        dbProduct.quantity = product.quantity
        dbProduct.price = product.price
        return productRepository.update(dbProduct)
    }

    @Transactional
    open fun createOrder(orderWithItems: OrderWithItems): Order {
        // save order row
        val order = orderWithItems.order
        order.payed = false
        order.created = Instant.now()
        val dbOrder = orderRepository.save(order)
        // save all order items
        // TODO odebrat quantity z produktu
        for (oi in orderWithItems.items) {
            productOrderRepository.save(ProductOrder(productId = oi.productId, orderId = dbOrder.id!!, quantity = oi.quantity))
        }
        return dbOrder
    }

    @Transactional
    open fun deleteOrderWithItems(orderId: Long) {
        val o = orderRepository.findById(orderId)
        if (o.isEmpty) {
            throw Exception("The order does not exist")
        }
        // check paid
        if (o.get().payed) {
            throw Exception("The order can not be deleted because it has been paid")
        }
        productOrderRepository.deleteOrderItems(orderId)
        orderRepository.deleteById(orderId)
    }

    @Transactional
    open fun updateOrder(order: Order): Order {
        // todo
//        orderRepository.deleteById(orderId)
        return order
    }
}