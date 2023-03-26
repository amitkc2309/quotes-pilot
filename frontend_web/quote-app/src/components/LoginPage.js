import axios from "axios";
import { useState } from "react";
import RegisterPage from "./RegisterPage";
import configData from "../config.json"
import { Link, useNavigate } from "react-router-dom";

export default function LoginPage(){
    const [user,setUser]=useState("");
    const [password, setPassword]=useState("");
    const [error,setError]=useState("");
    const navigate = useNavigate();

    function handleUserEvent(event){
        setUser(event.target.value);
    }
    function handlePasswordEvent(event){
        setPassword(event.target.value);
    }
    let URL=configData.BACKEND_URL+"/login/";
    function handleLogin(event){
        event.preventDefault();
        localStorage.clear();
        axios
        .post(URL, {
            name: user,
            password: password
            })
            .then((res) => {
                //console.log(res.data);
                const jwt=res.data;
                setError("");
                localStorage.setItem('jwtToken', jwt);
                navigate("/home");
             }).catch((err) => {
                console.error(err.response.data.message);
                setError("Wrong credentials");
              });
    }

    return(
        <div>
           <form onSubmit={handleLogin}>
                <div className="form-group">
                <input type="user" placeholder="Enter Username" value={user} 
                onChange={handleUserEvent} className="form-control"/>
                </div>
                <div className="form-group">
                <input type="password" placeholder="Enter Password" value={password} 
                onChange={handlePasswordEvent} className="form-control" />
                </div>
                <button type="submit" className="btn btn-primary">Log-In</button>
            </form>
            {error!=null && <p className="text-warning">{error}</p>}
            <Link to="/register">
                <button className="btn btn-info">If you are new, Register Here!!</button>
            </Link>           
        </div>
        
    );
}