package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
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
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      determineScopedName(scopedNetworkType, tagable).map { name =>
        val longName = determineScopedLongName(scopedNetworkType, tagable)
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

  private def determineScopedName(scopedNetworkType: ScopedNetworkType, tagable: Tagable): Option[Name] = {
    val nameOption = tagable.tagValue(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(Name(name, proposed = stateProposed(tagable)))
      case None =>
        tagable.tagValue(scopedNetworkType.proposedNodeRefTagKey) match {
          case Some(name) => Some(Name(name, proposed = true))
          case None => determineScopedLongName(scopedNetworkType, tagable)
        }
    }
    nameOption.map(n => n.copy(name = NodeUtil.normalize(n.name)))
  }

  private def determineScopedLongName(scopedNetworkType: ScopedNetworkType, tagable: Tagable): Option[Name] = {
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
