package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItem
import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@MicronautTest(transactional = false)
open class OrderTest : ProductOrderServiceAbstractTest() {

    @Test
    fun createOrderOk() {
        val o = productOrderService.createOrder(
            OrderWithItems(
                order = Order(id = null, name = "Order 1", payed = true, created = null),
                items = listOf(
                    OrderItem(products[2].id!!, 1),
                    OrderItem(products[3].id!!, 2),
                    OrderItem(products[4].id!!, 3),
                )
            )
        )
        assertNotNull(o.id)
        assertEquals("Order 1", o.name)
        assertEquals(false, o.payed)
        assertNotNull(o.created)
        // check items
        val items = productOrderRepository.findOrderItems(o.id!!)
        assertEquals(3, items.size)
        for ((i, pi) in items.withIndex()) {
            assertEquals(o.id, pi.orderId)
            assertEquals(products[2 + i].id, pi.productId)
            assertEquals((i + 1).toLong(), pi.quantity)
        }
    }

    @Test
    fun createOrderWithoutItems() {
        val e = assertFailsWith<Exception> {
            productOrderService.createOrder(
                OrderWithItems(
                    order = Order(id = null, name = "Order 1", payed = true, created = null),
                    items = listOf()
                )
            )
        }
        assertTrue(e.message!!.contains("No items"))
    }

    @Test
    fun createOrderWithInvalidItems() {
        val e = assertFailsWith<OrderItemException> {
            productOrderService.createOrder(
                OrderWithItems(
                    order = Order(id = null, name = "Order 1", payed = true, created = null),
                    items = listOf(
                        OrderItem(products[0].id!!, 1), // deleted
                        OrderItem(products[1].id!!, 2), // deleted
                        OrderItem(products[2].id!!, -1), // invalid quantity
                        OrderItem(products[3].id!!, 1000), // not enough quantity
                        OrderItem(-1, 10), // invalid id
                    )
                )
            )
        }
        // no new order and items
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
        productOrderService.deleteOrderWithItems(orders[3].id!!)
    }

    @Test
    fun deleteOrderNotExists() {
        val e = assertFailsWith<Exception> {
            productOrderService.deleteOrderWithItems(-1)
        }
        assertTrue(e.message!!.contains("order does not exist"))
    }

    @Test
    fun deleteOrderPaid() {
        val e = assertFailsWith<Exception> {
            productOrderService.deleteOrderWithItems(orders[1].id!!)
        }
        assertTrue(e.message!!.contains("it has been paid"))
    }
}