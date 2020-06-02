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
import services.{WeatherService, UserProfileService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global



class Application (components: ControllerComponents, assets: Assets,
                   userProfileService: UserProfileService,
                   weatherService: WeatherService,
                   actorSystem: ActorSystem) extends AbstractController(components) {

  def index = Action.async {
    val lat = -33.8830
    val lon = 151.2167

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestsFuture = (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetsStats).mapTo[Int]
    val userInfoFuture = userProfileService.getUserProfileInfo()
    val weatherInfoFuture = weatherService.getWeatherInfo(lat, lon)

    for {
      userInfo <- userInfoFuture
      weatherInfo <- weatherInfoFuture
      requests <- requestsFuture
    } yield {
      Ok(views.html.index(userInfo, weatherInfo, requests))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
