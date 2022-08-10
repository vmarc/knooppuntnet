package kpn.server.repository

import kpn.api.common.location.Ids
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class FactRefRepositoryImpl(database: Database) extends FactRefRepository {

  private val log = Log(classOf[FactRepositoryImpl])

  override def factRefs(subset: Subset, fact: Fact): SubsetFactRefs = {

    if (Fact.NetworkExtraMemberNode == fact) {
      networkFactsWithElementIds(subset, fact, "node")
    }
    else if (Fact.NetworkExtraMemberWay == fact) {
      networkFactsWithElementIds(subset, fact, "way")
    }
    else if (Fact.NetworkExtraMemberRelation == fact) {
      networkFactsWithElementIds(subset, fact, "relation")
    }
    else if (Fact.NodeMemberMissing == fact) {
      networkFactsWithElementRefs(subset, fact, "node")
    }
    else if (Fact.IntegrityCheckFailed == fact) {
      integrityCheckFailedFactRefs(subset)
    }
    else {
      routeFactRefs(subset, fact)
    }
  }

  private def routeFactRefs(subset: Subset, fact: Fact): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.country(subset.country)),
            equal("labels", Label.networkType(subset.networkType)),
            equal("labels", Label.fact(fact)),
          )
        ),
        project(
          fields(
            include("_id")
          )
        )
      )

      val refs = database.routes.aggregate[Id](pipeline, log).map(_._id)
      (s"routeRefs: ${refs.size}", SubsetFactRefs("relation", refs))
    }
  }

  private def networkFactsWithElementIds(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$facts"),
        filter(equal("facts.name", fact.name)),
        project(
          fields(
            excludeId(),
            computed("ids", "$facts.elementIds"),
          )
        )
      )

      val elementIds = database.networkInfos.aggregate[Ids](pipeline, log).flatMap(_.ids)
      val subsetFactRefs = SubsetFactRefs(elementType, elementIds)
      (s"network element references '$elementType': ${elementIds.mkString(", ")}", subsetFactRefs)
    }
  }

  private def networkFactsWithElementRefs(subset: Subset, fact: Fact, elementType: String): SubsetFactRefs = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("country", subset.country.domain),
            equal("summary.networkType", subset.networkType.name)
          )
        ),
        unwind("$facts"),
        filter(equal("facts.name", fact.name)),
        unwind("$facts.elements"),
        project(
          fields(
            excludeId(),
            computed("_id", "$facts.elements.id"),
          )
        )
      )

      val elementIds = database.networkInfos.aggregate[Id](pipeline, log).map(_._id)
      val subsetFactRefs = SubsetFactRefs(elementType, elementIds)
      (s"network element ref references '$elementType': ${elementIds.mkString(", ")}", subsetFactRefs)
    }
  }

  private def integrityCheckFailedFactRefs(subset: Subset): SubsetFactRefs = {
    log.debugElapsed {
      val factLabel = s"integrity-check-failed-${subset.networkType.name}"
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("labels", Label.country(subset.country)),
            equal("labels", Label.networkType(subset.networkType)),
            equal("labels", factLabel),
          )
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val refs = database.nodes.aggregate[Id](pipeline, log).map(_._id)
      (s"nodeRefs: ${refs.size}", SubsetFactRefs("node", refs))
    }
  }
}
