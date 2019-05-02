package controllers

import io.circe.Json
import javax.inject._
import play.api.libs.circe.Circe
import play.api.mvc._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

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
      Ok(Json.obj("hello" -> "world".asJson))
    )
  }
}
