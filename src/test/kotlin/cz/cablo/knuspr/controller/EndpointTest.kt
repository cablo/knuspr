package cz.cablo.knuspr.controller

import cz.cablo.knuspr.bean.OrderItem
import cz.cablo.knuspr.bean.OrderWithItems
import cz.cablo.knuspr.db.*
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

@MicronautTest(transactional = false)
class EndpointTest {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

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
    fun testEverything() {
        // TODO cablo
        println()
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


    }
}