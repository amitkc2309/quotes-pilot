import { Box, Tab, Tabs } from "@material-ui/core";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import GetSavedUserQuotes from "./GetSavedUserQuotes";
import RandomQuote from "./RandomQuote";
import SearchQuoteFromInternet from "./SearchQuoteFromInternet";
import SearchSavedUserQuotes from "./SearchSavedUserQuotes";

export default function Home(){
    const [value, setValue] = useState(1);
    const navigate = useNavigate();
    const [showLogout,setShowLogout] = useState(false);

    function handleTabChange(event, selected){
        setValue(selected);
    }
    function logout(){
        localStorage.clear();
        navigate("/login");
    }
    return(
        <div className="container">
            <div className="row">
            <div className="col-11">
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={value} onChange={handleTabChange} textColor="primary" indicatorColor="primary">
                <Tab label="Search Quote" value={1}/>
                <Tab label="Random Quote" value={2}/>
                <Tab label="Search in saved Quotes"  value={3}/>
                <Tab label="Saved Quotes"  value={4}/>     
             </Tabs>
             
            </Box>
            {value ==1 && <SearchQuoteFromInternet/>}
            {value ==2 && <RandomQuote/>}
            {value ==3 && <SearchSavedUserQuotes/>}
            {value ==4 && <GetSavedUserQuotes/>}
            </div>

            <div className="col-1">
                    <div className="dropdown">
                        <button className="btn btn-primary dropdown-toggle"
                        onClick={()=>setShowLogout(!showLogout)}>
                           Welcome {localStorage.getItem('loggedUser')}
                        </button>
                        <div className={showLogout ? "dropdown-menu show btn btn-primary" : "dropdown-menu"}>
                            <button className="dropdown-item" type="button" onClick={logout}>Logout</button>
                        </div>
                    </div>
            </div>
            </div>
        </div>
        
        
        
    )
}