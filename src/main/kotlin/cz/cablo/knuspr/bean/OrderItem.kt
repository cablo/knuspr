package cz.cablo.knuspr.bean

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class OrderItem(val productId: Long, var quantity: Long)
