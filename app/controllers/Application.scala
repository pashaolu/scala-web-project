package controllers

import java.util.concurrent.TimeUnit

import actors.StatsActor
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import play.api.libs.ws.WSClient
import services.{StockMarketService, SunService, WeatherService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



class Application (components: ControllerComponents, assets: Assets,
                   sunService: SunService,
                   weatherService: WeatherService,
                   stockMarketService: StockMarketService,
                   actorSystem: ActorSystem) extends AbstractController(components) {

  def index = Action.async {
    val lat = -33.8830
    val lon = 151.2167
    val symbol = "DJI"

    val sunInfoFuture = sunService.getSunInfo(lat, lon)
    val tempFuture = weatherService.getTemperature(lat, lon)
    val stockmarketFuture = stockMarketService.getStockMarketInfo(symbol)

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestsFuture = (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetsStats).mapTo[Int]

    for {
      sunInfo <- sunInfoFuture
      temperature <- tempFuture
      stockmarketInfo <- stockmarketFuture
      requests <- requestsFuture
    } yield {
      Ok(views.html.index(sunInfo, temperature, stockmarketInfo, requests))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
