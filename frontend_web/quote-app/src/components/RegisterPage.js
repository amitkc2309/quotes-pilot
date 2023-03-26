import axios from "axios";
import { useState } from "react";
import LoginPage from "./LoginPage";

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
    let URL="http://localhost:8080/register/";
    function handleRegister(event){
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

    return(
        <div>
            <form onSubmit={handleRegister}>
                <div className="form-group">
                <input type="user" placeholder="Enter username" value={user} 
                onChange={handleUserEvent} className="form-control"/>
                </div>
                <div className="form-group">
                <input type="password" placeholder="Enter Password" value={password} 
                onChange={handlePasswordEvent} className="form-control" />
                </div>
                <button type="submit" className="btn btn-primary">Register</button>
            </form>
            {response!=null && <p className="text-info">{response}</p>}
            <button className="btn" onClick={LoginPage}>Go to Login!!</button>
        </div>
        
    );
}