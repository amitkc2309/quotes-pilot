import { Box, Tab, Tabs } from "@material-ui/core";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import RandomQuote from "./RandomQuote";
import SearchQuoteFromInternet from "./SearchQuoteFromInternet";

export default function Home(){
    const [value, setValue] = useState(1);
    const navigate = useNavigate();

    function handleTabChange(event, selected){
        setValue(selected);
    }
    function logout(){
        localStorage.clear();
        navigate("/login");
    }
    return(
        <div>
            <div>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={value} onChange={handleTabChange} textColor="primary" indicatorColor="primary">
                <Tab label="Search Quote" value={1}/>
                <Tab label="Random Quote" value={2}/>
                <Tab label="My Quotes"  value={3}/>
                
             </Tabs>
            
             <div className="align-content-end">
                <button className="btn btn-secondary" onClick={logout}>Log-Out</button>
            </div>
            
             
            </Box>
            {value ==1 && <SearchQuoteFromInternet/>}
            {value ==2 && <RandomQuote/>}
            </div>

            

        </div>
        
        
        
    )
}