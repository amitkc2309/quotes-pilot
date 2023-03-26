import axios from "axios";
import { useEffect, useState } from "react";
import RegisterPage from "./RegisterPage";
import configData from "../config.json"
import { Link, Navigate, Route, useNavigate } from "react-router-dom";

export default function AuthenticateRoute({children}){
     const navigate = useNavigate();
     var jwtToken=localStorage.getItem('jwtToken');
     console.log("Authenticated root with token "+jwtToken);
     if(!jwtToken || jwtToken === undefined){
        return <Navigate to="/login" />;     
     }
     console.log("Authenticated root with childern "+children);
     console.log(jwtToken);
    return children;

}