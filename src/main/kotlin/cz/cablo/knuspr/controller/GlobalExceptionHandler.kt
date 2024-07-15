package cz.cablo.knuspr.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<Exception, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: Exception): HttpResponse<*> {
        return HttpResponse.badRequest(exception.message)
    }
}