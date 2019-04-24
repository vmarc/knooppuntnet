package controllers

import javax.inject._

import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents

@Singleton
class FrontendController @Inject()(assets: Assets, errorHandler: HttpErrorHandler, config: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  def assetOrDefault(resource: String): Action[AnyContent] = {

    println("serving resource " + resource)

    if (resource.startsWith("nl/")) {
      languageAsset("nl", resource)
    }
    else if (resource.startsWith("fr/")) {
      languageAsset("fr", resource)
    }
    else if (resource.startsWith("de/")) {
      languageAsset("de", resource)
    }
    else {
      languageAsset("en", resource)
    }
  }

  private def isActualAsset(resource: String): Boolean = {
    resource.endsWith("manifest.json") ||
      resource.endsWith(".ico") ||
      resource.endsWith(".svg") ||
      resource.endsWith(".png") ||
      resource.endsWith(".js") ||
      resource.endsWith(".css")
  }

  private def languageAsset(language: String, resource: String): Action[AnyContent] = {
    if (isActualAsset(resource)) {
      assets.at(resource)
    }
    else {
      assets.at(language + "/index.html")
    }
  }

}
