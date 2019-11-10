package kpn.core.poi.tags

import kpn.api.custom.Tags

case class NotHasTag(tagKey: String, allowedValues: String*) extends TagExpression {

  def evaluate(tags: Tags): Boolean = !tags.has(tagKey, allowedValues:_*)

  def tagKeys: Seq[String] = Seq()

}
