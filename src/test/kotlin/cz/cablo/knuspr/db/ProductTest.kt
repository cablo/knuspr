package cz.cablo.knuspr.db

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.*

@MicronautTest
open class ProductTest : ProductOrderServiceAbstractTest() {

    @Test
    fun listValidProducts() {
        val ps = productOrderService.findAllValidProducts()
        assertEquals(ps.size, products.size - 2)
        assertEquals("Knuspr 2", ps.first().name)
        assertEquals(products.last().name, ps.last().name)
    }

    @Test
    fun createProductOk() {
        val p = productOrderService.createProduct(Product(id = null, name = "Knuspr 0", quantity = 100, price = 1, deleted = Instant.now()))
        assertNotNull(p.id)
        assertEquals("Knuspr 0", p.name)
        assertEquals(100, p.quantity)
        assertEquals(1, p.price)
        assertNull(p.deleted)
    }

    @Test
    fun createProductDuplicateName() {
        val e = assertFailsWith<Exception> {
            productOrderService.createProduct(Product(id = null, name = products[5].name, quantity = 100, price = 1, deleted = null))
        }
        assertTrue(e.message!!.contains("already exists"))
    }

    @Test
    fun deleteProductOk() {
        var p = productOrderService.deleteProduct(products[5].id!!)
        p = productRepository.findById(p.id).get()
        assertNotNull(p.id)
        assertNotNull(p.deleted)
    }

    @Test
    fun deleteProductNotExists() {
        var e = assertFailsWith<Exception> {
            productOrderService.deleteProduct(-1)
        }
        assertTrue(e.message!!.contains("does not exist"))
    }

    @Test
    fun deleteProductAlreadyDeleted() {
        val e = assertFailsWith<Exception> {
            productOrderService.deleteProduct(products[0].id!!)
        }
        assertTrue(e.message!!.contains("does not exist"))
    }

    @Test
    fun updateProductOk() {
        val p = productOrderService.updateProduct(Product(id = products[5].id, name = "New Knuspr", quantity = 10, price = 2, deleted = Instant.now()))
        assertEquals(products[5].id, p.id)
        assertEquals("New Knuspr", p.name)
        assertEquals(10, p.quantity)
        assertEquals(2, p.price)
        assertNull(p.deleted)
    }

    @Test
    fun updateProductNotExists() {
        // unknown id
        var e = assertFailsWith<Exception> {
            productOrderService.updateProduct(Product(id = -1, name = "New Knuspr", quantity = 10, price = 2, deleted = Instant.now()))
        }
        assertTrue(e.message!!.contains("does not exist"))
        // already deleted
        e = assertFailsWith<Exception> {
            productOrderService.updateProduct(Product(id = products[0].id, name = "New Knuspr", quantity = 10, price = 2, deleted = Instant.now()))
        }
        assertTrue(e.message!!.contains("does not exist"))
    }

    @Test
    fun updateProductPayedOrder() {
        val e = assertFailsWith<Exception> {
            productOrderService.updateProduct(Product(id = products[2].id, name = "New Knuspr", quantity = 10, price = 2, deleted = Instant.now()))
        }
        assertTrue(e.message!!.contains("used by a paid Order"))
    }

    @Test
    fun updateProductExistsNewName() {
        // new name collision
        val e = assertFailsWith<Exception> {
            productOrderService.updateProduct(Product(id = products[3].id, name = "Knuspr 4", quantity = 10, price = 2, deleted = Instant.now()))
        }
        assertTrue(e.message!!.contains("already exists"))
        // no name changed -> ok
        val p = productOrderService.updateProduct(Product(id = products[3].id, name = "Knuspr 3", quantity = 10, price = 2, deleted = Instant.now()))
        assertEquals(products[3].id, p.id)
        assertEquals("Knuspr 3", p.name)
        assertEquals(10, p.quantity)
        assertEquals(2, p.price)
        assertNull(p.deleted)
    }
}