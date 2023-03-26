import axios from "axios";
import { useState } from "react";
import RegisterPage from "./RegisterPage";

export default function LoginPage({isAuthenticated}){
    const [user,setUser]=useState("");
    const [password, setPassword]=useState("");
    const [response,setResponse]=useState("");

    function handleUserEvent(event){
        setUser(event.target.value);
    }
    function handlePasswordEvent(event){
        setPassword(event.target.value);
    }
    let URL="http://localhost:8080/login/";
    function handleLogin(event){
        event.preventDefault();
        axios.post(URL, {
            name: user,
            password: password
            })
            .then((res) => {
                console.log(res.data);
                setResponse(res.data);
                const jwt=response;
                localStorage.setItem('jwtToken', jwt);
             }).catch((err) => {
                console.error(err.response.data.message);
                setResponse("Wrong credentials");
              });
    }

    return(
        <div>
           <form onSubmit={handleLogin}>
                <div className="form-group">
                <input type="user" placeholder="Enter username" value={user} 
                onChange={handleUserEvent} className="form-control"/>
                </div>
                <div className="form-group">
                <input type="password" placeholder="Enter Password" value={password} 
                onChange={handlePasswordEvent} className="form-control" />
                </div>
                <button type="submit" className="btn btn-primary">Log-in!!</button>
            </form>
            {response!=null && <p className="text-info">{response}</p>}
            <button className="btn" onClick={RegisterPage}>If you are new Register Here!!</button>
        </div>
        
    );
}