package net.toshimichi.angular

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createCustomerRoutes() {

    val customerService by inject<CustomerService>()

    get("/customers") {
        return@get call.respond(customerService.findAll())
    }

    get("/customers/{id}") {
        val id = runCatching {
            checkNotNull(call.parameters["id"]).toInt()
        }.getOrElse {
            return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)
        }

        val customer = runCatching {
            checkNotNull(customerService.findById(id))
        }.getOrElse {
            return@get call.respondText("Could not find a customer", status = HttpStatusCode.BadRequest)
        }

        return@get call.respond(customer)
    }

    post<Customer>("/customers") {
        customerService.addCustomer(it)
        return@post call.respondText("", status = HttpStatusCode.OK)
    }
}
