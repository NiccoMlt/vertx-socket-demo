package org.example.vertx.counter

import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.bridge.BridgeEventType
import io.vertx.ext.web.handler.sockjs.BridgeEvent

class CounterHandler internal constructor(private val eventBus: EventBus, private val repository: CounterRepository) :
  Handler<BridgeEvent> {
  override fun handle(event: BridgeEvent) {
    if (event.type() == BridgeEventType.SOCKET_CREATED) logger.info("A socket was created")
    if (event.type() == BridgeEventType.SEND) clientToServer()
    event.complete(true)
  }

  private fun clientToServer() {
    val value = repository.get()?.plus(1) ?: 1;
    repository.update(value)
    eventBus.publish("out", value)
  }

  companion object {
    private val logger = LoggerFactory.getLogger(
      CounterHandler::class.java
    )
  }
}
