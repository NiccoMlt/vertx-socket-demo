import React from 'react';
import logo from './logo.svg';
import './App.css';
import SockJsOutput from './SockJsOutput';

const App: React.FC = () => (
  <div className="App">
    <header className="App-header">
      <img src={logo} className="App-logo" alt="logo" />
      <SockJsOutput url={'http://localhost:8080/sock'} />
    </header>
  </div>
);

export default App;
