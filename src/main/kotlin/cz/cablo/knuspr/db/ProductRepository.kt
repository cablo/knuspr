package cz.cablo.knuspr.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository


@JdbcRepository(dialect = Dialect.POSTGRES)
interface ProductRepository : CrudRepository<Product, Long> {

    @Query("SELECT * FROM product ORDER BY name")
    fun findAllOrdered(): List<Product>

    @Query("SELECT * FROM product WHERE deleted is null ORDER BY name")
    fun findAllValid(): List<Product>

    @Query("SELECT EXISTS (SELECT 1 FROM product WHERE name = :name AND deleted is null)")
    fun existsValidWithName(name: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM product WHERE id <> :currentProductId and name = :name AND deleted is null)")
    fun existsValidWithNameExceptId(name: String, currentProductId: Long): Boolean

    @Query("SELECT * FROM product WHERE id = :productId AND deleted is null")
    fun findValidById(productId: Long): Product?

    @Query("UPDATE product SET deleted = now() WHERE id = :productId")
    fun softDelete(productId: Long)

    @Query("UPDATE product SET quantity = quantity + :quantityDelta WHERE id = :productId")
    fun updateQuantity(productId: Long, quantityDelta: Long)

    @Query("SELECT EXISTS (SELECT 1 FROM product_order po, \"order\" o WHERE po.product_id=:productId and po.order_id = o.id and o.payed=true)")
    fun payedOrderExists(productId: Long): Boolean
}