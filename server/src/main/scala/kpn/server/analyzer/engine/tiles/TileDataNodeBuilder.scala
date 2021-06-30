package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.Fact
import kpn.api.custom.FactLevel
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

import scala.util.Failure
import scala.util.Success

class TileDataNodeBuilder {

  private val prioritizedScopes = Seq(
    NetworkScope.regional, // prefer regional over local
    NetworkScope.local,
    NetworkScope.national,
    NetworkScope.international
  )

  def build(networkType: NetworkType, node: NodeInfo): TileDataNode = {
    doBuild(networkType, node.id, node.latitude, node.longitude, node.orphan, node.facts, node.tags)
  }

  def build(networkType: NetworkType, node: NetworkInfoNode): TileDataNode = {
    doBuild(networkType, node.id, node.latitude, node.longitude, node.facts.contains(Fact.OrphanNode), node.facts, node.tags)
  }

  private def doBuild(networkType: NetworkType, id: Long, latitude: String, longitude: String, orphan: Boolean, facts: Seq[Fact], tags: Tags): TileDataNode = {

    val refOption: Option[String] = {
      prioritizedScopes.flatMap { scope =>
        tags(s"${scope.letter}${networkType.letter}n_ref").filterNot(_ == "o")
      }.headOption
    }

    val nameOption: Option[String] = prioritizedScopes.flatMap { scope =>
      tags(s"${scope.letter}${networkType.letter}n_name")
    }.headOption match {
      case Some(value) => Some(value)
      case None =>
        prioritizedScopes.flatMap { scope =>
          tags(s"${scope.letter}${networkType.letter}n:name")
        }.headOption
    }

    val (ref, name) = refOption match {
      case Some(refValue) => (Some(refValue), nameOption)
      case None =>
        nameOption match {
          case None => (None, None)
          case Some(nameValue) =>
            if (nameValue.length <= 3) {
              (nameOption, None)
            }
            else {
              (None, nameOption)
            }
        }
    }

    val surveyDateTry = SurveyDateAnalyzer.analyze(tags)
    val surveyDate = surveyDateTry match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }

    TileDataNode(
      id,
      ref,
      name,
      latitude,
      longitude,
      layer(orphan, facts),
      surveyDate,
      tags("state")
    )
  }


  private def layer(orphan: Boolean, facts: Seq[Fact]): String = {
    if (orphan) {
      if (hasError(facts)) {
        "error-orphan-node"
      }
      else {
        "orphan-node"
      }
    }
    else if (hasError(facts)) {
      "error-node"
    }
    else {
      "node"
    }
  }

  private def isOrphan(node: NetworkInfoNode): Boolean = {
    !node.definedInRelation && node.routeReferences.isEmpty
  }

  private def hasError(facts: Seq[Fact]): Boolean = {
    facts.exists(_.level == FactLevel.ERROR)
  }

}
