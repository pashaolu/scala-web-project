package model

case class WeatherInfo(
  datetime: String,
  summary: String,
  temperature: Double,
  humidity: Double,
  pressure: Double,
  location: String
)
