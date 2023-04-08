import axios from "axios";
import { useState } from "react";
import Quote from "../services/Quote";
import configData from "../config.json"
import Search from "./Search";

function SearchSavedUserQuotes(){
    const [showQuotes, setShowQuotes] = useState(false);
    const [showTags, setShowTags] = useState(false);
    const [list, setList]=useState([]);
    const [tags,setTags]=useState([]);
    let TAG_URL=configData.BACKEND_URL+"/quote/tags";
    let URL=configData.BACKEND_URL+"/quote/search-by-tag?tag=";
    function handleShowTags(event){  
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        let tags=[];
        tags=axios
        .get(TAG_URL,config)
        .then(
            res=>{
                setTags(res.data.map(
                    t=>        
                    <li className='list-group-item' key={t.id}>
                        <div className='card' style={{width: '12rem'}}>
                        <button className='btn btn-outline-primary' onClick={(event)=>handleShowQuotesByTag(event,t.tag)}>
                                {t.tag}
                            </button>
                        </div>
                    </li>           
                )
                 );
            }
        ).catch((err) => {
            console.error(err);
          });
          setShowTags(true);
    }  

    function handleShowQuotesByTag(event,tag){  
        console.log("searched quote by tag="+tag);
        let quotes=[];
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*'
            }
        };
        quotes=axios
        .get(URL+tag,config)
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
            <button className='btn' onClick={handleShowTags}>Show All saved Tags </button>
            {showTags && <ul className='d-flex flex-wrap list-group-flush'>{tags}</ul>}
            {showQuotes && <ul className='d-flex flex-wrap list-group-flush'>{list}</ul>}
        </div>
    );    

}

export default SearchSavedUserQuotes;