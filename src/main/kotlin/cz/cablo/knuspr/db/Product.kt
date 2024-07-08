package cz.cablo.knuspr.db

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant

@Serdeable
@MappedEntity
data class Product(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long?,
    var name: String,
    var quantity: Long,
    var price: Long,
    var deleted: Instant?
)