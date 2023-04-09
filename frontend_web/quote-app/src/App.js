import { BrowserRouter, BrowserRouter as Router, Route, Routes, } from 'react-router-dom'
import './App.css';
import RandomQuote from './components/RandomQuote';
import Home from './components/Home';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import SearchQuoteFromInternet from './components/SearchQuoteFromInternet';
import AuthenticateRoute from './components/AuthenticateRoute';

function App() {
  return (
    <div className="app">
      <header className="App-header">
        <h2>Quote-Pilot</h2>
      </header>
      <div className="custom-container">
        <BrowserRouter>
          <Routes>
            <Route path="/" exact element={<LoginPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/home" element={<AuthenticateRoute><Home /></AuthenticateRoute>} />
          </Routes>
        </BrowserRouter>
      </div>
    </div>
  );
}

export default App;
