package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface ProductOrderRepository : CrudRepository<ProductOrder, Long> {

    @Query("SELECT po.* FROM product_order po, product p WHERE po.product_id = p.id AND po.order_id = :orderId ORDER BY p.name")
    fun findOrderItems(orderId: Long): List<ProductOrder>

    @Query("DELETE FROM product_order WHERE order_id = :orderId")
    fun deleteOrderItems(orderId: Long)
}