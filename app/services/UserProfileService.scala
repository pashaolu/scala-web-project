package services

import model.UserProfileInfo
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserProfileService(wsClient: WSClient) {

  def getUserProfileInfo(): Future[UserProfileInfo] = {
    val responseFuture = wsClient.url("https://pipl.ir/v1/getPerson").get
    responseFuture.map{ response =>
      val json = response.json

      val fname = (json \ "person" \ "personal" \ "name").as[String]
      val lname = (json \ "person" \ "personal" \ "last_name").as[String]
      val age = (json \ "person" \ "personal" \ "age").as[Int]
      val username = (json \ "person" \ "online_info" \ "username" ).as[String]
      val email = (json \ "person" \ "online_info" \ "email" ).as[String]
      val phone = (json \ "person" \ "personal" \ "cellphone" ).as[String]

      val fullname = s"$fname $lname"
      val userProfileInfo = UserProfileInfo( fullname, username, email, age, phone)
      userProfileInfo
    }
  }

}
