package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Embeddable

@Embeddable
data class ProductOrderId(
    val productId: Long, val orderId: Long
)