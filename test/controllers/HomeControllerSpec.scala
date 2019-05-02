package controllers

import org.scalatest.FunSpec
import org.scalatestplus.play.guice._
import play.api.mvc.{ControllerComponents, Result}
import play.api.test._
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class HomeControllerSpec extends FunSpec with GuiceOneAppPerTest {
  private[this] def createController(): HomeController = {
    implicit val ec: ExecutionContextExecutor = ExecutionContext.global
    val cc = app.injector.instanceOf[ControllerComponents]
    new HomeController(cc)(ec)
  }

  import play.api.test.Helpers._

  private[this] def contentAsCirceJson(of: Future[Result]): Json =
    parse(contentAsString(of)).right.get


  describe("#hello") {
    it("hello world JSONを返す") {
      val controller = createController()

      val result = controller.hello
        .apply(FakeRequest())
      val responseBody = contentAsCirceJson(result)

      assert(status(result) == 200)
      assert(responseBody == Json.obj("hello" -> "world".asJson))
    }
  }

}
