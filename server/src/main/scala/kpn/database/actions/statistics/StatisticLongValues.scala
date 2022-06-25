package kpn.database.actions.statistics

import kpn.api.custom.Subset

case class StatisticLongValues(
  _id: String,
  values: Seq[StatisticLongValue]
) {

  def total(): Long = {
    values.map(_.value).sum
  }

  def subsetValue(subset: Subset): Long = {
    values.filter(_.isSubset(subset)).map(_.value).sum
  }

}
