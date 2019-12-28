import { Dispatch, Middleware, MiddlewareAPI } from 'redux';
import EventBus, { EventBus as IEventBus } from 'vertx3-eventbus-client';
import { ActionCreatorWithPayload, createAction } from '@reduxjs/toolkit';
import { increment } from './counterSlice';
import { RootState } from './rootReducer';

type EventBusSetup = {
  host: string;
  options?: unknown;
};

const EB_CONNECT = 'EB_CONNECT';
const EB_CONNECTING = 'EB_CONNECTING';
const EB_CONNECTED = 'EB_CONNECTED';
const EB_DISCONNECT = 'EB_DISCONNECT';
const EB_INCREMENT = 'EB_INCREMENT';
const EB_DISCONNECTED = 'EB_DISCONNECTED';

const ebConnect: ActionCreatorWithPayload<EventBusSetup> = createAction<EventBusSetup>(EB_CONNECT);
const ebConnecting = createAction(EB_CONNECTING);
const ebConnected = createAction(EB_CONNECTED);
const ebDisconnect = createAction(EB_DISCONNECT);
const ebDisconnected = createAction(EB_DISCONNECTED);
const ebIncrement = createAction(EB_INCREMENT);

let eventBus: IEventBus | null = null;

/** @deprecated not working */
const eventBusMiddleware: Middleware = (store: MiddlewareAPI<Dispatch, RootState>) => (next) => (action) => {

  const onOpen = (api: MiddlewareAPI): IEventBus['onopen'] => () => {
    console.log('EventBus opened');
    api.dispatch(ebConnected());
  };

  const onClose = (api: MiddlewareAPI): IEventBus['onclose'] => () => {
    console.log('EventBus closed');
    api.dispatch(ebDisconnected());
  };

  type EventBusMessageHandler = Parameters<IEventBus['registerHandler']>[2];

  const onMessage = (api: MiddlewareAPI): EventBusMessageHandler => (error, message: unknown) => {
    if (!error) {
      console.log('receiving server message: %o', message);
      api.dispatch(increment());
    } else {
      console.error('receiving server error: %o', error);
    }
  };

  switch (action.type) {
    case EB_CONNECT:
      if (eventBus) {
        eventBus.close();
      }
      eventBus = new EventBus(action.payload.host, action.payload.options);
      eventBus.enableReconnect(true);

      eventBus.onopen = onOpen(store);
      eventBus.onclose = onClose(store);
      eventBus.registerHandler('out', {}, onMessage(store));
      break;
    case EB_DISCONNECT:
      if (eventBus !== null) {
        eventBus.close();
      }
      eventBus = null;
      console.log('EventBus closed');
      break;
    case EB_INCREMENT:
      console.log('sending a message %o', action);
        eventBus?.send('in', 1, {});
      break;
    default:
      console.log('the next action:', action);
      return next(action);
  }
  return store.getState();
};

export {
  ebConnect,
  ebConnecting,
  ebConnected,
  ebDisconnect,
  ebDisconnected,
  ebIncrement,
};

export default eventBusMiddleware;
