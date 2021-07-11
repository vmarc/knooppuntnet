package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom._
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

import scala.util.{Failure, Success}

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

    var proposed = false

    val refOption: Option[String] = {
      prioritizedScopes.flatMap { scope =>
        tags(s"${scope.letter}${networkType.letter}n_ref").filterNot(_ == "o") match {
          case Some(ref) => Some(ref)
          case None =>
            tags(s"proposed:${scope.letter}${networkType.letter}n_ref").filterNot(_ == "o") match {
              case None => None
              case Some(ref) =>
                proposed = true
                Some(ref)
            }
        }
      }.headOption
    }

    val nameOption: Option[String] = prioritizedScopes.flatMap { scope =>
      tags(s"${scope.letter}${networkType.letter}n_name") match {
        case Some(name) => Some(name)
        case None =>
          tags(s"proposed:${scope.letter}${networkType.letter}n_name") match {
            case None => None
            case Some(name) =>
              proposed = true
              Some(name)
          }
      }
    }.headOption match {
      case Some(value) => Some(value)
      case None =>
        prioritizedScopes.flatMap { scope =>
          tags(s"${scope.letter}${networkType.letter}n:name") match {
            case Some(name) => Some(name)
            case None =>
              tags(s"proposed:${scope.letter}${networkType.letter}n:name") match {
                case None => None
                case Some(name) =>
                  proposed = true
                  Some(name)
              }
          }
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

    val state = tags("state") match {
      case None => if (proposed) Some("proposed") else None
      case Some(s) => Some(s)
    }

    TileDataNode(
      id,
      ref,
      name,
      latitude,
      longitude,
      layer(orphan, facts),
      surveyDate,
      state
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
