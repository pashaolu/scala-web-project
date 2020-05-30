package model

case class StockMarketInfo(
  symbol: Option[String],
  name: Option[String],
  currency: Option[String],
  price: Option[Double],
  close_yesterday: Option[Double],
  volume: Option[Long],
  timezone: Option[String],
  last_trade_time: Option[String]
)