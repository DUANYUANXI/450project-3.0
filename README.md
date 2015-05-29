# 450project-3.0
450project-3.0
excepted output:
A) Add some stocks to the Trading System: IBM, CBOE, GOOG, AAPL, GE, T

B) Create 3 users: REX, ANN, RAJ

C) Connect users REX, ANN, RAJ to the trading system

D) Subscribe users REX, ANN, RAJ to Current Market, Last Sale, Ticker, and Messages for all Stocks
REX's Market Display is in 'Visible' mode
ANN's Market Display is in 'Visible' mode
RAJ's Market Display is in 'Visible' mode

E) User REX queries market state
REX: Market State Query: CLOSED

F) Put the market in PREOPEN state
[ANN's GUI], Update Market State: PREOPEN
[REX's GUI], Update Market State: PREOPEN
[RAJ's GUI], Update Market State: PREOPEN

G) User ANN queries market state
ANN: Market State Query: PREOPEN

H) Put the market in OPEN state
[ANN's GUI], Update Market State: OPEN
[REX's GUI], Update Market State: OPEN
[RAJ's GUI], Update Market State: OPEN
NO opening trade in GOOG
NO opening trade in AAPL
NO opening trade in T
NO opening trade in IBM
NO opening trade in CBOE
NO opening trade in GE

I) User RAJ queries market state
RAJ: Market State Query: OPEN

1.1) User REX submits an order for GOOG, BUY 100@40.00
[RAJ's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $40.00@100 -- $0.00@0

1.2) User ANN submits a quote for GOOG, 100@40.00 x 100@40.50
[RAJ's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100
[REX's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100
[ANN's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@100

1.3) User RAJ submits an Order for GOOG, SELL 135@40.50
[RAJ's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235
[REX's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235
[ANN's GUI], Update Market Data: GOOG: $40.00@200 -- $40.50@235

1.4) User REX does a Book Depth query for GOOG
Book Depth: $40.00 x 200 -- $40.50 x 235

1.5) User REX does a query for their orders with remaining quantity for GOOG
REX Orders With Remaining Qty: [Product: GOOG, Price: $40.00, OriginalVolume: 100, RemainingVolume: 100, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXGOOG$40.00449981045799235]

1.6) User ANN does a query for their orders with remaining quantity for GOOG
ANN Orders With Remaining Qty: []

1.7) User RAJ does a query for their orders with remaining quantity for GOOG
RAJ Orders With Remaining Qty: [Product: GOOG, Price: $40.50, OriginalVolume: 135, RemainingVolume: 135, CancelledVolume: 0, User: RAJ, Side: SELL, IsQuote: false, Id: RAJGOOG$40.50449981055079338]

1.8) User REX cancels their order
[REX's GUI], Update Market Activity: {2015-05-24 22:43:37.954} Cancel Message: BUY 100 GOOG at $40.00 BUY Order Cancelled [Tradable Id: REXGOOG$40.00449981045799235]

[RAJ's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235
[REX's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235
[ANN's GUI], Update Market Data: GOOG: $40.00@100 -- $40.50@235

1.9) User ANN cancels their quote
[ANN's GUI], Update Market Activity: {2015-05-24 22:43:37.985} Cancel Message: BUY 100 GOOG at $40.00 Quote BUY-Side Cancelled [Tradable Id: ANNGOOG449981051892029]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:37.985} Cancel Message: SELL 100 GOOG at $40.50 Quote SELL-Side Cancelled [Tradable Id: ANNGOOG449981051911701]

[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $40.50@135

1.10) User RAJ cancels their order
[RAJ's GUI], Update Market Activity: {2015-05-24 22:43:37.985} Cancel Message: SELL 135 GOOG at $40.50 SELL Order Cancelled [Tradable Id: RAJGOOG$40.50449981055079338]

[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0

1.11) Display position values for all users
REX Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00
ANN Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00
RAJ Stock Value: $0.00, Account Costs: $0.00, Net Value: $0.00

1.12) User REX enters an order for GOOG, BUY 100@$10.00
[RAJ's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $10.00@100 -- $0.00@0

1.13) User ANN enters a quote for GOOG, 100@$10.00 x 100@10.10
[RAJ's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100
[REX's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100
[ANN's GUI], Update Market Data: GOOG: $10.00@200 -- $10.10@100

1.14) User RAJ enters an order for GOOG, SELL 150@$10.00 - results in a trade
[RAJ's GUI], Update Market Activity: {2015-05-24 22:43:38.016} Fill Message: SELL 150 GOOG at $10.00 leaving 0 [Tradable Id: RAJGOOG$10.00449981108614844]

[REX's GUI], Update Market Activity: {2015-05-24 22:43:38.016} Fill Message: BUY 100 GOOG at $10.00 leaving 0 [Tradable Id: REXGOOG$10.00449981096780787]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.016} Fill Message: BUY 50 GOOG at $10.00 leaving 50 [Tradable Id: ANNGOOG449981101272443]

[RAJ's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[REX's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[ANN's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@100
[RAJ's GUI], Update Last Sale: GOOG: $10.00@150
[REX's GUI], Update Last Sale: GOOG: $10.00@150
[ANN's GUI], Update Last Sale: GOOG: $10.00@150
[RAJ's GUI], Update Ticker: GOOG: $10.00  
[REX's GUI], Update Ticker: GOOG: $10.00  
[ANN's GUI], Update Ticker: GOOG: $10.00  

1.15) User REX does a Book Depth query for GOOG
IBM Book Depth: $10.00 x 50 -- $10.10 x 100

1.16) User REX enters a market order for GOOG, SELL 75@MKT - results in a trade
[REX's GUI], Update Market Activity: {2015-05-24 22:43:38.032} Fill Message: BUY 75 GOOG at $10.10 leaving 0 [Tradable Id: REXGOOGMKT449981128935704]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.032} Fill Message: SELL 75 GOOG at $10.10 leaving 25 [Tradable Id: ANNGOOG449981101277147]

[RAJ's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[REX's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[ANN's GUI], Update Market Data: GOOG: $10.00@50 -- $10.10@25
[RAJ's GUI], Update Last Sale: GOOG: $10.10@75
[REX's GUI], Update Last Sale: GOOG: $10.10@75
[ANN's GUI], Update Last Sale: GOOG: $10.10@75
[RAJ's GUI], Update Ticker: GOOG: $10.10 ↑
[REX's GUI], Update Ticker: GOOG: $10.10 ↑
[ANN's GUI], Update Ticker: GOOG: $10.10 ↑

1.17) User ANN does a Book Depth query for GOOG
IBM Book Depth: $10.00 x 50 -- $10.10 x 25

1.18) User ANN cancels her quote for GOOG
[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.047} Cancel Message: BUY 50 GOOG at $10.00 Quote BUY-Side Cancelled [Tradable Id: ANNGOOG449981101272443]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.047} Cancel Message: SELL 25 GOOG at $10.10 Quote SELL-Side Cancelled [Tradable Id: ANNGOOG449981101277147]

[RAJ's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: GOOG: $0.00@0 -- $0.00@0

1.19) Show stock holdings for all users
REX Holdings: [GOOG]
ANN Holdings: [GOOG]
RAJ Holdings: [GOOG]

1.20) Show order Id's  for all users
REX Orders: [client.TradableUserData@3d4eac69, client.TradableUserData@42a57993, client.TradableUserData@75b84c92]
ANN Orders: []
RAJ Orders: [client.TradableUserData@6bc7c054, client.TradableUserData@232204a1]

1.21) Show positions for all users
REX Stock Value: $1,767.50, Account Costs: $-1,757.50, Net Value: $10.00
ANN Stock Value: $-252.50, Account Costs: $257.50, Net Value: $5.00
RAJ Stock Value: $-1,515.00, Account Costs: $1,500.00, Net Value: $-15.00

2.1) User REX submits an order for IBM, BUY 100@40.00
[RAJ's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $40.00@100 -- $0.00@0

2.2) User ANN submits a quote for IBM, 100@40.00 x 100@40.50
[RAJ's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100
[REX's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100
[ANN's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@100

2.3) User RAJ submits an Order for IBM, SELL 135@40.50
[RAJ's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235
[REX's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235
[ANN's GUI], Update Market Data: IBM: $40.00@200 -- $40.50@235

2.4) User REX does a Book Depth query for IBM
Book Depth: $40.00 x 200 -- $40.50 x 235

2.5) User REX does a query for their orders with remaining quantity for IBM
REX Orders With Remaining Qty: [Product: IBM, Price: $40.00, OriginalVolume: 100, RemainingVolume: 100, CancelledVolume: 0, User: REX, Side: BUY, IsQuote: false, Id: REXIBM$40.00449981158162898]

2.6) User ANN does a query for their orders with remaining quantity for IBM
ANN Orders With Remaining Qty: []

2.7) User RAJ does a query for their orders with remaining quantity for IBM
RAJ Orders With Remaining Qty: [Product: IBM, Price: $40.50, OriginalVolume: 135, RemainingVolume: 135, CancelledVolume: 0, User: RAJ, Side: SELL, IsQuote: false, Id: RAJIBM$40.50449981167078212]

2.8) User REX cancels their order
[REX's GUI], Update Market Activity: {2015-05-24 22:43:38.063} Cancel Message: BUY 100 IBM at $40.00 BUY Order Cancelled [Tradable Id: REXIBM$40.00449981158162898]

[RAJ's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235
[REX's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235
[ANN's GUI], Update Market Data: IBM: $40.00@100 -- $40.50@235

2.9) User ANN cancels their quote
[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.063} Cancel Message: BUY 100 IBM at $40.00 Quote BUY-Side Cancelled [Tradable Id: ANNIBM449981165105014]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.079} Cancel Message: SELL 100 IBM at $40.50 Quote SELL-Side Cancelled [Tradable Id: ANNIBM449981165108008]

[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $40.50@135

2.10) User RAJ cancels their order
[RAJ's GUI], Update Market Activity: {2015-05-24 22:43:38.079} Cancel Message: SELL 135 IBM at $40.50 SELL Order Cancelled [Tradable Id: RAJIBM$40.50449981167078212]

[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0

2.11) Display position values for all users
REX Stock Value: $1,767.50, Account Costs: $-1,757.50, Net Value: $10.00
ANN Stock Value: $-252.50, Account Costs: $257.50, Net Value: $5.00
RAJ Stock Value: $-1,515.00, Account Costs: $1,500.00, Net Value: $-15.00

2.12) User REX enters an order for IBM, BUY 100@$10.00
[RAJ's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $10.00@100 -- $0.00@0

2.13) User ANN enters a quote for IBM, 100@$10.00 x 100@10.10
[RAJ's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100
[REX's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100
[ANN's GUI], Update Market Data: IBM: $10.00@200 -- $10.10@100

2.14) User RAJ enters an order for IBM, SELL 150@$10.00 - results in a trade
[REX's GUI], Update Market Activity: {2015-05-24 22:43:38.094} Fill Message: BUY 100 IBM at $10.00 leaving 0 [Tradable Id: REXIBM$10.00449981181390954]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.094} Fill Message: BUY 50 IBM at $10.00 leaving 50 [Tradable Id: ANNIBM449981183594658]

[RAJ's GUI], Update Market Activity: {2015-05-24 22:43:38.094} Fill Message: SELL 150 IBM at $10.00 leaving 0 [Tradable Id: RAJIBM$10.00449981191310829]

[RAJ's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[REX's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[ANN's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@100
[RAJ's GUI], Update Last Sale: IBM: $10.00@150
[REX's GUI], Update Last Sale: IBM: $10.00@150
[ANN's GUI], Update Last Sale: IBM: $10.00@150
[RAJ's GUI], Update Ticker: IBM: $10.00  
[REX's GUI], Update Ticker: IBM: $10.00  
[ANN's GUI], Update Ticker: IBM: $10.00  

2.15) User REX does a Book Depth query for IBM
IBM Book Depth: $10.00 x 50 -- $10.10 x 100

2.16) User REX enters a market order for IBM, SELL 75@MKT - results in a trade
[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.11} Fill Message: SELL 75 IBM at $10.10 leaving 25 [Tradable Id: ANNIBM449981183598080]

[REX's GUI], Update Market Activity: {2015-05-24 22:43:38.11} Fill Message: BUY 75 IBM at $10.10 leaving 0 [Tradable Id: REXIBMMKT449981205295560]

[RAJ's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[REX's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[ANN's GUI], Update Market Data: IBM: $10.00@50 -- $10.10@25
[RAJ's GUI], Update Last Sale: IBM: $10.10@75
[REX's GUI], Update Last Sale: IBM: $10.10@75
[ANN's GUI], Update Last Sale: IBM: $10.10@75
[RAJ's GUI], Update Ticker: IBM: $10.10 ↑
[REX's GUI], Update Ticker: IBM: $10.10 ↑
[ANN's GUI], Update Ticker: IBM: $10.10 ↑

2.17) User ANN does a Book Depth query for IBM
IBM Book Depth: $10.00 x 50 -- $10.10 x 25

2.18) User ANN cancels her quote for IBM
[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.125} Cancel Message: BUY 50 IBM at $10.00 Quote BUY-Side Cancelled [Tradable Id: ANNIBM449981183594658]

[ANN's GUI], Update Market Activity: {2015-05-24 22:43:38.141} Cancel Message: SELL 25 IBM at $10.10 Quote SELL-Side Cancelled [Tradable Id: ANNIBM449981183598080]

[RAJ's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[REX's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0
[ANN's GUI], Update Market Data: IBM: $0.00@0 -- $0.00@0

2.19) Show stock holdings for all users
REX Holdings: [GOOG, IBM]
ANN Holdings: [GOOG, IBM]
RAJ Holdings: [GOOG, IBM]

2.20) Show order Id's  for all users
REX Orders: [client.TradableUserData@3d4eac69, client.TradableUserData@42a57993, client.TradableUserData@75b84c92, client.TradableUserData@4aa298b7, client.TradableUserData@7d4991ad, client.TradableUserData@28d93b30]
ANN Orders: []
RAJ Orders: [client.TradableUserData@6bc7c054, client.TradableUserData@232204a1, client.TradableUserData@1b6d3586, client.TradableUserData@4554617c]

2.21) Show positions for all users
REX Stock Value: $3,535.00, Account Costs: $-3,515.00, Net Value: $20.00
ANN Stock Value: $-505.00, Account Costs: $515.00, Net Value: $10.00
RAJ Stock Value: $-3,030.00, Account Costs: $3,000.00, Net Value: $-30.00
