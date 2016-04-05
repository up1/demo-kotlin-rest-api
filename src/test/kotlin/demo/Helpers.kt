package demo

import org.apache.http.Header
import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.*
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.junit.After
import org.junit.Before
import org.wasabi.app.AppConfiguration
import org.wasabi.app.AppServer
import org.wasabi.routing.Route
import java.net.BindException
import java.util.*

open public class TestServerContext {
    @Before fun initTest(): Unit {
        TestServer.start()
    }
    @After fun postTest(): Unit {
        TestServer.reset()
    }
}

// TODO: Clean up response access for tests (switch to EasyHttp.jvm)

object TestServer {

    public val definedPort: Int = Random().nextInt(30000) + 5000
    public var appServer: AppServer = AppServer(AppConfiguration(definedPort))

    public fun start() {
        try {
            appServer.start(false)
        } catch (e: BindException) {

        }
    }

    public fun stop() {
        appServer.stop()
    }

    public fun loadDefaultRoutes() {
        appServer.get("/", { response.send("Root")})
        appServer.get("/first", { response.send("First")})
    }

    public val routes: ArrayList<Route>
        get() = appServer.routes

    public fun reset() {
        appServer.routes.clear()
        appServer.channels.clear()
    }
}

private fun makeRequest(headers: HashMap<String, String>, request: HttpRequestBase): HttpClientResponse {

    val cookie = BasicClientCookie("someCookie", "someCookieValue")
    cookie.path = request.uri?.path
    cookie.domain = "localhost"

    val cookieStore = BasicCookieStore();
    cookieStore.addCookie(cookie)

    val httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    for ((key, value) in headers) {
        request.setHeader(key, value)
    }
    request.setHeader("Connection", "Close")


    val response = httpClient.execute(request)!!

    val body = EntityUtils.toString(response.entity)!!
    val responseHeaders = response.allHeaders!!

    return HttpClientResponse(responseHeaders, body,
            response.statusLine?.statusCode!!,
            response.statusLine?.reasonPhrase ?: "")


}

public fun delete(url: String, headers: HashMap<String, String>): HttpClientResponse {
    return makeRequest(headers, HttpDelete(url))
}

public fun get(url: String, headers: HashMap<String,String> = hashMapOf()): HttpClientResponse {

    val requestConfig = RequestConfig.custom()
            .setRedirectsEnabled(false)
            .build();

    val get = HttpGet(url)
    get.config = requestConfig

    return makeRequest(headers, get)
}

public fun post(url: String, headers: HashMap<String,String> = hashMapOf(), postParams: List<NameValuePair>): HttpClientResponse {

    val requestConfig = RequestConfig.custom()
            .setRedirectsEnabled(false)
            .build();

    val post = HttpPost(url)
    post.config = requestConfig
    post.entity = UrlEncodedFormEntity(postParams);
    return makeRequest(headers, post)
}

public fun options(url: String, headers: HashMap<String, String> = hashMapOf()): HttpClientResponse {
    return makeRequest(headers, HttpOptions(url))
}


public fun postForm(url: String, headers: HashMap<String, String>, fields: ArrayList<BasicNameValuePair>, chunked: Boolean = false): HttpClientResponse {
    val httpPost = HttpPost(url)
    val entity = UrlEncodedFormEntity(fields, "UTF-8")
    entity.isChunked = chunked
    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded")
    httpPost.entity = entity
    return makeRequest(headers, httpPost)
}

data public class HttpClientResponse(val headers: Array<Header>, val body: String, val statusCode: Int, val statusDescription: String)
