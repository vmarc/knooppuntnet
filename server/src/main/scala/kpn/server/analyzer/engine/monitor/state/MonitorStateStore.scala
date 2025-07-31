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
    val newTiles = monitorStateTileBuilder.build(state)
    val oldTiles = monitorRouteRepository.stateTiles(state.routeId, state.relationId)

    val newTileKeys = newTiles.map(_.key).toSet
    val oldTileKeys = oldTiles.map(_.key).toSet

    val obsoleteTileKeys = oldTileKeys -- newTileKeys

    val obsoleteTiles = oldTiles.filter(tile => obsoleteTileKeys.contains(tile.key))
    obsoleteTiles.foreach(tile => monitorRouteRepository.deleteStateTile(tile._id))

    val createdTileKeys = newTileKeys -- oldTileKeys
    val createdTiles = newTiles.filter(tile => createdTileKeys.contains(tile.key))

    val commonTileKeys = newTileKeys -- createdTileKeys

    val updatedTiles = commonTileKeys.flatMap { key =>
      if (commonTileKeys.contains(key)) {
        oldTiles.find(_.key == key) match {
          case None => None
          case Some(oldTile) =>
            newTiles.find(_.key == key).map(newTile => newTile.copy(_id = oldTile._id)) match {
              case None => None
              case Some(newTile) =>
                if (oldTile != newTile) {
                  Some(newTile)
                }
                else {
                  None
                }
            }
        }
      }
      else {
        None
      }
    }

    (createdTiles ++ updatedTiles).foreach(monitorRouteRepository.saveStateTile)
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
