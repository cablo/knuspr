package cz.cablo.knuspr.controller

import cz.cablo.knuspr.bean.OrderItem
import cz.cablo.knuspr.bean.OrderWithItems
import cz.cablo.knuspr.db.*
import cz.cablo.knuspr.db.ProductStrings.NEW_KNUSPR
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.*


@MicronautTest(transactional = false)
class EndpointTest {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var productOrderService: ProductOrderService

    @Inject
    lateinit var productOrderRepository: ProductOrderRepository

    @Inject
    lateinit var objectMapper: ObjectMapper

    val products = mutableListOf<Product>()
    val orders = mutableListOf<Order>()
    val productOrders = mutableListOf<ProductOrder>()


    @BeforeEach
    fun beforeEachTest() {
        createData()
    }

    @Test
    fun listAllProducts() {
        val body = client.toBlocking().exchange("/products", String::class.java).body()
        assertTrue(body.startsWith("[{\"id\":"))
    }

    @Test
    fun deleteAllProducts() {
        for (p in products) {
            val dp = objectMapper.readValue(client.toBlocking().exchange<Any, String>(HttpRequest.DELETE("/product/delete/${p.id}"), String::class.java).body(), Product::class.java)
            assertEquals(p.id, dp.id)
            assertNotNull(dp.deleted)
        }
        assertEquals(10, productOrderService.findAllProducts().size)
        assertEquals(0, productOrderService.findAllValidProducts().size)
    }

    @Test
    fun updateAllProducts() {
        for ((i, p) in products.withIndex()) {
            val up = objectMapper.readValue(
                client.toBlocking().exchange(HttpRequest.POST("/product/update", Product(id = p.id, name = "$NEW_KNUSPR $i", quantity = 1, price = 10, deleted = Instant.now())), String::class.java).body(), Product::class.java
            )
            assertEquals(p.id, up.id)
            assertEquals("$NEW_KNUSPR $i", up.name)
            assertEquals(1, up.quantity)
            assertEquals(10, up.price)
            assertNull(up.deleted)
        }
    }

    @Test
    fun listAllOrders() {
        val body = client.toBlocking().exchange("/orders", String::class.java).body()
        assertTrue(body.startsWith("[{\"id\":"))
    }

    @Test
    fun deleteAllOrders() {
        for (o in orders) {
            client.toBlocking().exchange<Any, Any>(HttpRequest.DELETE("/order/delete/${o.id}"))
        }
        assertEquals(0, productOrderService.findAllOrders().size)
    }

    @Test
    fun updateAllOrders() {
        assertEquals(15, productOrderRepository.findAll().size)
        for ((i, o) in orders.withIndex()) {
            val uo = objectMapper.readValue(
                client.toBlocking().exchange(
                    HttpRequest.POST(
                        "/order/update", OrderWithItems(
                            order = Order(id = o.id, name = "New Order $i", paid = true, created = null), items = listOf(
                                OrderItem(products[0].id!!, 1)
                            )
                        )
                    ), String::class.java
                ).body(), Order::class.java
            )
            assertNotEquals(o.id, uo.id)
            assertEquals("New Order $i", uo.name)
            assertEquals(false, uo.paid)
            assertNotNull(uo.created)
        }
        assertEquals(5, productOrderRepository.findAll().size)
    }

    @Test
    fun payAllOrders() {
        for (o in orders) {
            val po = objectMapper.readValue(client.toBlocking().exchange("/order/pay/${o.id}", String::class.java).body(), Order::class.java)
            assertEquals(o.id, po.id)
            assertTrue(po.paid)
        }
    }


    private fun createData() {
        productOrderRepository.deleteDatabase()
        products.clear()
        orders.clear()
        for (i in 0..9) {
            products.add(
                objectMapper.readValue(
                    client.toBlocking().exchange(
                        HttpRequest.POST("/product/create", Product(id = null, name = "Knuspr $i", quantity = 100, price = 1, deleted = Instant.now())), String::class.java
                    ).body(), Product::class.java
                )
            )
        }
        for (i in 0..4) {
            orders.add(
                objectMapper.readValue(
                    client.toBlocking().exchange(
                        HttpRequest.POST(
                            "/order/create", OrderWithItems(
                                order = Order(id = null, name = "Order $i", paid = true, created = null), items = listOf(
                                    OrderItem(products[0].id!!, 1),
                                    OrderItem(products[1].id!!, 2),
                                    OrderItem(products[2].id!!, 3),
                                )
                            )
                        ), String::class.java
                    ).body(), Order::class.java
                )
            )
        }
        productOrders.addAll(productOrderRepository.findAll())
    }
}