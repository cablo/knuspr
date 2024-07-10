package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.Product
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test


@MicronautTest
class Knuspr2Test : AbstractDbTest() {

    @Inject
    @Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var objectMapper: ObjectMapper


    @Test
    fun testCreateOrderOk() {
//        val res = client.toBlocking().exchange(
//            HttpRequest.POST(
//                "/order/create", OrderWithItems(
//                    order = Order(id = null, name = "Knuspr", payed = false, created = null),
//                    items = listOf(OrderItem(productId = 1, quantity = 12))
//                )
//            ), String::class.java
//        )
//        assertEquals(HttpStatus.CREATED, res.status)
//        assertNotNull(objectMapper.readValue(res.body(), Order::class.java).id)
    }


    private fun deleteProduct(): Long {
        val res = createKnuspr()
        assertEquals(HttpStatus.CREATED, res.status)
        val product = objectMapper.readValue(res.body(), Product::class.java)
        assertNotNull(product.id)

        val req: HttpRequest<Any> = HttpRequest.GET("/product/delete/" + product.id)
        val res2 = client.toBlocking().exchange(req, String::class.java)
        val deletedProduct = objectMapper.readValue(res2.body(), Product::class.java)
        assertEquals(HttpStatus.OK, res2.status)
        assertEquals(product.id, deletedProduct.id)

        return deletedProduct.id!!
    }

    private fun createKnuspr(): HttpResponse<String> {
        return client.toBlocking().exchange(
            HttpRequest.POST(
                "/product/create", Product(id = null, name = "Knuspr", quantity = 100, price = 2, deleted = null)
            ), String::class.java
        )
    }
}
