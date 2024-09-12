package cz.cablo.knuspr.controller

import cz.cablo.knuspr.bean.OrderWithItems
import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/order")
@Tag(name = "Orders")
class OrderController(private val productOrderService: ProductOrderService) {

    @Post("/create")
    fun create(@Body orderWithItems: OrderWithItems): HttpResponse<*> {
        return HttpResponse.created(productOrderService.createOrder(orderWithItems))
    }

    @Delete("/delete/{orderId}")
    fun delete(orderId: Long): HttpResponse<Any> {
        productOrderService.deleteOrder(orderId)
        return HttpResponse.ok()
    }

    @Post("/update")
    fun update(@Body orderWithItems: OrderWithItems): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.updateOrder(orderWithItems))
    }

    @Get("/pay/{orderId}")
    fun pay(orderId: Long): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.payOrder(orderId))
    }
}