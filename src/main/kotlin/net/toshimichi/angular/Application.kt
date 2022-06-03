package net.toshimichi.angular

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

    val logger = LoggerFactory.getLogger(this::class.java)

    // set up database
    Database.connect("jdbc:h2:mem:ktor_db;DB_CLOSE_DELAY=-1", "org.h2.Driver")
    transaction {
        SchemaUtils.create(Customers)
    }

    // set up DI
    startKoin {
        slf4jLogger()

        modules(org.koin.dsl.module {
            singleOf(::CustomerService)
        })
    }

    // Ktor
    install(ContentNegotiation) {
        json()
    }

    routing {
        createCustomerRoutes()

        get("{any...}") {
            logger.debug("Could not find path for ${call.request.path()}")
            return@get call.respondText("Could not find path", status = HttpStatusCode.NotFound)
        }

        post("{any...}") {
            logger.debug("Could not find path for ${call.request.path()}")
            return@post call.respondText("Could not find path", status = HttpStatusCode.NotFound)
        }
    }
}
