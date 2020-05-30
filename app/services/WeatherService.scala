package services

import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class WeatherService(wsClient: WSClient) {

  def getTemperature(lat:Double, lon:Double): Future[Double] = {
    val weatherResponseF = wsClient.url(s"http://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=5375edbe0e60132bed52e51b39aa0957").get()
    weatherResponseF.map{response =>
      val weatherJson = response.json
      val temperature = (weatherJson \ "main" \ "temp").as[Double]
      temperature
    }
  }

}
