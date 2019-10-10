package kpn.core.poi.tags

import kpn.shared.data.Tags

trait TagExpression {

  def evaluate(tags: Tags): Boolean

  def and(other: TagExpression): TagExpression = And(this, other)

  def tagKeys: Seq[String]
}
