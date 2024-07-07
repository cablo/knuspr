package cz.cablo.knuspr.bean

import cz.cablo.knuspr.db.Order
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class OrderWithItems(var order: Order, var items: List<OrderItem>)
