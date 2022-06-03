package net.toshimichi.angular

import net.toshimichi.angular.Customers.email
import net.toshimichi.angular.Customers.username
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class CustomerService {

    fun addCustomer(customer: Customer) {
        transaction {
            Customers.insert {
                it[email] = customer.email
                it[username] = customer.username
            }
        }
    }

    fun findById(id: Int): Customer? {
        return transaction {
            Customers
                .select { Customers.id eq id }
                .mapToCustomer()
                .getOrNull(0)
        }
    }

    fun findAll(): Collection<Customer> {
        return transaction {
            Customers.selectAll()
                .mapToCustomer()
        }
    }

    private fun Query.mapToCustomer(): List<Customer> {
        return map { Customer(it[email], it[username]) }
    }
}
