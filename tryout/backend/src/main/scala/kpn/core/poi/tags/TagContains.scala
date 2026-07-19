package kpn.core.poi.tags

import kpn.api.custom.Tag

case class TagContains(tagKey: String, tagValues: String*) extends TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean = {
    tags.exists { tag =>
      tag.key == tagKey && tagValues.exists(tagValue => tag.value.contains(tagValue))
    }
  }

  def tagKeys: Seq[String] = Seq(tagKey)
}
