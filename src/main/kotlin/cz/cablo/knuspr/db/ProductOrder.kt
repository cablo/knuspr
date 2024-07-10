package cz.cablo.knuspr.db

import io.micronaut.data.annotation.EmbeddedId
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@MappedEntity
data class ProductOrder(
    @EmbeddedId
    val id: ProductOrderId,

    var quantity: Long
)