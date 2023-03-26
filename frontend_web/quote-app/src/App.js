import {BrowserRouter, BrowserRouter as Router, Route, Routes,} from 'react-router-dom'
import './App.css';
import RandomQuote from './components/RandomQuote';
import Home from './components/Home';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import SearchQuoteFromInternet from './components/SearchQuoteFromInternet';
import AuthenticateRoute from './components/AuthenticateRoute';

function App() {
  return (
    <div className="container">
     <h2 className='modal-header card-header' style={{color: 'blue'}}>Quote-Pilot</h2>    
     <BrowserRouter>
        <Routes>
          <Route path="/" exact element={<LoginPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/register" element={<RegisterPage/>} />
          <Route path="/home" element={<AuthenticateRoute><Home/></AuthenticateRoute>} />
        </Routes>   
    </BrowserRouter>
    </div>
  );
}

export default App;
