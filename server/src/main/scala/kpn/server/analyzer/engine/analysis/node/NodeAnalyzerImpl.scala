package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import org.springframework.stereotype.Component

@Component
class NodeAnalyzerImpl extends NodeAnalyzer {

  override def name(tags: Tags): String = {
    ScopedNetworkType.all.flatMap(n => tags(n.nodeRefTagKey)).mkString(" / ")
  }

  override def names(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeRefTagKey).map { name =>
        NodeName(scopedNetworkType, name)
      }
    }
  }

  override def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeRefTagKey)
    }.mkString(" / ")
  }

  override def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String] = {
    tags(scopedNetworkType.nodeRefTagKey)
  }

}
