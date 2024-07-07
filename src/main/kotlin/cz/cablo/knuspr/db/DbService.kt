package cz.cablo.knuspr.db

import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

@Singleton
open class DbService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val productOrderRepository: ProductOrderRepository
) {

    @Transactional
    open fun createProduct(product: Product): Product {
        // if exists valid Product with the same name -> error
        if (productRepository.existsValidWithName(product.name)) {
            throw Exception("Valid Product with name ${product.name} already exists")
        }
        return productRepository.save(product)
    }

    @Transactional
    open fun deleteProduct(productId: Long): Product {
        val p = productRepository.findValidById(productId)
            ?: throw Exception("The product does not exist or has already been deleted")
        productRepository.softDelete(productId)
        return p
    }

    @Transactional
    open fun updateProduct(product: Product): Product {
        // check existence
        val p = productRepository.findById(product.id!!)
        if (p.isEmpty) {
            throw Exception("The product does not exist")
        }
        // check Product is not used by Order
        val dbProduct = p.get()
        if (productRepository.payedOrderExists(dbProduct.id!!)) {
            throw Exception("Product can not be updated, it is used by a payed Order")
        }
        // check there is no valid Product with the new name
        if (productRepository.existsValidWithName(product.name)) {
            throw Exception("Valid Product with name ${product.name} already exists")
        }
        // update some properties only (id and deleted remain the same)
        dbProduct.name = product.name
        dbProduct.quantity = product.quantity
        dbProduct.price = product.price
        // TODO may be use update
        return productRepository.save(dbProduct)
    }
}