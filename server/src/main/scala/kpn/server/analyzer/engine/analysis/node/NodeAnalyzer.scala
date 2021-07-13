package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.{NetworkType, ScopedNetworkType, Tags}

object NodeAnalyzer {

  def hasNodeTag(tags: Tags): Boolean = {
    ScopedNetworkType.all.exists { scopedNetworkType =>
      tags.has(scopedNetworkType.nodeTagKey) || tags.has(scopedNetworkType.proposedNodeTagKey)
    }
  }

  def networkTypes(tags: Tags): Seq[NetworkType] = {
    ScopedNetworkType.all.filter { n =>
      tags.has(n.nodeTagKey) || tags.has(n.proposedNodeTagKey)
    }.map(_.networkType).distinct
  }

  def name(tags: Tags): String = {
    ScopedNetworkType.all.flatMap { n =>
      tags(n.nodeTagKey) match {
        case None => tags(n.proposedNodeTagKey)
        case Some(name) => Some(name)
      }
    }.mkString(" / ")
  }

  def names(tags: Tags): Seq[NodeName] = {
    ScopedNetworkType.all.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeTagKey) match {
        case Some(name) => Some(NodeName(scopedNetworkType, name, proposed = false))
        case None =>
          tags(scopedNetworkType.proposedNodeTagKey) match {
            case Some(name) => Some(NodeName(scopedNetworkType, name, proposed = true))
            case None => None
          }
      }
    }
  }

  def name(networkType: NetworkType, tags: Tags): String = {
    networkType.scopedNetworkTypes.flatMap { scopedNetworkType =>
      tags(scopedNetworkType.nodeTagKey) match {
        case None => tags(scopedNetworkType.proposedNodeTagKey)
        case Some(name) => Some(name)
      }
    }.distinct.mkString(" / ")
  }
}
