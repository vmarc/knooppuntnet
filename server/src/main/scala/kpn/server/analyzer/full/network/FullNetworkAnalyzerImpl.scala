package kpn.server.analyzer.full.network

import kpn.api.common.NetworkFacts
import kpn.api.common.data.raw.RawRelation
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkNodeMember
import kpn.core.mongo.doc.NetworkRelationMember
import kpn.core.mongo.doc.NetworkWayMember
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.analyzer.load.NetworkIdsLoader
import kpn.server.analyzer.load.NetworkRelationLoader
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class FullNetworkAnalyzerImpl(
  database: Database,
  networkIdsLoader: NetworkIdsLoader,
  networkRelationLoader: NetworkRelationLoader
) extends FullNetworkAnalyzer {

  private val log = Log(classOf[FullNetworkAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {

    val existingNetworkIds = findActiveNetworkIds()

    val allNetworkIds = networkIdsLoader.load(context.timestamp)

    val networkIds = allNetworkIds.zipWithIndex.flatMap { case (networkId, index) =>
      Log.context(s"${index + 1}/${allNetworkIds.size}, $networkId") {
        networkRelationLoader.load(Some(context.timestamp), networkId).map { rawRelation =>
          database.networks.save(toDoc(rawRelation))
          networkId
        }
      }
    }

    val obsoleteNetworkIds = (existingNetworkIds.toSet -- networkIds).toSeq.sorted
    deactivateObsoleteNetworks(obsoleteNetworkIds)

    context.copy(
      obsoleteNetworkIds = obsoleteNetworkIds,
      networkIds = networkIds,
    )
  }

  private def findActiveNetworkIds(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val networkIds = database.networks.aggregate[Id](pipeline, log).map(_._id)
      (s"{$networkIds.size} existing networks", networkIds)
    }
  }

  private def deactivateObsoleteNetworks(networkIds: Seq[Long]): Unit = {
    networkIds.foreach { networkId =>
      database.networks.findById(networkId, log).map { networkDoc =>
        // TODO log.warn(...)
        database.networks.save(networkDoc.copy(active = false), log)
      }
      // TODO should also deactivate NetworkInfoDoc's? Better do this in  PostProcessor?
    }
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
      NetworkFacts(),
      rawRelation.tags
    )
  }
}
