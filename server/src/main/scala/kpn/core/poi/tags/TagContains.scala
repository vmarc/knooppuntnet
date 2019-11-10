package kpn.core.poi.tags

import kpn.api.custom.Tags

case class TagContains(tagKey: String, tagValues: String*) extends TagExpression {

  def evaluate(tags: Tags): Boolean = {
    tags.tags.exists { tag =>
      tag.key == tagKey && tagValues.exists(tagValue => tag.value.contains(tagValue))
    }
  }

  def tagKeys: Seq[String] = Seq(tagKey)

}
