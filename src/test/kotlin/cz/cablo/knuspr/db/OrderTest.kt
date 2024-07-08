package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItem
import cz.cablo.knuspr.bean.OrderWithItems
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@MicronautTest
open class OrderTest : ProductOrderServiceAbstractTest() {

    @Test
    fun createOrderOk() {
        val itemsCount = productOrders.size
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
        assertEquals(itemsCount + 3, productOrderRepository.findAll().size)
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