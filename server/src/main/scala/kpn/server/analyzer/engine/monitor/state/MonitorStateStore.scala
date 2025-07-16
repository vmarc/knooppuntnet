package kpn.server.analyzer.engine.monitor.state

import kpn.api.base.ObjectId
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorStateStore(
  monitorRouteRepository: MonitorRouteRepository,
  monitorStateTileBuilder: MonitorStateTileBuilder
) {

  def saveState(state: MonitorState): Unit = {
    monitorRouteRepository.saveState(state)
    val tiles = monitorStateTileBuilder.build(state)
    tiles.foreach(monitorRouteRepository.saveStateTile)
  }

  def deleteStates(routeId: ObjectId): Unit = {
    monitorRouteRepository.deleteStates(routeId)
    monitorRouteRepository.deleteStateTiles(routeId)
  }

  def deleteState(routeId: ObjectId, subRelationId: Long): Unit = {
    monitorRouteRepository.deleteState(routeId, subRelationId)
  }

  def deleteStateById(objectId: ObjectId): Unit = {
    monitorRouteRepository.deleteStateById(objectId)
  }
}
