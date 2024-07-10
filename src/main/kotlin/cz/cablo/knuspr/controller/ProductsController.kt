package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/products")
class ProductsController(private val productOrderService: ProductOrderService) {

    @Get("/")
    fun list(): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.findAllValidProducts())
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

    @Get("/all")
    fun listAll(): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.findAllProducts())
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }
}