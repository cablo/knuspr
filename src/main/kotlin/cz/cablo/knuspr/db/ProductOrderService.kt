package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

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
    private val productRepository: ProductRepository, private val orderRepository: OrderRepository, private val productOrderRepository: ProductOrderRepository,
    private val internalService: InternalService
) {

    @Transactional
    open fun findAllProducts(): List<Product> {
        internalService.deleteExpiredOrdersInternal(this)
        return productRepository.findAllOrdered()
    }

    @Transactional
    open fun findAllValidProducts(): List<Product> {
        internalService.deleteExpiredOrdersInternal(this)
        return productRepository.findAllValidOrdered()
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
        val p = productRepository.findValidById(productId) ?: throw Exception(ErrMessages.PRODUCT_NOT_EXISTS)
        productRepository.softDelete(productId)
        return p
    }

    @Transactional
    open fun updateProduct(product: Product): Product {
        // check existence
        val dbProduct = productRepository.findValidById(product.id!!) ?: throw Exception(ErrMessages.PRODUCT_NOT_EXISTS)
        // check Product is not used by paid Order
        if (productRepository.existsPaidOrder(dbProduct.id!!)) {
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

    @Transactional
    @Throws(OrderItemException::class)
    open fun createOrder(orderWithItems: OrderWithItems): Order {
        internalService.deleteExpiredOrdersInternal(this)
        return internalService.createOrderInternal(orderWithItems)
    }

    @Transactional
    open fun deleteOrder(orderId: Long) {
        return internalService.deleteOrderInternal(orderId)
    }

    @Transactional
    @Throws(OrderItemException::class)
    open fun updateOrder(orderWithItems: OrderWithItems): Order {
        val orderId = orderWithItems.order.id ?: throw Exception(ErrMessages.ORDER_NOT_EXISTS)
        internalService.deleteExpiredOrdersInternal(this)
        internalService.deleteOrderInternal(orderId)
        return internalService.createOrderInternal(orderWithItems)
    }
}