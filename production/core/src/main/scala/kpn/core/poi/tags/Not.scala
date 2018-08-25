package kpn.core.poi.tags

import kpn.shared.data.Tags

case class Not(operand: TagExpression) extends TagExpression {

  def evaluate(tags: Tags): Boolean = !operand.evaluate(tags)

}
