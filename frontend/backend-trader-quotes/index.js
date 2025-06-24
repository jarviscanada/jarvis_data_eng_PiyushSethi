const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const axios = require('axios');

const app = express();
const port = 8080;

// CORS setup for frontend access
app.use(cors());
app.use(bodyParser.json());

// sample data
let traders = [
  {
    key: "1",
    id: 1,
    firstName: "Mike",
    lastName: "Spencer",
    dob: "1990-01-01",
    country: "Canada",
    email: "mike@test.com",
    amount: 20000,
    actions: "<button (click)='deleteTrader'>Delete Trader</button>",
  },
  {
    key: "2",
    id: 2,
    firstName: "Hellen",
    lastName: "Miller",
    dob: "1990-01-01",
    country: "Austria",
    email: "hellen@test.com",
    actions: "<button (click)='deleteTrader'>Delete Trader</button>",
    amount: 538429,
  },
];
let currentId = Math.max(...traders.map(t => t.id), 0) +1;

// let quotes = [
//   {
//     ticker: "AAPL",
//     lastPrice: 172.85,
//     bidPrice: 172.8,
//     bidSize: 1500,
//     askPrice: 172.9,
//     askSize: 1200,
//   },
//   {
//     ticker: "GOOGL",
//     lastPrice: 2815.5,
//     bidPrice: 2815.0,
//     bidSize: 1000,
//     askPrice: 2816.0,
//     askSize: 800,
//   },
//   {
//     ticker: "AMZN",
//     lastPrice: 3365.25,
//     bidPrice: 3365.0,
//     bidSize: 2000,
//     askPrice: 3366.0,
//     askSize: 1500,
//   },
// ];

// fetch trader by ID
app.get("/dashboard/traders/:id", (req, res) => {
  const { id } = req.params;
  const trader = traders.find(t => t.id === parseInt(id));
  
  if (trader) {
    res.json(trader);
  } else {
    res.status(404).json({ message: "Trader not found" });
  }
});

// fetch all traders
app.get("/traders", (req, res) => {
  // console.log("GET /dashboard/traders hit");
  // console.log(traders)
  res.json(traders);
});

let cachedQuotes = null;
let lastFetchTime = 0;
const CACHE_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
// fetch all quotes
app.get("/quotes/dailyList", async (req, res) => {
  const now = Date.now()

  if (cachedQuotes &&(now- lastFetchTime < CACHE_DURATION)){
    return res.json(cachedQuotes)
  }
  const symbols = ['AAPL', 'GOOGL', 'AMZN','MSFT','TSLA']; // Customize this list

  try {
    const quotePromises = symbols.map(symbol => {
      const options = {
        method: 'GET',
        url: 'https://alpha-vantage.p.rapidapi.com/query',
        params: {
          function: 'TIME_SERIES_DAILY',
          symbol: symbol,
          outputsize: 'compact',
          datatype: 'json'
        },
        headers: {
          'x-rapidapi-key': '0663c3ccf5mshcc8e09c1439bb25p19cd67jsnaa913948a975',
          'x-rapidapi-host': 'alpha-vantage.p.rapidapi.com'
        }
      };

      return axios.request(options);
    });

    const responses = await Promise.all(quotePromises);

    const formattedQuotes = responses.map((res, idx) => {
      const symbol = symbols[idx];
      const dailyData = res.data["Time Series (Daily)"];
      const latestDate = Object.keys(dailyData)[0];
      const quote = dailyData[latestDate];

      return {
        ticker: symbol,
        lastPrice: parseFloat(quote["4. close"]),
        bidPrice: parseFloat(quote["1. open"]), // Mock as bid
        bidSize: 1000, // Placeholder
        askPrice: parseFloat(quote["2. high"]), // Mock as ask
        askSize: 1000  // Placeholder
      };
    });
    cachedQuotes = formattedQuotes;
    lastFetchTime = now;
    res.json(formattedQuotes);
  } catch (error) {
    console.error("Error fetching quotes:", error.message);
  if (error.response) {
    console.error("Status:", error.response.status);
    console.error("Data:", error.response.data);
  } else if (error.request) {
    console.error("No response received:", error.request);
  } else {
    console.error("Error setting up request:", error.message);
  }
  res.status(500).json({ message: "Failed to fetch real-time quotes" });
}})

// create a new trader
app.post(
  "/trader/firstname/:firstName/lastname/:lastName/dob/:dob/country/:country/email/:email",
  (req, res) => {
    const { firstName, lastName, dob, country, email } = req.params;

    // //const nextId = Math.max(...traders.map(t => t.id), 0) + 1;
    currentId += 1; // 🔥 Guarantees uniqueness
    const newTrader = {
      // key: String(traders.length + 1),
      // id: traders.length + 1,
      key: String(currentId),
      id: currentId,
      firstName,
      lastName,
      dob,
      country,
      email,
      amount: 0,
      actions: "<button (click)='deleteTrader'>Delete Trader</button>",
    };

    traders.push(newTrader);
    res.status(201).json(newTrader);
  }
);

// PUT /dashboard/trader/deposit/:id/amount/:amount
app.put("/dashboard/trader/deposit/:id/amount/:amount", (req, res) => {
  const { id, amount } = req.params;
  const trader = traders.find(t => t.id === parseInt(id));

  if (!trader) {
    return res.status(404).json({ message: "Trader not found" });
  }

  const depositAmt = parseFloat(amount);
  if (isNaN(depositAmt) || depositAmt <= 0) {
    return res.status(400).json({ message: "Invalid deposit amount" });
  }

  trader.amount += depositAmt;
  res.status(200).json(trader);
});

// PUT /dashboard/trader/withdraw/:id/amount/:amount
app.put("/dashboard/trader/withdraw/:id/amount/:amount", (req, res) => {
  const { id, amount } = req.params;
  const trader = traders.find(t => t.id === parseInt(id));

  if (!trader) {
    return res.status(404).json({ message: "Trader not found" });
  }

  const withdrawAmt = parseFloat(amount);
  if (isNaN(withdrawAmt) || withdrawAmt <= 0) {
    return res.status(400).json({ message: "Invalid withdrawal amount" });
  }

  if (trader.amount < withdrawAmt) {
    return res.status(400).json({ message: "Insufficient funds" });
  }

  trader.amount -= withdrawAmt;
  res.status(200).json(trader);
});


// delete a trader by ID
app.delete("/trader/:id", (req, res) => {
  const { id } = req.params;
  const index = traders.findIndex((trader) => trader.id === parseInt(id));
  if (index !== -1) {
    traders.splice(index, 1);
    res.status(200).json({ message: "Trader deleted successfully." });
  } else {
    res.status(404).json({ message: "Trader not found." });
  }
});


// start the server
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
