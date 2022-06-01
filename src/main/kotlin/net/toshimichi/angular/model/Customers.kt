package net.toshimichi.angular.model

import org.jetbrains.exposed.sql.Table

object Customers : Table() {

    val id = integer("id").autoIncrement()
    val email = varchar("email", 32)
    val username = varchar("username", 32)

    override val primaryKey = PrimaryKey(id)
}
