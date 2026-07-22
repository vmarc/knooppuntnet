package kpn.core.poi.tags

import kpn.api.custom.Tag

trait TagExpression {

  def evaluate(tags: Seq[Tag]): Boolean

  def and(other: TagExpression): TagExpression = And(this, other)

  def or(other: TagExpression): TagExpression = Or(this, other)

  def tagKeys: Seq[String]
}
