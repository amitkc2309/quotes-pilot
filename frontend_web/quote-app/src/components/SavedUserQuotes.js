import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import Search from './Search';
import configData from "../config.json"

function SavedUserQuotes () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [list, setList]=useState([]);
    let URL=configData.BACKEND_URL+"/quote/quotes-all";
    function handleShowQuotes(){  
        let quotes=[];
        quotes=axios
        .get(URL)
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
            console.error("err");
          });
         
        setShowQuotes(true);
    }  
    return(
        <div>
            <button className='btn' onClick={handleShowQuotes}>Show My quotes</button>
            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    
}

export default SavedUserQuotes;