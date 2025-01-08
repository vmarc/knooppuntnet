package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.ScopedRouteType
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeTagAnalysis

import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success

object NodeTagAnalyzer {

  def analyze(tagable: Tagable): Option[NodeTagAnalysis] = {
    if (tagable.hasTag("network:type", "node_network")) {
      analyzeTags(tagable)
    }
    else {
      None
    }
  }

  private def analyzeTags(tagable: Tagable): Option[NodeTagAnalysis] = {
    val facts = ListBuffer[Fact]()
    val nodeNames = findNodeNames(tagable)
    if (nodeNames.isEmpty) {
      None
    }
    else {
      val name = nodeNames.map(_.name).mkString(" / ")
      val lastSurvey = analyzeSurvey(tagable, facts)
      Some(
        NodeTagAnalysis(
          name,
          nodeNames,
          lastSurvey,
          facts.toSeq
        )
      )
    }
  }

  private def analyzeSurvey(tagable: Tagable, facts: ListBuffer[Fact]): Option[Day] = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(tagable)
    surveyDateTry match {
      case Success(v) => v
      case Failure(_) =>
        facts.addOne(Fact.NodeInvalidSurveyDate)
        None
    }
  }

  private def findNodeNames(tagable: Tagable): Seq[NodeName] = {
    ScopedRouteType.all.flatMap { scopedRouteType =>
      determineScopedName(scopedRouteType, tagable).map { name =>
        val longName = determineScopedLongName(scopedRouteType, tagable)
        NodeName(
          scopedRouteType.routeType,
          scopedRouteType.networkScope,
          name.name,
          longName.map(_.name),
          proposed = name.proposed
        )
      }
    }.sortBy(_.name)
  }

  private def determineScopedName(scopedRouteType: ScopedRouteType, tagable: Tagable): Option[Name] = {
    val nameOption = tagable.tagValue(scopedRouteType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tagable)))
      case None =>
        tagable.tagValue(scopedRouteType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(scopedRouteType, tagable)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(scopedRouteType: ScopedRouteType, tagable: Tagable): Option[Name] = {
    val prefix = scopedRouteType.key
    val nameTagKeys = Seq(
      s"${prefix}_name",
      s"$prefix:name",
      s"name:${prefix}_ref"
    )
    val proposedNameTagKeys = Seq(
      s"proposed:${prefix}_name",
      s"proposed:$prefix:name",
      s"proposed:name:${prefix}_ref"
    )
    tagable.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value) match {
      case Some(name) => Some(Name(name, stateProposed(tagable)))
      case None =>
        tagable.tags.find(tag => proposedNameTagKeys.contains(tag.key)).map(_.value).map { name =>
          Name(name, proposed = true)
        }
    }
  }

  private def stateProposed(tagable: Tagable): Boolean = {
    tagable.hasTag("state", "proposed")
  }
}
