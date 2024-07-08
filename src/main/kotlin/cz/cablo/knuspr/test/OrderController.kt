package cz.cablo.knuspr.test

import cz.cablo.knuspr.bean.OrderWithItems
import cz.cablo.knuspr.db.OrderItemException
import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/order")
class OrderController(private val productOrderService: ProductOrderService) {

    @Post("/create")
    fun create(@Body orderWithItems: OrderWithItems): HttpResponse<Any> {
        return try {
            HttpResponse.created(productOrderService.createOrder(orderWithItems))
        } catch (oie: OrderItemException) {
            HttpResponse.badRequest(oie.itemErrors)
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

    @Get("/delete/{orderId}")
    fun delete(orderId: Long): HttpResponse<Any> {
        return try {
            HttpResponse.created(productOrderService.deleteOrderWithItems(orderId))
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }
}