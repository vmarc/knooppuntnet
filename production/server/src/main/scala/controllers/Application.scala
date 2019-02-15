package controllers

import java.nio.ByteBuffer

import akka.actor.ActorSystem
import boopickle.DefaultBasic._
import kpn.core.db.json.JsonFormats
import kpn.core.facade.AnalyzerFacade
import kpn.core.util.Log
import kpn.shared.ApiResponse
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.filter.ChangesParameters
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import play.api.mvc.Result
import play.api.mvc.Results
import services.ApiService
import services.AuthenticationService
import services.JsonApiService
import spray.json.JsNull
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsValue
import spray.json.JsonParser
import spray.json.JsonWriter

object Router extends autowire.Server[ByteBuffer, Pickler, Pickler] {
  override def read[R: Pickler](p: ByteBuffer): R = Unpickle[R].fromBytes(p)

  override def write[R: Pickler](r: R): ByteBuffer = Pickle.intoBytes(r)
}

class Application(
  analyzerFacade: AnalyzerFacade,
  controllerComponents: ControllerComponents,
  assetsFinder: AssetsFinder,
  authenticiationService: AuthenticationService
)(system: ActorSystem) extends AbstractController(controllerComponents) {

  private val log = Log(classOf[Application])
  private val apiService = new ApiService(analyzerFacade)(system)
  private val jsonApiService = new JsonApiService(analyzerFacade)(system)

  def index(path: String) = Action { request =>
    val user = request.session.get("user")
    log.infoElapsed(s"$user $path") {
      Results.Ok(views.html.index(assetsFinder))
    }
  }

  def login() = Action { request =>
    authenticiationService.retrieveRequestToken(request)
  }

  def logout() = Action { request =>
    authenticiationService.logout(request)
  }

  def authenticated() = Action { request =>
    authenticiationService.retrieveAccessRequestToken(request)
  }

  def jsonApiGet(path: String) = Action { request =>

    def subsetPath(target: String): String = {
      target + """/(be|de|nl)/(rcn|rwn|rhn|rmn|rpn|rin)"""
    }

    val overview = """overview""".r
    val subsetNetworks = subsetPath("networks").r
    val subsetFacts = subsetPath("facts").r
    val subsetFactDetails = """RouteBroken/(be|de|nl)/(rcn|rwn|rhn|rmn|rpn|rin)""".r
    val subsetOrphanNodes = subsetPath("orphan-nodes").r
    val subsetOrphanRoutes = subsetPath("orphan-routes").r
    val subsetChanges = subsetPath("changes").r
    val networkDetails = """network/(\d*)""".r
    val networkMap = """network-map/(\d*)""".r
    val networkFacts = """network-facts/(\d*)""".r
    val networkNodes = """network-nodes/(\d*)""".r
    val networkRoutes = """network-routes/(\d*)""".r
    val networkChanges = """network-changes/(\d*)""".r
    val node = """node/(\d*)""".r
    val route = """route/(\d*)""".r
    val changes = """changes""".r
    val changeSet = """changeset/(\d*)/(\d*)""".r
    val mapDetailNode = """node-detail/(\d*)/(rcn|rwn|rhn|rmn|rpn|rin)""".r
    val mapDetailRoute = """route-detail/(\d*)""".r
    val poiConfiguration = """poi-configuration""".r
    val poi = """poi/(node|way|relation)/(\d*)""".r

    val userApiService = request.session.get("user") match {
      case Some(user) => new JsonApiService(analyzerFacade, Some(user))(system)
      case None => jsonApiService
    }

    path match {

      case subsetNetworks(country, networkType) =>
        val subset = Subset.of(country, networkType).get
        reply(
          userApiService.subsetNetworks(subset),
          JsonFormats.subsetNetworksPageFormat
        )

      case subsetFacts(country, networkType) =>
        val subset = Subset.of(country, networkType).get
        reply(
          userApiService.subsetFacts(subset),
          JsonFormats.subsetFactsPageFormat
        )

      case subsetFactDetails(country, networkType) =>
        val subset = Subset.of(country, networkType).get
        reply(
          userApiService.subsetFactDetails(subset, Fact.RouteBroken /* TODO */),
          JsonFormats.subsetFactDetailsPageFormat
        )

      case subsetOrphanNodes(country, networkType) =>
        val subset = Subset.of(country, networkType).get
        reply(
          userApiService.subsetOrphanNodes(subset),
          JsonFormats.subsetOrphanNodesPageFormat
        )

      case subsetOrphanRoutes(country, networkType) =>
        val subset = Subset.of(country, networkType).get
        reply(
          userApiService.subsetOrphanRoutes(subset),
          JsonFormats.subsetOrphanRoutesPageFormat
        )

      case subsetChanges(country, networkType) =>
        val subset1 = Subset.of(country, networkType).get
        reply(
          userApiService.subsetChanges(ChangesParameters(subset = Some(subset1))),
          JsonFormats.subsetChangesPageFormat
        )

      case networkDetails(networkId) =>
        reply(
          userApiService.networkDetails(networkId.toLong),
          JsonFormats.networkDetailsPageFormat
        )

      case networkMap(networkId) =>
        reply(
          userApiService.networkMap(networkId.toLong),
          JsonFormats.networkMapPageFormat
        )

      case networkFacts(networkId) =>
        reply(
          userApiService.networkFacts(networkId.toLong),
          JsonFormats.networkFactsPageFormat
        )

      case networkNodes(networkId) =>
        reply(
          userApiService.networkNodes(networkId.toLong),
          JsonFormats.networkNodesPageFormat
        )

      case networkRoutes(networkId) =>
        reply(
          userApiService.networkRoutes(networkId.toLong),
          JsonFormats.networkRoutesPageFormat
        )

      case networkChanges(networkId) =>
        val res = userApiService.networkChanges(ChangesParameters(networkId = Some(networkId.toLong)))
        reply(
          res,
          JsonFormats.networkChangesPageFormat
        )

      case node(nodeId) =>
        reply(
          userApiService.node(nodeId.toLong),
          JsonFormats.nodePageFormat
        )

      case route(routeId) =>
        reply(
          userApiService.route(routeId.toLong),
          JsonFormats.routePageFormat
        )

      case changes() =>
        reply(
          userApiService.changes(ChangesParameters()),
          JsonFormats.changesPageFormat
        )

      case changeSet(changeSetId, replicationNumber) =>
        reply(
          userApiService.changeSet(changeSetId.toLong, replicationNumber.toInt),
          JsonFormats.changeSetPageFormat
        )

      case mapDetailNode(nodeId, networkType) =>
        reply(
          userApiService.mapDetailNode(NetworkType.withName(networkType).get, nodeId.toLong),
          JsonFormats.mapDetailNodeFormat
        )

      case mapDetailRoute(routeId) =>
        reply(
          userApiService.mapDetailRoute(routeId.toLong),
          JsonFormats.mapDetailRouteFormat
        )

      case overview() =>
        reply(
          userApiService.overview(),
          JsonFormats.statisticsFormat
        )

      case poiConfiguration() =>
        reply(
          userApiService.poiConfiguration(),
          JsonFormats.tilePoiConfigurationFormat
        )

      case poi(elementType, elementId) =>
        reply(
          userApiService.poi(elementType, elementId.toLong),
          JsonFormats.poiPageFormat
        )

      case _ =>
        println("NOT FOUND " + path)
        NotFound("xxx")
    }
  }

  def jsonApiPost(path: String) = Action { request =>

    val changes = """changes""".r

    val userApiService = request.session.get("user") match {
      case Some(user) => new JsonApiService(analyzerFacade, Some(user))(system)
      case None => jsonApiService
    }

    path match {

      case changes() =>

        request.body.asJson match {
          case Some(playJsValue) =>
            val string = playJsValue.toString
            val sprayJsValue = JsonParser(string)
            val parameters = JsonFormats.changesParametersFormat.read(sprayJsValue)
            reply(
              userApiService.changes(parameters),
              JsonFormats.changesPageFormat
            )

          case None =>
            NotFound("invalid ChangeParameters in request body")
        }

      case _ =>
        println("NOT FOUND " + path)
        NotFound("xxx")
    }
  }

  private def reply[T](response: ApiResponse[T], jsonWriter: JsonWriter[T]): Result = {

    val jsonResult = response.result match {
      case Some(result) => jsonWriter.write(result)
      case None => JsNull
    }
    val jsonSituationOn: JsValue = response.situationOn match {
      case Some(situationOn) => JsonFormats.timestampFormat.write(situationOn)
      case None => JsNull
    }
    val jsonResponse = JsObject(
      Map(
        "situationOn" -> jsonSituationOn,
        "version" -> JsNumber(response.version),
        "result" -> jsonResult
      )
    )

    Ok(jsonResponse.toString()).as(JSON)
  }

  def autowireApi(path: String) = Action.async(parse.raw) { implicit request =>
    val userApiService = request.session.get("user") match {
      case Some(user) => new ApiService(analyzerFacade, Some(user))(system)
      case None => apiService
    }
    val byteString = request.body.asBytes(parse.UNLIMITED).get
    new AutoWireService().unpickle(path, byteString.asByteBuffer, userApiService)
  }
}
