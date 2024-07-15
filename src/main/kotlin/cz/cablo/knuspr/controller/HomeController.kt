package cz.cablo.knuspr.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Hidden
import java.net.URI

@Controller
class HomeController {

    @Hidden
    @Get("/")
    fun redirect(): HttpResponse<URI> {
        return HttpResponse.redirect(URI_PUBLIC)
    }

    companion object {
        private val URI_PUBLIC = URI.create("/public")
    }
}