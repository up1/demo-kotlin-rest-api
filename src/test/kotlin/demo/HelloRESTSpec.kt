package demo

import org.junit.Test as spec
import kotlin.test.assertEquals

class HelloRESTSpec {
    @spec fun adding_a_route_to_routing_table_should_store_it() {

        TestServer.reset()
        TestServer.appServer.get("/", { })

        assertEquals(1, TestServer.appServer.routes.size)
    }
}

