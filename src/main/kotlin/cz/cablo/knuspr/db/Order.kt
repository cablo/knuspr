package cz.cablo.knuspr.db

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import java.time.Instant
import java.time.LocalDateTime

@Serdeable
@MappedEntity
data class Order(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long?,
    var name: String,
    var payed: Boolean,
    var created: Instant?
)