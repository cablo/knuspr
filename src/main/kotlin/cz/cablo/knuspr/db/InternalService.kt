package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItemError
import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.transaction.TransactionDefinition
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
open class InternalService(
    private val productRepository: ProductRepository, private val orderRepository: OrderRepository, private val productOrderRepository: ProductOrderRepository
) {

    @Transactional(isolation = TransactionDefinition.Isolation.SERIALIZABLE)
    @Throws(OrderItemException::class)
    open fun createOrderInternal(orderWithItems: OrderWithItems): Order {
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
            productOrderRepository.save(ProductOrder(id = ProductOrderId(productId = oi.productId, orderId = dbOrder.id!!), quantity = oi.quantity))
        }
        // if order item error -> exception
        if (itemErrors.isNotEmpty()) {
            throw OrderItemException(itemErrors)
        }
        return dbOrder
    }

    @Transactional(isolation = TransactionDefinition.Isolation.SERIALIZABLE)
    open fun deleteOrderInternal(orderId: Long) {
        // check order existence
        val o = orderRepository.findById(orderId).orElseThrow { Exception(ErrMessages.ORDER_NOT_EXISTS) }
        // check paid
        if (o.paid) {
            throw Exception(ErrMessages.ORDER_PAID)
        }
        // return items quantities to products:
        // each product item must return its quantity to the product, but referenced product can be deleted, so:
        // a) Try to find valid product by name and if founded -> return quantity to it; else
        // b) Return quantity to the referenced product
        val items = productOrderRepository.findOrderItems(orderId)
        for (oi in items) {
            // try to find valid product; else use current product
            var targetProductId = productRepository.findValidProductIdForProduct(oi.id.productId)
            if (targetProductId == null) {
                targetProductId = oi.id.productId
            }
            productRepository.updateQuantity(targetProductId, oi.quantity)
        }
        // hard delete items and order
        productOrderRepository.deleteOrderItems(orderId)
        orderRepository.deleteById(orderId)
    }

    @Transactional(isolation = TransactionDefinition.Isolation.SERIALIZABLE)
    open fun deleteExpiredOrdersInternal(productOrderService: ProductOrderService) {
        val orderIds = orderRepository.findExpiredAndNotPaidOrderIds()
        for (orderId in orderIds) {
            productOrderService.deleteOrder(orderId)
        }
    }
}