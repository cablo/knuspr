package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.Product
import cz.cablo.knuspr.db.ProductOrderService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/product")
@Tag(name = "Products")
class ProductController(private val productOrderService: ProductOrderService) {

    @Post("/create")
    fun create(@Body product: Product): HttpResponse<*> {
        return HttpResponse.created(productOrderService.createProduct(product))
    }

    @Delete("/delete/{productId}")
    fun delete(productId: Long): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.deleteProduct(productId))
    }

    @Post("/update")
    fun update(@Body product: Product): HttpResponse<*> {
        return HttpResponse.ok(productOrderService.updateProduct(product))
    }
}