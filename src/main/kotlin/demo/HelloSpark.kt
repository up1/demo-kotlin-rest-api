package demo

import spark.Request
import spark.Response
import spark.Spark.*

fun main(arguments: Array<String>) {
    get("/hello", {
        request:Request, respons: Response ->
        "Hello World with Spark Java!"
    })
}
