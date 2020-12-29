package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.Fact
import kpn.api.custom.FactLevel
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

import scala.util.Failure
import scala.util.Success

class TileDataNodeBuilder {

  def build(networkType: NetworkType, node: NodeInfo): TileDataNode = {

    val refOption = node.tags(s"r${networkType.letter}n_ref").filterNot(_ == "o")

    val nameOption = node.tags(s"r${networkType.letter}n_name") match {
      case Some(value) => Some(value)
      case None =>
        node.tags(s"r${networkType.letter}n:name") match {
          case Some(value) => Some(value)
          case None => None
        }
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

    val surveyDateTry = SurveyDateAnalyzer.analyze(node.tags)
    val surveyDate = surveyDateTry match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }

    TileDataNode(
      node.id,
      ref,
      name,
      node.latitude,
      node.longitude,
      layer(node.orphan, node.facts),
      surveyDate
    )
  }

  def build(networkType: NetworkType, node: NetworkInfoNode): TileDataNode = {

    val refOption = node.tags(s"r${networkType.letter}n_ref")

    val nameOption = node.tags(s"r${networkType.letter}n_name") match {
      case Some(value) => Some(value)
      case None =>
        node.tags(s"r${networkType.letter}n:name") match {
          case Some(value) => Some(value)
          case None => None
        }
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

    val surveyDateTry = SurveyDateAnalyzer.analyze(node.tags)
    val surveyDate = surveyDateTry match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }

    TileDataNode(
      node.id,
      ref,
      name,
      node.latitude,
      node.longitude,
      layer(isOrphan(node), node.facts),
      surveyDate
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
