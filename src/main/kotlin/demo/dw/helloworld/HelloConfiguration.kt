package demo.dw.helloworld

import io.dropwizard.Configuration

class HelloConfiguration : Configuration() {
    var template : String? = null
    var defaultName : String = "Stranger"
}