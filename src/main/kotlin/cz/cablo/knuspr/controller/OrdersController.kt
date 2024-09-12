package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/orders")
@Tag(name = "Orders")
class OrdersController(private val productOrderService: ProductOrderService) {

    @Get("/")
    fun listAll(): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.findAllOrders())
    }

    @Get("/unpaid")
    fun listUnpaid(): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.findAllUnpaidOrders())
    }
}