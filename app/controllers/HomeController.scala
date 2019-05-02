package controllers

import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(
  cc: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def hello: Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(""))
  }
}
