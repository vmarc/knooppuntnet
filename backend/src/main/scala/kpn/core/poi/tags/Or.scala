package kpn.core.poi.tags

import kpn.api.custom.Tag

case class Or(left: TagExpression, right: TagExpression) extends TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean = left.evaluate(tags) || right.evaluate(tags)

  def tagKeys: Seq[String] = left.tagKeys ++ right.tagKeys
}
