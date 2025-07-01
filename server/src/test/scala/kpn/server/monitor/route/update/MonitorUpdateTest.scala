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
}
