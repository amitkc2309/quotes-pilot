import React from 'react'
import ReactDOM from 'react-dom'
import axios from "axios";
import { useEffect, useState } from "react";
import RegisterPage from "./RegisterPage";
import configData from "../config.json"
import jwtDecode from "jwt-decode";
import { Link, Navigate, Route, useNavigate } from "react-router-dom";

export default function AuthenticateRoute({children}){
     const navigate = useNavigate();
     var jwtToken=localStorage.getItem('jwtToken');
     const decoded = jwtDecode(jwtToken);
     if(!jwtToken || jwtToken === undefined || decoded.exp * 1000 < Date.now()){
        return <Navigate to="/login" />;     
     }
     console.log("Authenticated root with token "+jwtToken);
    return children;

}