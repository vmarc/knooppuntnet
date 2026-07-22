package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Fact extends EnumEntry

object Fact extends Enum[Fact] {

  val values: IndexedSeq[Fact] = findValues

  case object RouteUnsupportedRouteType extends Fact

  // errors
  case object RouteNotForward extends Fact

  case object RouteNotBackward extends Fact

  case object RouteUnusedSegments extends Fact

  case object RouteNodeMissingInWays extends Fact

  case object RouteRedundantNodes extends Fact

  case object RouteWithoutWays extends Fact

  case object RouteWithoutNodes extends Fact

  case object RouteFixmetodo extends Fact

  case object RouteNameMissing extends Fact

  case object RouteTagMissing extends Fact

  case object RouteTagInvalid extends Fact

  case object RouteUnexpectedNode extends Fact

  case object RouteUnexpectedRelation extends Fact

  case object NetworkExtraMemberNode extends Fact

  case object NetworkExtraMemberWay extends Fact

  case object NetworkExtraMemberRelation extends Fact

  case object NodeMemberMissing extends Fact

  case object IntegrityCheckFailed extends Fact

  case object UnexpectedIntegrityCheck extends Fact

  case object NameMissing extends Fact

  case object OrphanRoute extends Fact

  case object OrphanNode extends Fact

  case object RouteOverlappingWays extends Fact

  case object RouteSuspiciousWays extends Fact

  case object RouteAnalysisFailed extends Fact

  // informational
  case object RouteIncomplete extends Fact

  case object RouteInaccessible extends Fact

  case object RouteInvalidSortingOrder extends Fact

  case object RouteNodeNameMismatch extends Fact

  case object RouteNameDeprecatedNoteTag extends Fact

  case object RouteOneWay extends Fact

  case object RouteNotOneWay extends Fact

  case object RouteIncompleteOk extends Fact

  // other
  case object IntegrityCheck extends Fact

  case object Added extends Fact

  case object Deleted extends Fact

  case object LostHikingNodeTag extends Fact

  case object LostBicycleNodeTag extends Fact

  case object LostRouteTags extends Fact

  case object LostHorseNodeTag extends Fact

  case object LostMotorboatNodeTag extends Fact

  case object LostCanoeNodeTag extends Fact

  case object LostInlineSkateNodeTag extends Fact

  case object NodeInvalidSurveyDate extends Fact

  case object RouteInvalidSurveyDate extends Fact

  case object NetworkInvalidSurveyDate extends Fact
}
