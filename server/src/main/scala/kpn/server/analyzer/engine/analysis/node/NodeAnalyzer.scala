package kpn.server.analyzer.engine.analysis.node

import kpn.shared.NetworkType
import kpn.shared.NodeName
import kpn.shared.ScopedNetworkType
import kpn.shared.data.Tags

object NodeAnalyzer {

  def hasNodeTag(tags: Tags): Boolean = {
    ScopedNetworkType.all.exists { scopedNetworkType =>
      tags.has(scopedNetworkType.nodeTagKey)
    }
  }

  def networkTypes(tags: Tags): Seq[NetworkType] = {
    ScopedNetworkType.all.filter(n => tags.has(n.nodeTagKey)).map(_.networkType).distinct
  }

  def name(tags: Tags): String = {
    ScopedNetworkType.all.flatMap(n => tags(n.nodeTagKey)).mkString(" / ")
  }

  def names(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeTagKey).map { name =>
        NodeName(scopedNetworkType, name)
      }
    }
  }

  def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeTagKey)
    }.mkString(" / ")
  }

}
