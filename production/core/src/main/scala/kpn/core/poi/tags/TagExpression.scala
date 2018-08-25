package kpn.core.poi.tags

import kpn.shared.data.Tags

trait TagExpression {

  def evaluate(tags: Tags): Boolean

  def and(other: TagExpression): TagExpression = And(this, other)

  def or(other: TagExpression): TagExpression = Or(this, other)

  def not: TagExpression = Not(this)
}
