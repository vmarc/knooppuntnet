package kpn.shared.node

import kpn.shared.NetworkType
import kpn.shared.NodeName
import kpn.shared.data.Tags

object NodeNameAnalyzer {

  def name(tags: Tags): String = {
    val tagKeys = NetworkType.all.map(t => t.nodeTagKey)
    tags.tags.filter(tag => tagKeys.contains(tag.key)).map(_.value).mkString(" / ")
  }

  def names(tags: Tags): Seq[NodeName] = {
    Seq()
  }

  def name(networkType: NetworkType, tags: Tags): String = {
    tags(networkType.nodeTagKey).getOrElse("")
  }

}
