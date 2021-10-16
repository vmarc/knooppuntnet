package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.node.domain.NodeTagAnalysis

import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success

object NodeTagAnalyzer {

  def analyze(tags: Tags): Option[NodeTagAnalysis] = {
    if (tags.has("network:type", "node_network")) {
      analyzeTags(tags)
    }
    else {
      None
    }
  }

  private def analyzeTags(tags: Tags): Option[NodeTagAnalysis] = {
    val facts = ListBuffer[Fact]()
    val nodeNames = findNodeNames(tags)
    if (nodeNames.isEmpty) {
      None
    }
    else {
      val name = nodeNames.map(_.name).mkString(" / ")
      val lastSurvey = analyzeSurvey(tags, facts)
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

  private def analyzeSurvey(tags: Tags, facts: ListBuffer[Fact]): Option[Day] = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(tags)
    surveyDateTry match {
      case Success(v) => v
      case Failure(_) =>
        facts.addOne(Fact.NodeInvalidSurveyDate)
        None
    }
  }

  private def findNodeNames(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      determineScopedName(scopedNetworkType, tags).map { name =>
        val longName = determineScopedLongName(scopedNetworkType, tags)
        NodeName(
          scopedNetworkType.networkType,
          scopedNetworkType.networkScope,
          name.name,
          longName.map(_.name),
          proposed = name.proposed
        )
      }
    }.sortBy(_.name)
  }

  private def determineScopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[Name] = {
    val nameOption = tags(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tags)))
      case None =>
        tags(scopedNetworkType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(scopedNetworkType, tags)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[Name] = {
    val prefix = scopedNetworkType.key
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
    tags.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value) match {
      case Some(name) => Some(Name(name, stateProposed(tags)))
      case None =>
        tags.tags.find(tag => proposedNameTagKeys.contains(tag.key)).map(_.value).map { name =>
          Name(name, proposed = true)
        }
    }
  }

  private def stateProposed(tags: Tags): Boolean = {
    tags.has("state", "proposed")
  }
}
