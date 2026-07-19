package kpn.server.analyzer.engine.monitor.state

import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class MonitorStateStore(
  monitorRouteRepository: MonitorRouteRepository,
  monitorStateTileBuilder: MonitorStateTileBuilder
) {

  def saveState(state: MonitorState): Unit = {
    monitorRouteRepository.saveState(state)
    updateTiles(state)
  }

  def deleteStates(routeId: ObjectId): Unit = {
    monitorRouteRepository.deleteStates(routeId)
    monitorRouteRepository.deleteStateTiles(routeId)
  }

  private def updateTiles(state: MonitorState): Unit = {
    val oldTiles = monitorRouteRepository.stateTiles(state.routeId, state.relationId)
    val newTiles = monitorStateTileBuilder.build(state)

    val oldTileKeys = oldTiles.map(_.key).toSet
    val newTileKeys = newTiles.map(_.key).toSet

    deleteObsoleteTiles(oldTiles, oldTileKeys, newTileKeys)
    saveNewAndUpdatedTiles(oldTiles, newTiles, oldTileKeys, newTileKeys)
  }

  private def deleteObsoleteTiles(
    oldTiles: Seq[MonitorStateTile],
    oldTileKeys: Set[String],
    newTileKeys: Set[String]
  ): Unit = {
    val obsoleteTileKeys = oldTileKeys -- newTileKeys
    val obsoleteTiles = oldTiles.filter(tile => obsoleteTileKeys.contains(tile.key))
    obsoleteTiles.foreach(tile => monitorRouteRepository.deleteStateTile(tile._id))
  }

  private def saveNewAndUpdatedTiles(
    oldTiles: Seq[MonitorStateTile],
    newTiles: Seq[MonitorStateTile],
    oldTileKeys: Set[String],
    newTileKeys: Set[String]
  ): Unit = {

    val createdTileKeys = newTileKeys -- oldTileKeys
    val createdTiles = newTiles.filter(tile => createdTileKeys.contains(tile.key))

    val commonTileKeys = newTileKeys.intersect(oldTileKeys)
    val updatedTiles = findUpdatedTiles(oldTiles, newTiles, commonTileKeys)

    (createdTiles ++ updatedTiles).foreach(monitorRouteRepository.saveStateTile)
  }

  private def findUpdatedTiles(
    oldTiles: Seq[MonitorStateTile],
    newTiles: Seq[MonitorStateTile],
    commonTileKeys: Set[String]
  ): Seq[MonitorStateTile] = {

    commonTileKeys.flatMap { key =>
      for {
        oldTile <- oldTiles.find(_.key == key)
        newTile <- newTiles.find(_.key == key)
        if oldTile != newTile
      } yield newTile.copy(_id = oldTile._id) // use the _id of the old tile, so that we will overwrite the old tile
    }.toSeq
  }
}
