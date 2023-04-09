import axios from "axios";
import { useState } from "react";
import RegisterPage from "./RegisterPage";
import configData from "../config.json"
import { Link, useNavigate } from "react-router-dom";
import jwtDecode from "jwt-decode";

export default function LoginPage() {
    const [user, setUser] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    function handleUserEvent(event) {
        setUser(event.target.value);
    }
    function handlePasswordEvent(event) {
        setPassword(event.target.value);
    }
    let URL = configData.BACKEND_URL + "/login/";
    function handleLogin(event) {
        if (user && password) {
            event.preventDefault();
            localStorage.clear();
            axios
                .post(URL, {
                    name: user,
                    password: password
                })
                .then((res) => {
                    //console.log("user="+user+" password="+password +" res="+res.data);
                    const jwt = res.data;
                    setError("");
                    localStorage.setItem('jwtToken', jwt);
                    const decoded = jwtDecode(jwt);
                    localStorage.setItem('loggedUser', decoded.sub);
                    navigate("/home");
                }).catch((err) => {
                    setError("Wrong credentials");
                });
        }
        setError("Please provide input");
    }

    return (
        <div className='card border-0' style={{ width: '20rem' }}>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <input type="user" placeholder="Enter Username" value={user}
                        onChange={handleUserEvent} className="form-control" />
                </div>
                <div className="form-group">
                    <input type="password" placeholder="Enter Password" value={password}
                        onChange={handlePasswordEvent} className="form-control" />
                </div>
                <button type="submit" className="btn btn-primary">Log-In</button>
            </form>
            {error != null && <p className="text-warning">{error}</p>}
            <Link to="/register">
                <button className="btn btn-info">If you are new, Register Here!!</button>
            </Link>
        </div>

    );
}