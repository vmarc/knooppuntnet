package kpn.shared.node

import kpn.shared.NetworkType
import kpn.shared.data.Tags

object NodeNetworkTypeAnalyzer {

  def networkTypes(tags: Tags): Seq[NetworkType] = {
    NetworkType.all.filter(networkType => tags.has(networkType.nodeTagKey))
  }
}
