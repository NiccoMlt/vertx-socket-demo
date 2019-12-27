package org.example.vertx.sockjs

import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.http.webSocketAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle

class SockJsClient: CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override suspend fun start() {
    val client = vertx.createHttpClient()

    try {
      val websocket = client.webSocketAwait(8080, "localhost", "/sock/echo/websocket")
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
