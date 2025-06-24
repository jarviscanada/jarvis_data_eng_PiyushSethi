import React, { useState,useEffect } from "react";
import './QuotePage.scss'
import axios from "axios";
import NavBar from '../../component/NavBar/NavBar';
import { dailyListQuotesUrl } from "../../util/constants";

function QuotePage(props){
    const[state, setState] = useState({
        quotes: []
    })

    const getQuotes = async() => {
        const response = await axios.get(dailyListQuotesUrl)
        setState(prev => ({
            ...prev,
            quotes: response.data || [],
        }))
    } 
    useEffect (() => {
        getQuotes();
    },[])

    return (
        <div className="quote-page">
            <h2>Quotes</h2>
            <NavBar />
            <div className="quote-content">
                {state.quotes.length === 0 ? (
                    <p>No quotes available.</p>
                ) : (
                    <table className="quote-table">
                        <thead>
                            <tr>
                                <th>Ticker</th>
                                <th>Last Price</th>
                                <th>Bid Price</th>
                                <th>Bid Size</th>
                                <th>Ask Price</th>
                                <th>Ask Size</th>
                            </tr>
                        </thead>
                        <tbody>
                            {state.quotes.map((quote, index) =>(
                                <tr key={index}>
                                    <td>{quote.ticker}</td>
                                    <td>{quote.lastPrice}</td>
                                    <td>{quote.bidPrice}</td>
                                    <td>{quote.bidSize}</td>
                                    <td>{quote.askPrice}</td>
                                    <td>{quote.askSize}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    )
}

export default QuotePage;