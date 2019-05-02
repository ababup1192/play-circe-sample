package controllers

import io.circe.Json
import javax.inject._
import play.api.libs.circe.Circe
import play.api.mvc._
import io.circe.syntax._
import io.circe.generic.auto._
import scala.concurrent.{ExecutionContext, Future}

case class User(id: Int, name: String)
case class UserCommand(name: String)

@Singleton
class HomeController @Inject()(
  cc: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(cc) with Circe {

  def hello: Action[AnyContent] = Action.async { implicit request =>
    Future(
      Ok(Json.obj("hello" -> "world".asJson))
    )
  }

  def user: Action[AnyContent] = Action.async { implicit request =>
    Future(
      Ok(User(1, "John").asJson)
    )
  }

  def addUser(): Action[Json] = Action(circe.json(1024)).async { implicit request =>
    Future(Created)
  }

}
