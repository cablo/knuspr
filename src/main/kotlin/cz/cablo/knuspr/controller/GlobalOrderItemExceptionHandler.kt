package cz.cablo.knuspr.controller

import cz.cablo.knuspr.db.OrderItemException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton
class GlobalOrderItemExceptionHandler : ExceptionHandler<OrderItemException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: OrderItemException): HttpResponse<*> {
        return HttpResponse.badRequest(exception.itemErrors)
    }
}