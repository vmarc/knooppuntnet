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
    ScopedNetworkType.all.flatMap(n => nameIn(tags, n.nodeRefTagKey, n.nodeNameTagKey)).mkString(" / ")
  }

  override def names(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      nameIn(tags, scopedNetworkType.nodeRefTagKey, scopedNetworkType.nodeNameTagKey).map { name =>
        NodeName(scopedNetworkType, name)
      }
    }
  }

  override def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      nameIn(tags, scopedNetworkType.nodeRefTagKey, scopedNetworkType.nodeNameTagKey)
    }.mkString(" / ")
  }

  override def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    nameIn(tags, scopedNetworkType.nodeRefTagKey, scopedNetworkType.nodeNameTagKey)
  }

  private def nameIn(tags: Tags, key1: String, key2: String): Option[String] = {
    val nameOption = tags(key1) match {
      case Some(name) => Some(name)
      case None => tags(key2)
    }
    nameOption.map(NodeNameAnalyzer.normalize)
  }
}
