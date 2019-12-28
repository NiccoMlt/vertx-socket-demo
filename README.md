# Vert.x Socket implementations

This project aims to be a demo to learn how to correctly implement a client-server communication between a Vert.x
backend in Kotlin and a JS/TS client in a browser (possibly in React.js).

![Vert.x-Chrome setup](https://miro.medium.com/max/657/1*op7I7cwC-GijQtMvoxnUiw.png)

This is meant to be a testbed for my [thesis project](https://github.com/NiccoMlt/Protelis-Web).

## Process

### 1. Vert.x Core WebSocket

Following [official example](https://github.com/vert-x3/vertx-examples/tree/master/core-examples/src/main/kotlin/io/vertx/example/core/http/websockets)
in [Vert.x documentation](https://github.com/vert-x3/vertx-examples/blob/master/core-examples/README.adoc#websockets-echo-example),
I implemented a simple WebSocket echo demo project with Vert.x Core.

### 2. Vert.x Web SockJS

SockJS is a client side JavaScript library and protocol which provides a simple WebSocket-like interface;
it allows to make connections to server irrespective of whether the browser or network will allow real WebSockets.
It does this by supporting various different transports between browser and server,
and choosing one at run-time transparently and according to browser and network capabilities.

Vert.x implements a SockJs server compatible with official [SockJS client](https://github.com/sockjs/sockjs-client).

Following [official documentation](https://vertx.io/docs/vertx-web/kotlin/#_sockjs), the previously created server was
converted into a SockJS one. A SockJS compliant server exposes a _WebSocket RFC 6455 compliant_ route at
`ws://<host>:<port>/<sockjs route>/websocket`, so the Kotlin client was only slightly adapted.

### 3. Vert.x Web SockJS EventBus bridge

Vert.x natively support remote connection to its EventBus via SockJS protocol.
Following [official documentation](https://vertx.io/docs/vertx-web/kotlin/#_sockjs_event_bus_bridge) and
[some](https://medium.com/@pvub/real-time-reactive-micro-service-performance-monitoring-vert-x-sockjs-f0e904d9ca8d)
[Medium articles](https://itnext.io/web-sockets-with-vert-x-and-sockjs-1f0710264eea),
I implemented a distributed counter.
