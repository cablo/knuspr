package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface ProductOrderRepository : CrudRepository<ProductOrder, ProductOrderId> {

    @Query("SELECT po.* FROM product_order po, product p WHERE po.id_product_id = p.id AND po.id_order_id = :orderId ORDER BY p.name")
    fun findOrderItems(orderId: Long): List<ProductOrder>

    @Query("DELETE FROM product_order WHERE id_order_id = :orderId")
    fun deleteOrderItems(orderId: Long)

    // TODO for test only because lateinit var jdbcOperations: JdbcOperations is not properly initialized in non-transactional tests
    @Query("""
        DELETE FROM product_order;
        DELETE FROM product;
        DELETE FROM "order";
        """)
    fun deleteDatabase()
}