package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItem
import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object Strings {
    const val NEW_ORDER = "New Order"
}

@MicronautTest(transactional = false)
open class OrderTest : CommonTest() {

    @Test
    fun createOrderOk() {
        val o = productOrderService.createOrder(
            OrderWithItems(
                order = Order(id = null, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                    OrderItem(products[2].id!!, 1),
                    OrderItem(products[3].id!!, 2),
                    OrderItem(products[4].id!!, 3),
                )
            )
        )
        assertNotNull(o.id)
        assertEquals(Strings.NEW_ORDER, o.name)
        assertEquals(false, o.paid)
        assertNotNull(o.created)
        // check items
        val items = productOrderRepository.findOrderItems(o.id!!)
        assertEquals(3, items.size)
        for ((i, pi) in items.withIndex()) {
            assertEquals(o.id, pi.id.orderId)
            assertEquals(products[2 + i].id, pi.id.productId)
            assertEquals((i + 1).toLong(), pi.quantity)
        }
    }

    @Test
    fun createOrderWithoutItems() {
        val e = assertFailsWith<Exception> {
            productOrderService.createOrder(OrderWithItems(order = Order(id = null, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf()))
        }
        assertEquals(ErrMessages.ORDER_NO_ITEMS, e.message)
    }

    @Test
    fun createOrderWithInvalidItems() {
        val e = assertFailsWith<OrderItemException> {
            productOrderService.createOrder(
                OrderWithItems(
                    order = Order(id = null, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                        OrderItem(products[0].id!!, 1), // deleted
                        OrderItem(products[1].id!!, 2), // deleted
                        OrderItem(products[2].id!!, -1), // invalid quantity
                        OrderItem(products[3].id!!, 1000), // not enough quantity
                        OrderItem(-1, 10), // invalid id
                    )
                )
            )
        }
        // check no new order and items
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
        // check returned error order items
        val errors = e.itemErrors
        assertEquals(5, errors.size)
        assertEquals(true, errors[0].missingProduct)
        assertEquals(null, errors[0].invalidQuantity)
        assertEquals(null, errors[0].missingQuantity)
        assertEquals(true, errors[1].missingProduct)
        assertEquals(null, errors[1].invalidQuantity)
        assertEquals(null, errors[1].missingQuantity)
        assertEquals(null, errors[2].missingProduct)
        assertEquals(true, errors[2].invalidQuantity)
        assertEquals(null, errors[2].missingQuantity)
        assertEquals(null, errors[3].missingProduct)
        assertEquals(null, errors[3].invalidQuantity)
        assertEquals(900, errors[3].missingQuantity)
        assertEquals(true, errors[4].missingProduct)
        assertEquals(null, errors[4].invalidQuantity)
        assertEquals(null, errors[4].missingQuantity)
    }

    @Test
    fun deleteOrderOk() {
        productOrderService.deleteOrder(orders[3].id!!)
        assertEquals(orders.size - 1, orderRepository.findAll().size)
        assertEquals(productOrders.size - 1, productOrderRepository.findAll().size)
    }

    @Test
    fun deleteOrderNotExists() {
        val e = assertFailsWith<Exception> {
            productOrderService.deleteOrder(-1)
        }
        assertEquals(ErrMessages.ORDER_NOT_EXISTS, e.message)
    }

    @Test
    fun deleteOrderPaid() {
        val e = assertFailsWith<Exception> {
            productOrderService.deleteOrder(orders.first().id!!)
        }
        assertEquals(ErrMessages.ORDER_PAID, e.message)
    }

    @Test
    fun updateOrderOk() {
        val o = productOrderService.updateOrder(
            OrderWithItems(
                order = Order(id = orders[3].id, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                    OrderItem(products[2].id!!, 1),
                    OrderItem(products[3].id!!, 2),
                    OrderItem(products[4].id!!, 3),
                )
            )
        )
        assertTrue(o.id !== orders[3].id)
        assertEquals(Strings.NEW_ORDER, o.name)
        assertEquals(false, o.paid)
        assertNotNull(o.created)
        // check items
        val items = productOrderRepository.findOrderItems(o.id!!)
        assertEquals(3, items.size)
        for ((i, pi) in items.withIndex()) {
            assertEquals(o.id, pi.id.orderId)
            assertEquals(products[2 + i].id, pi.id.productId)
            assertEquals((i + 1).toLong(), pi.quantity)
        }
    }

    @Test
    fun updateOrderWithoutItems() {
        val e = assertFailsWith<Exception> {
            productOrderService.updateOrder(OrderWithItems(order = Order(id = orders[3].id, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf()))
        }
        assertEquals(ErrMessages.ORDER_NO_ITEMS, e.message)
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
    }

    @Test
    fun updateOrderWithInvalidItems() {
        val e = assertFailsWith<OrderItemException> {
            productOrderService.updateOrder(
                OrderWithItems(
                    order = Order(id = orders[3].id, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                        OrderItem(products[0].id!!, 1), // deleted
                        OrderItem(products[1].id!!, 2), // deleted
                        OrderItem(products[2].id!!, -1), // invalid quantity
                        OrderItem(products[3].id!!, 1000), // not enough quantity
                        OrderItem(-1, 10), // invalid id
                    )
                )
            )
        }
        // check no new order and items
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
        // check returned error order items
        val errors = e.itemErrors
        assertEquals(5, errors.size)
        assertEquals(true, errors[0].missingProduct)
        assertEquals(null, errors[0].invalidQuantity)
        assertEquals(null, errors[0].missingQuantity)
        assertEquals(true, errors[1].missingProduct)
        assertEquals(null, errors[1].invalidQuantity)
        assertEquals(null, errors[1].missingQuantity)
        assertEquals(null, errors[2].missingProduct)
        assertEquals(true, errors[2].invalidQuantity)
        assertEquals(null, errors[2].missingQuantity)
        assertEquals(null, errors[3].missingProduct)
        assertEquals(null, errors[3].invalidQuantity)
        assertEquals(900 - productOrders[3].quantity, errors[3].missingQuantity)
        assertEquals(true, errors[4].missingProduct)
        assertEquals(null, errors[4].invalidQuantity)
        assertEquals(null, errors[4].missingQuantity)
    }

    @Test
    fun updateOrderNoId() {
        val e = assertFailsWith<Exception> {
            productOrderService.updateOrder(
                OrderWithItems(
                    order = Order(id = null, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                        OrderItem(products[2].id!!, 1),
                        OrderItem(products[3].id!!, 2),
                        OrderItem(products[4].id!!, 3),
                    )
                )
            )
        }
        assertEquals(ErrMessages.ORDER_NOT_EXISTS, e.message)
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
    }

    @Test
    fun updateOrderNotExists() {
        val e = assertFailsWith<Exception> {
            productOrderService.updateOrder(
                OrderWithItems(
                    order = Order(id = -1, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                        OrderItem(products[2].id!!, 1),
                        OrderItem(products[3].id!!, 2),
                        OrderItem(products[4].id!!, 3),
                    )
                )
            )
        }
        assertEquals(ErrMessages.ORDER_NOT_EXISTS, e.message)
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
    }

    @Test
    fun updateOrderPaid() {
        val e = assertFailsWith<Exception> {
            productOrderService.updateOrder(
                OrderWithItems(
                    order = Order(id = orders[0].id, name = Strings.NEW_ORDER, paid = true, created = null), items = listOf(
                        OrderItem(products[2].id!!, 1),
                        OrderItem(products[3].id!!, 2),
                        OrderItem(products[4].id!!, 3),
                    )
                )
            )
        }
        assertEquals(ErrMessages.ORDER_PAID, e.message)
        assertEquals(orders.size, orderRepository.findAll().size)
        assertEquals(productOrders.size, productOrderRepository.findAll().size)
    }

    @Test
    fun deleteExpiredOrdersInternal() {
        expireAllOrdersInternal()
        // prune them
        internalService.deleteExpiredOrdersInternal(productOrderService)
        assertEquals(orders.size - 2, orderRepository.findAll().size)
        assertEquals(productOrders.size - 2, productOrderRepository.findAll().size)
        // check returned quantities
        assertEquals(products[0].quantity, productRepository.findById(products[0].id).get().quantity)
        assertEquals(products[3].quantity + productOrders[3].quantity, productRepository.findById(products[3].id).get().quantity)
        assertEquals(products[4].quantity + productOrders[4].quantity, productRepository.findById(products[4].id).get().quantity)
    }

    @Test
    fun deleteExpiredOrdersInternalAndReturnQuantityToValidProduct() {
        expireAllOrdersInternal()
        productOrderService.deleteProduct(products[3].id!!)
        val newProduct = productOrderService.createProduct(Product(id = null, name = products[3].name, quantity = 100, price = 1, deleted = null))
        internalService.deleteExpiredOrdersInternal(productOrderService)
        // check row counts
        assertEquals(products.size + 1, productRepository.findAll().size)
        assertEquals(orders.size - 2, orderRepository.findAll().size)
        assertEquals(productOrders.size - 2, productOrderRepository.findAll().size)
        // check new product quantity is increased
        assertEquals(products[3].quantity + productOrders[3].quantity, productRepository.findById(newProduct.id).get().quantity)
    }

    private fun expireAllOrdersInternal() {
        // set all orders expired (including paid)
        for (o in orderRepository.findAll()) {
            o.created = Instant.now().minus(1, ChronoUnit.HOURS)
            orderRepository.update(o)
        }
    }
}