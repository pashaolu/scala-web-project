package services

import model.StockMarketInfo
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class StockMarketService(wsClient: WSClient) {

  def getStockMarketInfo(symbol: String): Future[StockMarketInfo] = {
    val responseFuture = wsClient.url(s"https://api.worldtradingdata.com/api/v1/stock?symbol=$symbol&api_token=DjN461GpOGZCLVs9biTDNm1N0xwtaOHtHBjWBpgViMXxmsdvWbpIQUOU4SDr").get()
    responseFuture.map{response =>
      val json = response.json

      val symbol = (json \ "data" \ 0 \ "symbol").asOpt[String]
      val name = (json \ "data" \ 0 \ "name").asOpt[String]
      val currency = (json \ "data" \ 0 \ "currency").asOpt[String]
      val price = (json \ "data" \ 0 \ "price").asOpt[Double]
      val close_yesterday = (json \ "data" \ 0 \ "close_yesterday").asOpt[Double]
      val volume = (json \ "data" \ 0 \ "volume").asOpt[Long]
      val last_trade_time =  (json \ 0 \ "data" \ "last_trade_time").asOpt[String]
      val timezone =  (json \ "data" \ 0 \ "timezone").asOpt[String]

      val stockmarketInfo = StockMarketInfo(
        symbol, name, currency, price, close_yesterday, volume, timezone, last_trade_time
      )
      stockmarketInfo
    }
  }

}
