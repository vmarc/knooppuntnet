package kpn.core.poi.tags

import kpn.shared.data.Tags

case class TagContains(tagKey: String, tagValues: String*) extends TagExpression {

  def evaluate(tags: Tags): Boolean = {
    tags.tags.exists { tag =>
      tag.key == tagKey && tagValues.exists(tagValue => tag.value.contains(tagValue))
    }
  }

}
