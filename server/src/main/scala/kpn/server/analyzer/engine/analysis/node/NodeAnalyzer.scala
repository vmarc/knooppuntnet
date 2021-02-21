package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags

object NodeAnalyzer {

  def name(tags: Tags): String = {
    ScopedNetworkType.all.flatMap(n => tags(n.nodeRefTagKey)).mkString(" / ")
  }

  def names(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeRefTagKey).map { name =>
        NodeName(scopedNetworkType, name)
      }
    }
  }

  def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeRefTagKey)
    }.mkString(" / ")
  }

  def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): String = {
    tags(scopedNetworkType.nodeRefTagKey).getOrElse("no-name")
  }

}
