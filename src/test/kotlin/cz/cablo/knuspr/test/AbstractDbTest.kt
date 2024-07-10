package cz.cablo.knuspr.test

import cz.cablo.knuspr.db.*
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@MicronautTest
open class AbstractDbTest {

    @Inject
    lateinit var productOrderService: ProductOrderService

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var productOrderRepository: ProductOrderRepository

    @Inject
    lateinit var jdbcOperations: JdbcOperations

    @BeforeEach
    fun setUp() {
        jdbcOperations.prepareStatement("delete from product_order;delete from product;delete from \"order\";") { statement ->
            statement.executeUpdate()
        }
    }

    @Test
    fun testCreate() {
        val products = mutableListOf<Product>()
        val orders = mutableListOf<Order>()
        val productOrders = mutableListOf<ProductOrder>()
        for (i in 1..100) {
            products.add(
                productRepository.save(
                    Product(
                        id = null, name = "Knuspr $i", quantity = 100, price = 1, deleted = null
                    )
                )
            )
        }
        for (i in 1..10) {
            orders.add(
                orderRepository.save(
                    Order(
                        id = null, name = "Order $i", paid = false, created = null
                    )
                )
            )
        }
        for (i in 1..5) {
            productOrders.add(
                productOrderRepository.save(ProductOrder(id = ProductOrderId(productId = products[i].id!!, orderId = orders[i].id!!), quantity = i.toLong()))
            )
        }

        // fail duplicated
        assertFailsWith<Exception> {
            productOrderService.createProduct(Product(id = null, name = "Knuspr 1", quantity = 100, price = 1, deleted = null))
        }

        // delete Knuspr 1 and create again
        productRepository.softDelete(products[0].id!!)
        assertEquals(100, productRepository.findAll().size)
        productOrderService.createProduct(
            Product(
                id = null, name = "Knuspr 1", quantity = 100, price = 1, deleted = null
            )
        )
        assertEquals(101, productRepository.findAll().size)
    }

}
