package demo.dw.helloworld.resources

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/hello")
class HelloResource(template: String?, defaultName: String) {

    @GET
    fun hello(): String {
        return "Hello World with Drop Wizard"
    }

}
