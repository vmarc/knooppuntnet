package kpn.core.poi.tags

import kpn.shared.data.Tags

case class NotHasTag(tagKey: String, allowedValues: String*) extends TagExpression {

  def evaluate(tags: Tags): Boolean = !tags.has(tagKey, allowedValues:_*)

}
