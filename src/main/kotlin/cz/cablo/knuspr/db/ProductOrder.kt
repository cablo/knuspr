package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@MappedEntity
data class ProductOrder(
    // TODO
    @field:Id
    var productId: Long,
    var orderId: Long,
    var quantity: Long
)