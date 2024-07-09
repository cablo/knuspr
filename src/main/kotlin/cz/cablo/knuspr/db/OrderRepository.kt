package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface OrderRepository : CrudRepository<Order, Long> {

    @Query("SELECT id FROM \"order\" WHERE paid = false AND created < NOW() - INTERVAL '30 minutes'")
    fun findExpiredAndNotPaidOrderIds(): List<Long>
}