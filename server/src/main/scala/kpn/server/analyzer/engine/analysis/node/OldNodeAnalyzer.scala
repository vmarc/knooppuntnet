package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags

trait OldNodeAnalyzer {

  def name(tags: Tags): String

  def longName(tags: Tags): Option[String]

  def names(tags: Tags): Seq[NodeName]

  def name(networkType: NetworkType, tags: Tags): String

  def scopedName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String]

  def scopedLongName(scopedNetworkType: ScopedNetworkType, tags: Tags): Option[String]

}
