package kpn.web.app

import akka.actor.ActorSystem
import controllers.Application
import controllers.Assets
import controllers.AssetsConfiguration
import controllers.AssetsFinderProvider
import controllers.AssetsMetadataProvider
import controllers.ViewIndexer
import kpn.core.app.ActorSystemConfig
import kpn.core.app.ApplicationContext
import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._
import services.AuthenticationService
import services.CryptoImpl

class KpnApplicationLoader extends ApplicationLoader {
  def load(context: Context): play.api.Application = {
    new ApplicationComponents(context).application
  }
}

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) with AhcWSComponents {

  private val assetsMetadata = {
    val assetsConfiguration = AssetsConfiguration()
    val provider = new AssetsMetadataProvider(
      context.environment,
      assetsConfiguration,
      fileMimeTypes,
      context.lifecycle
    )
    provider.get
  }

  private val assetsFinder = new AssetsFinderProvider(assetsMetadata).get

  val system: ActorSystem = ActorSystemConfig.actorSystem()
  val config = new ApplicationConfigWebImpl(context.initialConfiguration)
  val applicationContext = new ApplicationContext(system, config)

  new ViewIndexer(system, applicationContext.mainDatabase, applicationContext.changeDatabase)

  lazy val httpFilters: Seq[EssentialFilter] = Seq.empty

  private val oauthApplicationKey = context.initialConfiguration.get[String]("oauthApplicationKey")
  private val oauthApplicationSecret = context.initialConfiguration.get[String]("oauthApplicationSecret")
  private val cryptoKey = context.initialConfiguration.get[String]("cryptoKey")
  private val crypto = new CryptoImpl(cryptoKey)
  private val authenticiationService = new AuthenticationService(crypto, wsClient, oauthApplicationKey, oauthApplicationSecret)(system)

  lazy val applic: Application = new Application(
    applicationContext.analyzerFacade,
    controllerComponents,
    assetsFinder,
    authenticiationService
  )(system)

  lazy val assets = new Assets(httpErrorHandler, assetsMetadata)

  override lazy val router: Router = Router.from {

    case GET(p"/assets/$file<.*>") =>
      assets.versioned(path = "/public", file = file)

    case GET(p"/hello/$to") => Action {
      Results.Ok(s"Hello $to")
    }

    case GET(p"/login") => applic.login()

    case GET(p"/logout") => applic.logout()

    case GET(p"/authenticated") => applic.authenticated()

    case GET(p"/json-api/$path<.*>") => applic.jsonApiGet(path)

    case POST(p"/json-api/$path<.*>") => applic.jsonApiPost(path)

    case POST(p"/api/$path<.*>") => applic.autowireApi(path)

    case GET(p"/$path<.*>") => applic.index(path)
  }

}
