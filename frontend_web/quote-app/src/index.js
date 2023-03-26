import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import jwt_decode from 'jwt-decode';

const root = ReactDOM.createRoot(document.getElementById('root'));
function isAuthenticated(){
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    return false;
  }
  try {
    const { exp } = jwt_decode(token);
    if (Date.now() < exp * 1000) {
      return true;
    }
  } catch (e) {
    console.log(e);
  }

  return true;
}
root.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
