package kpn.server.repository

import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.doc.RawRouteDoc
import kpn.core.util.Log
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class RawDataRepositoryImpl(
  overpassRepository: OverpassRepository
) extends RawDataRepository {

  private val log = Log(classOf[RawDataRepositoryImpl])

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass node ids")
    log.infoElapsed {
      val ids = overpassRepository.nodeIds(timestamp)
      (s"Collected ${ids.size} overpass node ids", ids)
    }
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    overpassRepository.nodes(timestamp, nodeIds)
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass network ids")
    log.infoElapsed {
      val ids = overpassRepository.networkIds(timestamp)
      (s"Collected ${ids.size} overpass network ids", ids)
    }
  }

  override def networks(timestamp: Timestamp, networkIds: Seq[Long]): Seq[RawRelation] = {
    overpassRepository.relations(timestamp, networkIds)
  }

  override def routeIds(timestamp: Timestamp): Seq[Long] = {
    log.info(s"Collecting overpass route ids")
    log.infoElapsed {
      val routeIds = collectIds(timestamp, "route")
      val superRouteIds = collectIds(timestamp, "superroute")
      val allRouteIds = (routeIds.toSet ++ superRouteIds.toSet).toSeq.sorted
      (s"${allRouteIds.size} overpass route ids", allRouteIds)
    }
  }

  override def route(timestamp: Timestamp, routeId: Long): Option[RawRouteDoc] = {
    overpassRepository.relationTopLevel(timestamp, routeId).map { relation =>
      val structure = if (relation.relationIdMembers.nonEmpty) {
        overpassRepository.subRelationTree(timestamp, routeId)
      }
      else {
        None
      }
      RawRouteDoc(
        routeId,
        relation,
        structure
      )
    }
  }

  private def collectIds(timestamp: Timestamp, typeValue: String): Seq[Long] = {
    log.infoElapsed {
      val ids = overpassRepository.routeIds(timestamp, typeValue)
      (s"${ids.size} overpass $typeValue ids", ids)
    }
  }
}
