package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import org.springframework.stereotype.Component

case class Name(name: String, proposed: Boolean)

@Component
class OldNodeAnalyzerImpl extends OldNodeAnalyzer {

  override def name(tags: Tags): String = {
    NetworkType.all.flatMap { networkType =>
      NetworkScope.all.flatMap { networkScope =>
        scopedName(ScopedNetworkType.from(networkScope, networkType), tags)
      }.distinct
    }.mkString(" / ")
  }

  override def longName(tags: Tags): Option[String] = {
    val longNames = ScopedNetworkType.all.flatMap(scopedNetworkType => scopedLongName(scopedNetworkType, tags))
    if (longNames.nonEmpty) {
      Some(longNames.mkString(" / "))
    }
    else {
      None
    }
  }

  override def names(tags: Tags): Seq[NodeName] = {
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
    }
  }

  override def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      scopedName(scopedNetworkType, tags)
    }.distinct.mkString(" / ")
  }

  override def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    determineScopedName(scopedNetworkType, tags).map(_.name)
  }

  override def scopedLongName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    determineScopedLongName(scopedNetworkType, tags).map(_.name)
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
