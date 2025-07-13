package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorCommandAction
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.test.MongoTest
import kpn.core.test.OverpassData
import kpn.server.monitor.domain.MonitorRoute
import org.scalamock.scalatest.MockFactory

abstract class MonitorUpdateTest extends MongoTest with MockFactory {

  val ReferenceTimestamp1 = Timestamp(2022, 8, 1)
  val ReferenceTimestamp2 = Timestamp(2022, 8, 2)
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

  def setupRouteStructure(timestamp: Option[Timestamp], overpassData: OverpassData, relationId: Long): Unit = {
    val monitorRouteRelation = MonitorRouteRelation.from(new DataBuilder(overpassData.rawData).data.relations(relationId), None)
    (configuration.monitorRouteStructureLoader.load _).when(timestamp, relationId).returns(Some(monitorRouteRelation))
  }

  def message(commands: MonitorCommand*): MonitorMessage = {
    MonitorMessage(commands)
  }

  def add(stepId: String, description: Option[String] = None): MonitorCommand = {
    command(MonitorCommandAction.stepAdd, stepId, description)
  }

  def active(stepId: String): MonitorCommand = {
    command(MonitorCommandAction.stepActive, stepId)
  }

  def done(stepId: String): MonitorCommand = {
    command(MonitorCommandAction.stepDone, stepId)
  }

  private def command(action: MonitorCommandAction, stepId: String, description: Option[String] = None): MonitorCommand = {
    MonitorCommand(action, stepId, description)
  }

  val sameSubroute12Geometry = """{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[4.4562458,51.4618272],[4.455056,51.4614496]]]}]}"""

  def verifyDocumentCounts(
    expectedMonitorRouteCount: Int,
    expectedMonitorReferenceCount: Int,
    expectedMonitorStateCount: Int,
  ): Unit = {
    database.monitorRoutes.countDocuments() should equal(expectedMonitorRouteCount)
    database.monitorReferences.countDocuments() should equal(expectedMonitorReferenceCount)
    database.monitorStates.countDocuments() should equal(expectedMonitorStateCount)
  }

  def verifyNoReference(route: MonitorRoute, relationId: Option[Long]): Unit = {
    configuration.monitorRouteRepository.routeReference(route._id, relationId) should equal(None)
  }

  def verifyNoState(route: MonitorRoute, relationId: Long): Unit = {
    configuration.monitorRouteRepository.routeState(route._id, relationId) should equal(None)
  }

  def setupSuperRoute(): Unit = {
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc)
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc11)
    configuration.routeRepository.saveBaseRoute(TestSuperRoute.baseRouteDoc12)
    configuration.routeRepository.saveRoute(TestSuperRoute.routeDoc1)
  }
}
