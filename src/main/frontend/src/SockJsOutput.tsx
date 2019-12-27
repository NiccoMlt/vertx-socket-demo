import React, {useEffect, useState} from 'react';
import SockJS from 'sockjs-client';

type SockJsOutputProps = {
  url: string;
  options?: SockJS.Options;
};

const SockJsOutput: React.FC<SockJsOutputProps> = (props: SockJsOutputProps) => {
  const {url, options} = props;

  const [sock, setSock] = useState<WebSocket | null>(null);
  const [messages, setMessages] = useState<string[]>([]);

  useEffect(() => {
    if (!sock) {
      const socket = new SockJS(url, /* _reserved */null, options);

      socket.onopen = () => {
        console.log('open socket');
        console.log('New state: %d', socket?.readyState);
      };

      socket.onmessage = (e) => {
        console.log('message', e.data);
        setMessages([e.data, ...messages]);
      };

      socket.onclose = () => {
        console.log('close socket');
        console.log('New state: %d', socket?.readyState);
      };

      setSock(socket);
    }
  }, [sock, url, options, messages]);

  useEffect(() => () => sock?.close());

  const stateMsg = () => {
    switch (sock?.readyState) {
      case undefined:
        return 'Socket is undefined';
      case SockJS.OPEN:
        return 'Socket is open';
      case SockJS.CONNECTING:
        return 'Socket is connecting';
      case SockJS.CLOSING:
        return 'Socket is closing';
      case SockJS.CLOSED:
        return 'Socket is closed';
      default:
        return `Socket is in an unexpected state ${sock?.readyState}`;
    }
  };

  return (
    <div>
      <h1>Socket state</h1>
      <p>{stateMsg()}</p>
      <h1>Socket output</h1>
      <p>{messages.length > 0 ? messages[0] : 'No messages'}</p>
    </div>
  );
};

export default SockJsOutput;
