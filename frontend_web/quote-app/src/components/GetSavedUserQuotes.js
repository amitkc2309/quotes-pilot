import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import Search from './Search';
import { BACKEND_URL }  from "../config.js"
import Quote from '../services/Quote';

function GetSavedUserQuotes () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [list, setList]=useState([]);
    let URL=BACKEND_URL+"/quote/quotes-all";
    function handleShowQuotes(){  
        let quotes=[];
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        quotes=axios
        .get(URL,config)
        .then(
            res=>{
                setList(res.data.map(
                    q=>        
                    <li className='list-group-item' key={q.id}>
                        <div className='card' style={{width: '15rem'}}>
                        <div className="card-header">{q.tags}</div>
                        <div className="card-body">
                        <blockquote class="blockquote mb-0">
                            <p>{q.text}</p>
                            <footer className="blockquote-footer">{q.author}</footer>
                        </blockquote>
                        <div className='card-footer'>                        
                            <button className='btn btn-danger' onClick={(event)=>handleDeleteQuote(event,q)}>
                                Remove Quote
                            </button>
                        </div> 
                        </div>
                        </div>
                    </li>           
                )
                 );
            }
        ).catch((err) => {
            console.error(err);
          });
         
        setShowQuotes(true);
    }  

    function handleDeleteQuote(event,quote){
        Quote.removeQuote(quote);
        event.target.setAttribute("disabled", "disabled");
        event.target.innerHTML="Removed";
        event.target.parentNode.parentNode.parentNode.remove(event.target.parentNode.parentNode);
    }

    return(
        <div>
            <button className='btn' onClick={handleShowQuotes}>Show My quotes</button>
            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    
}

export default GetSavedUserQuotes;