import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import Search from './Search';
import configData from "../config.json"

function SearchQuoteFromInternet () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [keyword,setKeyword]=useState("");
    const [list, setList]=useState([]);
    let URL=configData.BACKEND_URL+"/quote/search?query=";
    function handleShowQuotes(){  
        console.log("search started for keyword "+keyword);
        URL=URL+keyword;
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        let quotes=[];
        quotes=axios
        .get(URL,config)
        .then(
            res=>{
                setList(res.data.map(
                    q=>        
                    <li className='list-group-item' key={q.id}>
                        <div className='card' style={{width: '18rem'}}>
                        <div className="card-header">{q.tags}</div>
                        <div className="card-body">
                        <blockquote class="blockquote mb-0">
                            <p>{q.text}</p>
                            <footer className="blockquote-footer">{q.author}</footer>
                        </blockquote>
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
    return(
        <div>
            <Search searchLabel ={"Search Quotes"} action={handleShowQuotes}
            setKeyword={setKeyword}/>

            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    
}

export default SearchQuoteFromInternet;