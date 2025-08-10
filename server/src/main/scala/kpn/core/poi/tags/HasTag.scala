package kpn.core.poi.tags

import kpn.api.custom.Tag
import kpn.api.custom.Tags

case class HasTag(tagKey: String, allowedValues: String*) extends TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean = Tags.has(tags, tagKey, allowedValues *)

  def tagKeys: Seq[String] = Seq(tagKey)
}
