package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/orders")
class OrdersController(private val productOrderService: ProductOrderService) {

    @Get("/unpaid")
    fun list(): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.findAllUnpaidOrders())
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

    @Get("/all")
    fun listAll(): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.findAllOrders())
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }
}