package actors

import akka.actor.Actor
import play.api.Logger

object StatsActor {
  val name = "statsActor"
  val path = s"/user/$name"

  case object Ping
  case object RequestReceived
  case object GetsStats
}

class StatsActor extends Actor{
  import StatsActor._

  var counter = 0

  override def receive: Receive = {
    case Ping => ()
    case RequestReceived => {
      counter += 1
      Logger.info(counter.toString)
    }
    case GetsStats => sender() ! counter
  }

}
