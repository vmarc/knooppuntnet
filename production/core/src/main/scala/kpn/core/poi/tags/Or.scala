package kpn.core.poi.tags

import kpn.shared.data.Tags

case class Or(left: TagExpression, right: TagExpression) extends TagExpression {

  def evaluate(tags: Tags): Boolean = left.evaluate(tags) || right.evaluate(tags)

}
