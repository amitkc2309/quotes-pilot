import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import { BACKEND_URL } from "../config.js"
import Quote from '../services/Quote';

function RandomQuote () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [data, setData]=useState([]);
    let URL=BACKEND_URL+"/quote/random";
    function handleShowQuotes(){
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        console.log("called with token "+token)
        let quotes=[];
        quotes=axios
        .get(URL,config)
        .then(
            res=>{
                let q=res.data;
                
               
                setData(
                    <div className='card' style={{width: '18rem'}}>
                    <div className="card-header">{q.tags}</div>
                    <div className="card-body">
                    <blockquote class="blockquote mb-0">
                        <p>{q.text}</p>
                        <footer className="blockquote-footer">{q.author}</footer>
                    </blockquote>
                    <div className='card-footer'>                        
                            <button className='btn btn-outline-primary' onClick={(event)=>handleSaveQuote(event,q)}>
                                Save Quote
                            </button>
                        </div> 
                    </div>
                    </div>
                )
            }
        ).catch((err) => {
            console.error(err);
          });
         
        setShowQuotes(true);
    }
    function handleSaveQuote(event,quote){
        Quote.saveQuote(quote);
        event.target.setAttribute("disabled", "disabled");
        event.target.innerHTML="Saved";

    }  
    return(
        <div>
            <button className='btn' onClick={handleShowQuotes}>Get Random Quote</button>
            {showQuotes && <div className='card' style={{width: '18rem'}}>{data}</div>}
        </div>
    );    
}

export default RandomQuote;