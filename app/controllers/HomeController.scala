package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def appSummary = Action {
    Ok(Json.obj("content" -> "Marc"))
  }

  def page1 = Action {
    Ok(Json.obj("content" -> "CONTENTS 1"))
  }

  def page2 = Action {
    Ok(Json.obj("content" -> "CONTENTS 2"))
  }

  def page3 = Action {
    Ok(Json.obj("content" -> "CONTENTS 3"))
  }

}
