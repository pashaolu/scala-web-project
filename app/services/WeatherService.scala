package services

import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat
import model.WeatherInfo
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class WeatherService(wsClient: WSClient) {

  def KelvinToCelsius(n:Double): Double = {
    f"${n - 273.15}%1.2f".toDouble
  }

  def UnixTimestampToDateString(ts:Long) = {
    new DateTime(ts *1000).toDateTime.toString("yyyy-MM-dd HH:mm:ss")
  }

  def getWeatherInfo(lat:Double, lon:Double): Future[WeatherInfo] = {
    val weatherResponseF = wsClient.url(s"http://api.openweathermap.org/data/2.5/weather?q=London&appid=5375edbe0e60132bed52e51b39aa0957").get()
    weatherResponseF.map{response =>
      val weatherJson = response.json
      val timestamp = (weatherJson \ "dt").as[Long]
      val temp_K = (weatherJson \ "main" \ "temp").as[Double]
      val main = (weatherJson \ "weather" \ 0 \ "main").as[String]
      val description = (weatherJson \ "weather" \ 0 \ "description").as[String]
      val pressure = (weatherJson \ "main" \ "pressure").as[Double]
      val humidity = (weatherJson \ "main" \ "humidity").as[Double]
      val location = (weatherJson \ "name" ).as[String]

      val summary = s"$main: $description"
      val temperature = KelvinToCelsius(temp_K)
      val datetime = UnixTimestampToDateString(timestamp)

      val weatherInfo = WeatherInfo(datetime, summary, temperature,humidity,pressure,location)
      weatherInfo
    }

  }

}
