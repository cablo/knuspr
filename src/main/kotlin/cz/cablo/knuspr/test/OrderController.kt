package cz.cablo.knuspr.test

import cz.cablo.knuspr.bean.OrderWithItems
import cz.cablo.knuspr.db.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/order")
class OrderController(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val productOrderRepository: ProductOrderRepository
) {

    @Post("/create")
    fun create(@Body orderWithItems: OrderWithItems): HttpResponse<Any> {
        val p = productRepository.save(Product(id = orderWithItems.items[0].productId, name = "Knuspr", quantity = 100, price = 2, deleted = null))
        val o = orderRepository.save(orderWithItems.order)
        val po = ProductOrder(p.id!!, o.id!!, 13)
        productOrderRepository.save(po)
        return HttpResponse.created(o)

//        return try {
//            HttpResponse.created(orderRepository.save(order))
//        } catch (e: Exception) {
//            HttpResponse.serverError(e.message)
//        }
    }
}