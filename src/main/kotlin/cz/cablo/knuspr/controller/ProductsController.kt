package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/products")
@Tag(name = "Products")
class ProductsController(private val productOrderService: ProductOrderService) {

    @Get("/")
    fun list(): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.findAllValidProducts())
    }

    @Get("/all")
    fun listAll(): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.findAllProducts())
    }
}