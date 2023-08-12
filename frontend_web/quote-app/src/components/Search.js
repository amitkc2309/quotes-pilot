import React from 'react'
import ReactDOM from 'react-dom'
import { useState } from "react";

export default function Search ({searchLabel, action, setKeyword}){
    function hadleOnChange(event){
        setKeyword(event.target.value);
    }
    function performSearch(){
        
        
    }
    return(
        <div>
            <input className="text-black" placeholder="Enter keyword" onChange={hadleOnChange}></input>
            <button className="btn-info" onClick={action}>{searchLabel}</button>
        </div>
    );
}