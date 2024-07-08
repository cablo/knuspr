package cz.cablo.knuspr.bean

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class OrderItemError(val productId: Long, val missingProduct: Boolean?, val invalidQuantity: Boolean?, val missingQuantity: Long?)
