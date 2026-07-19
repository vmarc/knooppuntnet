package kpn.core.poi.tags

import kpn.api.custom.Tag

case class NotTagContains(tagKey: String, tagValues: String*) extends TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean = {
    tags.find(_.key == tagKey) match {
      case Some(tag) => !tagValues.exists(tagValue => tag.value.contains(tagValue))
      case None => true
    }
  }

  def tagKeys: Seq[String] = Seq.empty
}
