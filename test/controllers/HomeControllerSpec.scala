package controllers

import akka.stream.Materializer
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.FunSpec
import org.scalatestplus.play.guice._
import play.api.http._
import play.api.mvc.{Codec, ControllerComponents, Result}
import play.api.test.FakeRequest

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

trait CirceTestHelper {

  import play.api.test.Helpers._

  def contentAsCirceJson(of: Future[Result]): Json =
    parse(contentAsString(of)).right.get

  implicit class RichFakeRequest[T](fakeRequest: FakeRequest[T]) {
    def withCirceJsonBody(json: Json): FakeRequest[Json] = fakeRequest.withBody[Json](json)
  }

  /**
    * from play.api.libs.circe.Circe
    */
  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }

  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }
}

class HomeControllerSpec extends FunSpec with GuiceOneAppPerTest with CirceTestHelper {

  import play.api.test.Helpers._

  private[this] def createController(): HomeController = {
    implicit val ec: ExecutionContextExecutor = ExecutionContext.global
    val cc = app.injector.instanceOf[ControllerComponents]
    new HomeController(cc)(ec)
  }

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

  describe("#user") {
    it("User JSONを返す") {
      val controller = createController()

      val result = controller.user
        .apply(FakeRequest())
      val responseBody = contentAsCirceJson(result)

      assert(status(result) == 200)
      assert(responseBody ==
        Json.obj(
          "id" -> 1.asJson,
          "name" -> "John".asJson
        )
      )
    }
  }

  describe("#addUser") {
    describe("nameを渡すと") {
      it("CREATED Statusを返す") {
        implicit lazy val materializer: Materializer = app.materializer
        val controller = createController()

        val request = FakeRequest("POST", "/user")
          .withCirceJsonBody(
            Json.obj("name" -> "Mike".asJson)
          )
        val result = call(controller.addUser(), request)

        assert(status(result) == CREATED)
      }
    }

    describe("100文字より大きいnameを渡すと") {
      it("BAD_REQUEST Statusを返す") {
        implicit lazy val materializer: Materializer = app.materializer
        val controller = createController()

        val request = FakeRequest("POST", "/user")
          .withCirceJsonBody(
            Json.obj("name" -> ("a" * 101).asJson)
          )
        val result = call(controller.addUser(), request)

        assert(status(result) == BAD_REQUEST)
      }
    }

    describe("空白のnameを渡すと") {
      it("BAD_REQUEST Statusを返す") {
        implicit lazy val materializer: Materializer = app.materializer
        val controller = createController()

        val request = FakeRequest("POST", "/user")
          .withCirceJsonBody(
            Json.obj("name" -> " ".asJson)
          )
        val result = call(controller.addUser(), request)

        assert(status(result) == BAD_REQUEST)
      }
    }
  }


}
