package demo.dw.helloworld

import demo.dw.helloworld.resources.HelloResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class HelloApplication : Application<HelloConfiguration>() {

    override fun getName(): String {
        return "hello-world"
    }

    override fun initialize(bootstrap: Bootstrap<HelloConfiguration>?) {

    }

    override fun run(configuration: HelloConfiguration,
                     environment: Environment) {

        val resource = HelloResource(
                configuration.template,
                configuration.defaultName)
        environment.jersey().register(resource)
    }
}
