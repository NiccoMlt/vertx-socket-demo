import React from 'react';
import logo from '../logo.svg';
import './App.css';
import EventBusCounter from './EventBusCounter';

const App: React.FC = () => (
  <div className="App">
    <header className="App-header">
      <img src={logo} className="App-logo" alt="logo" />
      {/* <SockJsOutput url={'http://localhost:8080/sock'} /> */}
      <EventBusCounter />
    </header>
  </div>
);

export default App;
