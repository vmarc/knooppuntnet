package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.test.MongoTest
import kpn.core.test.OverpassData

abstract class MonitorUpdateTest extends MongoTest {

  val ReferenceTimestamp = Timestamp(2022, 8, 1)
  val CurrentTimestamp = Timestamp(2022, 8, 11, 12, 0, 0)
  val UpdateTimestamp = Timestamp(2022, 8, 12, 12, 0, 0)

  val GpxUpload1Timestamp = Timestamp(2022, 8, 12, 12, 0, 0)
  val GpxUpload2Timestamp = Timestamp(2022, 8, 13, 12, 0, 0)

  private var _configuration: MonitorUpdaterConfiguration = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    _configuration = MonitorUpdaterTestSupport.configuration(database)
  }

  def configuration: MonitorUpdaterConfiguration = _configuration

  def setupRouteStructure(overpassData: OverpassData, relationId: Long): Unit = {
    val monitorRouteRelation = MonitorRouteRelation.from(new DataBuilder(overpassData.rawData).data.relations(relationId), None)
    (configuration.monitorRouteStructureLoader.load _).when(None, relationId).returns(Some(monitorRouteRelation))
  }

  def message(commands: MonitorRouteUpdateStatusCommand*): MonitorRouteUpdateStatusMessage = {
    MonitorRouteUpdateStatusMessage(
      commands = commands
    )
  }

  def command(action: String, stepId: String, description: Option[String] = None): MonitorRouteUpdateStatusCommand = {
    MonitorRouteUpdateStatusCommand(action, stepId, description)
  }

  val routeGeometry = """{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4553911,51.4633666],[4.4562458,51.4618272]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
  val sameRouteGeometryWithLineString = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4553911,51.4633666],[4.4562458,51.4618272]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""

  val subroute12Geometry = """{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[4.4562458,51.4618272],[4.455056,51.4614496]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""
  val sameSubroute12Geometry = """{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}"""

  def setupSuperRoute(): Unit = {
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc)
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc11)
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc12)
    configuration.routeRepository.saveRoute(TestSuperRoute.routeDoc1)
  }
}
