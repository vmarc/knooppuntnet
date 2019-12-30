package kpn.core.poi.tags

import kpn.api.custom.Tags

trait TagExpression {

  def evaluate(tags: Tags): Boolean

  def and(other: TagExpression): TagExpression = And(this, other)

  def or(other: TagExpression): TagExpression = Or(this, other)

  def tagKeys: Seq[String]
}
