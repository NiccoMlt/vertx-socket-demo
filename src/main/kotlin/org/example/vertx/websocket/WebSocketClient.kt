package org.example.vertx.websocket

import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.http.webSocketAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle

class WebSocketClient : CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override suspend fun start() {
    val client = vertx.createHttpClient()

    try {
      val websocket = client.webSocketAwait(8080, "localhost", "/sock/websocket")
      websocket.handler { data ->
        logger.info("Received data $data")
        client.close()
      }
      websocket.writeBinaryMessage(Buffer.buffer("Hello world"))
    } catch (ex: Exception) {
      logger.error(ex.message)
    }
  }
}
