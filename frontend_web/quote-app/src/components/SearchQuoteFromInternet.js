import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import Search from './Search';
import configData from "../config.json"
import Quote from '../services/Quote';

function SearchQuoteFromInternet () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [keyword,setKeyword]=useState("");
    const [list, setList]=useState([]);
    let URL=configData.BACKEND_URL+"/quote/search?query=";
    function handleShowQuotes(event){  
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        let quotes=[];
        quotes=axios
        .get(URL+keyword,config)
        .then(
            res=>{
                setList(res.data.map(
                    q=>        
                    <li className='list-group-item' key={q.id}>
                        <div className='card' style={{width: '17rem'}}>
                        <div className="card-header">{q.tags}</div>
                        <div className="card-body">
                        <blockquote className="blockquote mb-0">
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
                    </li>           
                )
                 );
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
            <Search searchLabel ={"Search Quotes"} action={handleShowQuotes}
            setKeyword={setKeyword}/>
            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    
}

export default SearchQuoteFromInternet;