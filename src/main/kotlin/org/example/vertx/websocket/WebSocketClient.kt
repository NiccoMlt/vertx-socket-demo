package org.example.vertx.websocket

import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions
import io.vertx.kotlin.core.http.webSocketAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf

class WebSocketClient : CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override suspend fun start() {
    val client = vertx.createHttpClient()

    try {
      val websocket = client.webSocketAwait(8080, "localhost", "/some-uri")
      websocket.handler { data ->
        logger.info("Received data ${data.toString("ISO-8859-1")}")
        client.close()
      }
      websocket.writeBinaryMessage(Buffer.buffer("Hello world"))
    } catch (ex: Exception) {
      logger.error(ex.message)
    }
  }
}
