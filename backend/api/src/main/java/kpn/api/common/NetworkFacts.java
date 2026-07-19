package kpn.api.common;

import kpn.api.common.NetworkExtraMemberNode;
import kpn.api.common.NetworkExtraMemberRelation;
import kpn.api.common.NetworkExtraMemberWay;
import kpn.api.common.NetworkIntegrityCheck;
import kpn.api.common.NetworkIntegrityCheckFailed;
import kpn.api.common.NetworkNameMissing;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkFacts(
  ImmutableList<NetworkExtraMemberNode> networkExtraMemberNode,
  ImmutableList<NetworkExtraMemberWay> networkExtraMemberWay,
  ImmutableList<NetworkExtraMemberRelation> networkExtraMemberRelation,
  Optional<NetworkIntegrityCheck> integrityCheck,
  Optional<NetworkIntegrityCheckFailed> integrityCheckFailed,
  Optional<NetworkNameMissing> nameMissing
) {
}

/*
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

*/
