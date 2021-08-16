package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkNodeMember
import kpn.core.mongo.doc.NetworkRelationMember
import kpn.core.mongo.doc.NetworkWayMember
import kpn.core.util.Log
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class BulkNetworkAnalyzerImpl(
  overpassRepository: OverpassRepository,
  networkRepository: NetworkRepository
) extends BulkNetworkAnalyzer {

  private val log = Log(classOf[BulkNetworkAnalyzerImpl])

  def analyze(timestamp: Timestamp, networkIds: Seq[Long]): Seq[Long] = {
    val batchSize = 25
    networkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (networkIdsBatch, index) =>
      Log.context(s"${index * batchSize}/${networkIds.size}") {
        log.infoElapsed {
          val networkDocs = overpassRepository.relations(timestamp, networkIdsBatch).map(toDoc)
          networkRepository.bulkSave(networkDocs)
          val ids = networkDocs.map(_._id)
          (s"analyzed ${ids.size} networks: ${ids.mkString(", ")}", ids)
        }
      }
    }.toSeq
  }

  private def toDoc(rawRelation: RawRelation): NetworkDoc = {

    val nodeMembers = rawRelation.nodeMembers.map { member =>
      NetworkNodeMember(member.ref, member.role)
    }

    val wayMembers = rawRelation.wayMembers.map { member =>
      NetworkWayMember(member.ref, member.role)
    }

    val relationMembers = rawRelation.relationMembers.map { member =>
      NetworkRelationMember(member.ref, member.role)
    }

    NetworkDoc(
      rawRelation.id,
      active = true,
      rawRelation.timestamp,
      nodeMembers,
      wayMembers,
      relationMembers,
      rawRelation.tags
    )
  }
}
