package kpn.shared.node

import kpn.shared.NetworkType
import kpn.shared.data.Tags

object NodeNameAnalyzer {
  def name(tags: Tags): String = {
    val tagKeys = NetworkType.all.map(t => t.nodeTagKey)
    tags.tags.filter(tag => tagKeys.contains(tag.key)).map(_.value).mkString(" / ")
  }

  def name(networkType: NetworkType, tags: Tags): String = {
    tags(networkType.nodeTagKey).getOrElse("")
  }

  def rcnName(tags: Tags): String = name(NetworkType.bicycle, tags)
  def rwnName(tags: Tags): String = name(NetworkType.hiking, tags)
  def rhnName(tags: Tags): String = name(NetworkType.horseRiding, tags)
  def rmnName(tags: Tags): String = name(NetworkType.motorboat, tags)
  def rpnName(tags: Tags): String = name(NetworkType.canoe, tags)
  def rinName(tags: Tags): String = name(NetworkType.inlineSkates, tags)
}
