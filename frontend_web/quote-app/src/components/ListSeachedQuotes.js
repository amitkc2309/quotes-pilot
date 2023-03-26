import axios from 'axios';
import React, { Component } from 'react';
//import { quotes } from '../data/quotes';
import { useState } from "react";
import Search from './Search';

function ListSeachedQuotes () {
    const [showQuotes, setShowQuotes] = useState(false);
    const [keyword,setKeyword]=useState("");
    const [list, setList]=useState([]);
    let URL="http://localhost:8080/quote/search?query=";
    function handleShowQuotes(){  
        console.log("search started for keyword "+keyword);
        URL=URL+keyword;
        let quotes=[];
        quotes=axios.get(URL).then(
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
            <Search searchLabel ={"List Quotes"} action={handleShowQuotes}
            setKeyword={setKeyword}/>

            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    
}

export default ListSeachedQuotes;