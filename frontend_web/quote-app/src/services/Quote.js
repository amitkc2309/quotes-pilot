import axios from "axios";
import { BACKEND_URL } from "../config.js"

class Quote{
     saveQuote(quote){
        let URL=BACKEND_URL+"/quote/add-quote/";
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*',
                'Content-Type':'application/json'
            }
        };
        const data = //{  text: quote.text, author: quote.author, tags:[quote.tags] };
        {
            "id": quote.id,
            "text": quote.text,
            "author": quote.author,
            "tags": quote.tags
        };
        axios.post(URL, 
        data,
        config)
        .then((res) => {
            return res.data;
         }).catch((err) => {
            return "Some error occured while saving";
          });
    }
    
    removeQuote(quote){
        let URL=BACKEND_URL+"/quote/remove-quote/";
        URL=URL+quote.id;
        console.log("deleting for quote id="+URL)
        const token=localStorage.getItem('jwtToken');
        const config = {
            headers: { 
                'Authorization': `Bearer ${token}` ,
                'Access-Control-Allow-Origin': '*',
                'Content-Type':'application/json'
            }
        };
        axios.delete(URL, 
        config)
        .then((res) => {
            return res.data;
         }).catch((err) => {
            return "Some error occured while deleting";
          });
    }
}

export default new Quote()