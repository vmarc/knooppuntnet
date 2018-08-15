package controllers

import javax.inject._

import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._
/**
  * Frontend controller managing all static resource associate routes.
  * @param assets Assets controller reference.
  * @param cc Controller components reference.
  */
@Singleton
class FrontendController @Inject()(assets: Assets, errorHandler: HttpErrorHandler, config: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  def assetOrDefault(resource: String): Action[AnyContent] = {

    println("serving resource " + resource)

    if (resource.startsWith("nl/")) {
      if (resource.contains("main") ||resource.contains("poly") ||resource.contains("runtime") ||resource.contains("styles")) {
        assets.at(resource)
      }
      else {
        assets.at("nl/index.html")
      }
    }
    else if (resource.startsWith("fr/")) {
      if (resource.contains("main") ||resource.contains("poly") ||resource.contains("runtime") ||resource.contains("styles")) {
        assets.at(resource)
      }
      else {
        assets.at("fr/index.html")
      }
    }
    else if (resource.startsWith("de/")) {
      if (resource.contains("main") ||resource.contains("poly") ||resource.contains("runtime") ||resource.contains("styles")) {
        assets.at(resource)
      }
      else {
        assets.at("de/index.html")
      }
    }
    else if (resource.startsWith("en/")) {
      if (resource.contains("main") ||resource.contains("poly") ||resource.contains("runtime") ||resource.contains("styles")) {
        assets.at(resource)
      }
      else {
        assets.at("en/index.html")
      }
    }
    else  {
      if (resource.contains("main") ||resource.contains("poly") ||resource.contains("runtime") ||resource.contains("styles")) {
        assets.at(resource)
      }
      else {
        assets.at("en/index.html")
      }
    }


//
//    else if (resource.startsWith(config.get[String]("apiPrefix"))){
//      Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
//    } else {
////      if (resource.contains(".")) {
//        assets.at(resource)
////      } else {
////        indexEN(resource)
////      }
//    }
  }
}
