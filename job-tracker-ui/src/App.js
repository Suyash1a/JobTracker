import React from 'react';
import './App.css';
import KanbanBoard from './components/KanbanBoard';

function App() {
  return (
      <div className="App">
        <h1>Job Tracker</h1>
        <KanbanBoard />
      </div>
  );
}

export default App;