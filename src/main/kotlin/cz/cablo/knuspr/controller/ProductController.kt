package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.Product
import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("/product")
class ProductController(private val productOrderService: ProductOrderService) {

    @Post("/create")
    fun create(@Body product: Product): HttpResponse<Any> {
        return try {
            HttpResponse.created(productOrderService.createProduct(product))
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

    @Get("/delete/{productId}")
    fun delete(productId: Long): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.deleteProduct(productId))
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

    @Post("/update")
    fun update(@Body product: Product): HttpResponse<Any> {
        return try {
            HttpResponse.ok(productOrderService.updateProduct(product))
        } catch (e: Exception) {
            HttpResponse.badRequest(e.message)
        }
    }

}