package kpn.api.common

case class NetworkFacts(
  networkExtraMemberNode: Option[Seq[NetworkExtraMemberNode]] = None,
  networkExtraMemberWay: Option[Seq[NetworkExtraMemberWay]] = None,
  networkExtraMemberRelation: Option[Seq[NetworkExtraMemberRelation]] = None,
  integrityCheck: Option[NetworkIntegrityCheck] = None,
  integrityCheckFailed: Option[NetworkIntegrityCheckFailed] = None,
  nameMissing: Option[NetworkNameMissing] = None
) {

  def nonEmpty: Boolean = networkExtraMemberNode.nonEmpty ||
    networkExtraMemberWay.nonEmpty ||
    networkExtraMemberRelation.nonEmpty ||
    integrityCheck.isDefined ||
    integrityCheckFailed.isDefined ||
    nameMissing.isDefined

  def factCount: Int = {
    val networkExtraMemberNodeCount = networkExtraMemberNode.size

    val networkExtraMemberWayCount = networkExtraMemberWay.size

    val networkExtraMemberRelationCount = networkExtraMemberRelation.size

    val integrityCheckFailedCount = integrityCheckFailed match {
      case Some(fact) => fact.checks.count(_.failed)
      case _ => 0
    }

    val nameMissingCount = if (nameMissing.isDefined) 1 else 0

    networkExtraMemberNodeCount + networkExtraMemberWayCount + networkExtraMemberRelationCount + integrityCheckFailedCount + nameMissingCount
  }
}
