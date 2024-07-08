package cz.cablo.knuspr.db

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import java.time.Instant
import kotlin.test.assertEquals

@MicronautTest(transactional = false)
open class ProductOrderServiceAbstractTest {

    @Inject
    lateinit var productOrderService: ProductOrderService

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var productOrderRepository: ProductOrderRepository

    val products = mutableListOf<Product>()
    val orders = mutableListOf<Order>()
    val productOrders = mutableListOf<ProductOrder>()


    @BeforeEach
    fun beforeEachTest() {
        createNewData()
    }

    private fun createNewData() {
        productOrderRepository.deleteDatabase()
        productOrders.clear()
        products.clear()
        orders.clear()
        for (i in 0..9) {
            products.add(productRepository.save(Product(id = null, name = "Knuspr $i", quantity = 100, price = 1, deleted = if (i <= 1) Instant.now() else null)))
        }
        for (i in 0..4) {
            orders.add(
                orderRepository.save(Order(id = null, name = "Order $i", payed = (i <= 2), created = null))
            )
        }
        for (i in 0..4) {
            productOrders.add(productOrderRepository.save(ProductOrder(productId = products[i].id!!, orderId = orders[i].id!!, quantity = i.toLong())))
        }
        assertEquals(10, productRepository.findAll().size)
        assertEquals(5, orderRepository.findAll().size)
        assertEquals(5, productOrderRepository.findAll().size)
    }
}