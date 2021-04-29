package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.analyzers.NodeNameAnalyzer
import org.springframework.stereotype.Component

@Component
class NodeAnalyzerImpl extends NodeAnalyzer {

  override def name(tags: Tags): String = {
    ScopedNetworkType.all.flatMap(scopedNetworkType => scopedName(scopedNetworkType, tags)).mkString(" / ")
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
      scopedName(scopedNetworkType, tags).map { name =>
        NodeName(scopedNetworkType.networkType, scopedNetworkType.networkScope, name)
      }
    }
  }

  override def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      scopedName(scopedNetworkType, tags)
    }.mkString(" / ")
  }

  override def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    val nameOption = tags(scopedNetworkType.nodeRefTagKey) match {
      case Some(name) => Some(name)
      case None => scopedLongName(scopedNetworkType, tags)
    }
    nameOption.map(NodeNameAnalyzer.normalize)
  }

  override def scopedLongName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    val prefix = scopedNetworkType.key
    val nameTagKeys = Seq(s"${prefix}_name", s"$prefix:name", s"name:${prefix}_ref")
    tags.tags.find(tag => nameTagKeys.contains(tag.key)).map(_.value)
  }
}
