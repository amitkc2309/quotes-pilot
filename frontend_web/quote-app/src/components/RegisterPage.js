import axios from "axios";
import { useState } from "react";
import LoginPage from "./LoginPage";
import configData from "../config.json"
import { Link } from "react-router-dom";
import { Card } from "@material-ui/core";

export default function RegisterPage(){
    const [user,setUser]=useState("");
    const [password, setPassword]=useState("");
    const [response,setResponse]=useState("");

    function handleUserEvent(event){
        setUser(event.target.value);
    }
    function handlePasswordEvent(event){
        setPassword(event.target.value);
    }
    let URL=configData.BACKEND_URL+"/register/";
    function handleRegister(event){
        if (user && password) {
            event.preventDefault();
            axios.post(URL, {
                name: user,
                password: password
            })
                .then((res) => {
                    console.log(res.data);
                    setResponse(res.data);
                }).catch((err) => {
                    console.error(err.response.data.message);
                    setResponse("User already exist");
                });

        }
        setResponse("Please provide input");   
    }

    return(
        <div className="container col-5 ">
        <div className='card border-0' style={{width: '20rem'}}>
        <form onSubmit={handleRegister}>
                <div className="form-group">
                <input type="user" placeholder="Enter Username" value={user} 
                onChange={handleUserEvent} className="form-control"/>
                </div>
                <div className="form-group">
                <input type="password" placeholder="Enter Password" value={password} 
                onChange={handlePasswordEvent} className="form-control" />
                </div>
                <button type="submit" className="btn btn-primary">Register</button>
            </form>
            {response!=null && <p className="text-info">{response}</p>}
            <Link to="/login">
                <button className="btn btn-info">Already have an Account? Login Here!!</button>
            </Link>
        </div>
            
        </div>
        
    );
}