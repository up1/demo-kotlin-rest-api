./gradlew shadowJar
java -jar build/libs/kotlin.rest.api-1.0-SNAPSHOT-all.jar server
open http://localhost:8080/hello
