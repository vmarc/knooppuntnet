package kpn.core.poi.tags

import kpn.api.custom.Tag
import kpn.api.custom.Tags

case class NotHasTag(tagKey: String, allowedValues: String*) extends TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean = !Tags.has(tags, tagKey, allowedValues: _*)

  def tagKeys: Seq[String] = Seq.empty
}
