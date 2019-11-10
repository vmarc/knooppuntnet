package kpn.core.poi.tags

import kpn.api.custom.Tags

case class NotTagContains(tagKey: String, tagValues: String*) extends TagExpression {

  def evaluate(tags: Tags): Boolean = {
    tags.tags.find(_.key == tagKey) match {
      case Some(tag) => !tagValues.exists(tagValue => tag.value.contains(tagValue))
      case None => true
    }
  }

  def tagKeys: Seq[String] = Seq()
}
