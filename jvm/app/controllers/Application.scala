package controllers

import akka.stream.Materializer
import play.api.Configuration
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

class Application(implicit val config: Configuration,
                  val materializer: Materializer,
                  val ec: ExecutionContext,
                  val environment: play.api.Environment)
    extends Controller {

  def index(string: String) = Action {
    Ok(views.Application.index)
  }

}
