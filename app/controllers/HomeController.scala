package controllers

import io.circe.{Decoder, Json}
import javax.inject._
import play.api.libs.circe.Circe
import play.api.mvc._
import io.circe.syntax._
import io.circe.generic.auto._
import io.tabmo.circe.extra.rules.StringRules

import scala.concurrent.{ExecutionContext, Future}

case class User(id: Int, name: String)
case class UserCommand(name: String)

object UserCommand {
  import io.tabmo.json.rules._

  implicit val decoder: Decoder[UserCommand] = Decoder.instance[UserCommand] { c =>
    for {
      name <- c.downField("name").read(StringRules.maxLength(100) |+| StringRules.notBlank())
    } yield UserCommand(name)
  }
}

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
    Future(
      UserCommand.decoder.decodeJson(request.body) match {
        case Right(_) => Created
        case Left(_) => BadRequest("validation error")
      }
    )
  }

}
