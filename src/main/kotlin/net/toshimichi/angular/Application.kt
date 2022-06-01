package net.toshimichi.angular

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import net.toshimichi.angular.model.Customers
import net.toshimichi.angular.route.createCustomerRoutes
import net.toshimichi.angular.service.CustomerService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

    Database.connect("jdbc:h2:mem:ktor_db;DB_CLOSE_DELAY=-1", "org.h2.Driver")
    transaction {
        SchemaUtils.create(Customers)
    }

    startKoin {
        modules(org.koin.dsl.module {
            singleOf(::CustomerService)
        })
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        createCustomerRoutes()
    }
}
