package demo

import org.wasabi.app.AppServer
import org.wasabi.interceptors.parseContentNegotiationHeaders

fun main(args:Array<String>) {
    var server = AppServer()
    server.get("/", {
        response.send("Hello Kotlin")
    })
    server.start()
}

data class Person(var name: String, var age: Int) {}