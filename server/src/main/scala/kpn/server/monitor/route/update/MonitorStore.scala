package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorStore(
  monitorRouteRepository: MonitorRouteRepository,
) {

  private val log = Log(classOf[MonitorStore])

  def saveReference(reference: MonitorReference): Unit = {
    monitorRouteRepository.saveReference(reference)
  }

  def deleteReferences(routeId: ObjectId): Unit = {
    monitorRouteRepository.deleteReferences(routeId)
  }

  def deleteReference(routeId: ObjectId, subRelationId: Long): Unit = {
    monitorRouteRepository.deleteReference(routeId, subRelationId)
  }

  def deleteReferenceById(objectId: ObjectId): Unit = {
    monitorRouteRepository.deleteReferenceById(objectId)
  }

  def saveState(state: MonitorState): Unit = {
    monitorRouteRepository.saveState(state)
  }

  def deleteStates(routeId: ObjectId): Unit = {
    monitorRouteRepository.deleteStates(routeId)
  }

  def deleteState(routeId: ObjectId, subRelationId: Long): Unit = {
    monitorRouteRepository.deleteState(routeId, subRelationId)
  }

  def deleteStateById(objectId: ObjectId): Unit = {
    monitorRouteRepository.deleteStateById(objectId)
  }
}
