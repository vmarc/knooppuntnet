package kpn.api.common.data

import kpn.api.custom.Tag
import kpn.api.custom.Tags

trait Tagable {

  def tags: Seq[Tag]

  def tagValue(key: String): Option[String] = {
    Tags.get(tags, key)
  }

  def hasTag(key: String, allowedValues: String*): Boolean = {
    Tags.has(tags, key, allowedValues: _*)
  }
}
