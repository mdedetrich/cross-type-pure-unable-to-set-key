package views

import scalatags.text.Frag
import scalatags.Text.all._
import controllers.routes

object Application {
  def index(implicit environment: play.api.Environment): Frag = {
    scalatags.Text.tags.html(
      head(
        title := "Receive",
        link(rel := "stylesheet",
             media := "screen",
             href := routes.Assets.versioned("stylesheets/main.css").url)
      ),
      body(
        div(id := "react-container"),
        raw(playscalajs.html.scripts("client").body)
      )
    )
  }
}
