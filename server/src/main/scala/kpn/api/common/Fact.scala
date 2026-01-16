package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Fact extends EnumEntry

object Fact extends Enum[Fact] {

  val values: IndexedSeq[Fact] = findValues

  final case object RouteUnsupportedRouteType extends Fact

  // errors
  final case object RouteNotForward extends Fact

  final case object RouteNotBackward extends Fact

  final case object RouteUnusedSegments extends Fact

  final case object RouteNodeMissingInWays extends Fact

  final case object RouteRedundantNodes extends Fact

  final case object RouteWithoutWays extends Fact

  final case object RouteWithoutNodes extends Fact

  final case object RouteFixmetodo extends Fact

  final case object RouteNameMissing extends Fact

  final case object RouteTagMissing extends Fact

  final case object RouteTagInvalid extends Fact

  final case object RouteUnexpectedNode extends Fact

  final case object RouteUnexpectedRelation extends Fact

  final case object NetworkExtraMemberNode extends Fact

  final case object NetworkExtraMemberWay extends Fact

  final case object NetworkExtraMemberRelation extends Fact

  final case object NodeMemberMissing extends Fact

  final case object IntegrityCheckFailed extends Fact

  final case object UnexpectedIntegrityCheck extends Fact

  final case object NameMissing extends Fact

  final case object OrphanRoute extends Fact

  final case object OrphanNode extends Fact

  final case object RouteOverlappingWays extends Fact

  final case object RouteSuspiciousWays extends Fact

  final case object RouteAnalysisFailed extends Fact

  // informational
  final case object RouteIncomplete extends Fact

  final case object RouteInaccessible extends Fact

  final case object RouteInvalidSortingOrder extends Fact

  final case object RouteNodeNameMismatch extends Fact

  final case object RouteNameDeprecatedNoteTag extends Fact

  final case object RouteOneWay extends Fact

  final case object RouteNotOneWay extends Fact

  final case object RouteIncompleteOk extends Fact

  // other
  final case object RouteBroken extends Fact

  final case object IntegrityCheck extends Fact

  final case object Added extends Fact

  final case object Deleted extends Fact

  final case object LostHikingNodeTag extends Fact

  final case object LostBicycleNodeTag extends Fact

  final case object LostRouteTags extends Fact

  final case object LostHorseNodeTag extends Fact

  final case object LostMotorboatNodeTag extends Fact

  final case object LostCanoeNodeTag extends Fact

  final case object LostInlineSkateNodeTag extends Fact

  final case object NodeInvalidSurveyDate extends Fact

  final case object RouteInvalidSurveyDate extends Fact

  final case object NetworkInvalidSurveyDate extends Fact
}
