package kpn.core.analysis

import kpn.api.common.Fact
import kpn.api.common.FactLevel

object Facts {

  def isError(fact: Fact): Boolean = {
    level(fact) == FactLevel.ERROR
  }

  def isInfo(fact: Fact): Boolean = {
    level(fact) == FactLevel.INFO
  }

  def isOther(fact: Fact): Boolean = {
    level(fact) == FactLevel.OTHER
  }

  def level(fact: Fact): FactLevel = {
    fact match {
      case Fact.RouteUnsupportedRouteType => FactLevel.ERROR
      case Fact.RouteNotContinious => FactLevel.ERROR
      case Fact.RouteNotForward => FactLevel.ERROR
      case Fact.RouteNotBackward => FactLevel.ERROR
      case Fact.RouteUnusedSegments => FactLevel.ERROR

      case Fact.RouteNodeMissingInWays => FactLevel.ERROR
      case Fact.RouteRedundantNodes => FactLevel.ERROR
      case Fact.RouteWithoutWays => FactLevel.ERROR
      case Fact.RouteWithoutNodes => FactLevel.ERROR

      case Fact.RouteFixmetodo => FactLevel.ERROR

      case Fact.RouteNameMissing => FactLevel.ERROR

      case Fact.RouteTagMissing => FactLevel.ERROR
      case Fact.RouteTagInvalid => FactLevel.ERROR

      case Fact.RouteUnexpectedNode => FactLevel.ERROR
      case Fact.RouteUnexpectedRelation => FactLevel.ERROR

      case Fact.NetworkExtraMemberNode => FactLevel.ERROR
      case Fact.NetworkExtraMemberWay => FactLevel.ERROR
      case Fact.NetworkExtraMemberRelation => FactLevel.ERROR
      case Fact.NodeMemberMissing => FactLevel.ERROR
      case Fact.IntegrityCheckFailed => FactLevel.ERROR
      case Fact.UnexpectedIntegrityCheck => FactLevel.ERROR
      case Fact.NameMissing => FactLevel.ERROR
      case Fact.OrphanRoute => FactLevel.OTHER
      case Fact.OrphanNode => FactLevel.OTHER

      case Fact.RouteOverlappingWays => FactLevel.ERROR
      case Fact.RouteSuspiciousWays => FactLevel.ERROR
      case Fact.RouteAnalysisFailed => FactLevel.ERROR

      // informational
      case Fact.RouteIncomplete => FactLevel.INFO
      case Fact.RouteInaccessible => FactLevel.INFO
      case Fact.RouteInvalidSortingOrder => FactLevel.INFO

      case Fact.RouteNodeNameMismatch => FactLevel.INFO
      case Fact.RouteNameDeprecatedNoteTag => FactLevel.INFO
      case Fact.RouteOneWay => FactLevel.INFO
      case Fact.RouteNotOneWay => FactLevel.INFO
      case Fact.RouteIncompleteOk => FactLevel.INFO

      // other
      case Fact.RouteBroken => FactLevel.OTHER

      case Fact.IntegrityCheck => FactLevel.OTHER

      case Fact.Added => FactLevel.OTHER
      case Fact.Deleted => FactLevel.OTHER
      case Fact.LostHikingNodeTag => FactLevel.OTHER
      case Fact.LostBicycleNodeTag => FactLevel.OTHER
      case Fact.LostRouteTags => FactLevel.OTHER

      case Fact.LostHorseNodeTag => FactLevel.OTHER
      case Fact.LostMotorboatNodeTag => FactLevel.OTHER
      case Fact.LostCanoeNodeTag => FactLevel.OTHER
      case Fact.LostInlineSkateNodeTag => FactLevel.OTHER

      case Fact.NodeInvalidSurveyDate => FactLevel.ERROR
      case Fact.RouteInvalidSurveyDate => FactLevel.ERROR
      case Fact.NetworkInvalidSurveyDate => FactLevel.ERROR
    }
  }

  val locationFacts: Seq[Fact] = Seq(
    Fact.RouteNotContinious,
    Fact.RouteNotForward,
    Fact.RouteNotBackward,
    Fact.RouteUnusedSegments,
    Fact.RouteNodeMissingInWays,
    Fact.RouteRedundantNodes,
    Fact.RouteFixmetodo,
    Fact.RouteWithoutWays,
    Fact.RouteNameMissing,
    Fact.RouteTagMissing,
    Fact.RouteTagInvalid,
    Fact.RouteUnexpectedNode,
    Fact.RouteUnexpectedRelation,
    Fact.RouteSuspiciousWays,
    Fact.RouteAnalysisFailed,
    Fact.RouteIncomplete,
    Fact.RouteInaccessible,
    Fact.RouteInvalidSortingOrder, //
    Fact.RouteNodeNameMismatch,
    Fact.RouteNameDeprecatedNoteTag,
    Fact.RouteOneWay,
    Fact.RouteNotOneWay,
    Fact.RouteIncompleteOk,
    Fact.RouteBroken,
    Fact.Added,
    Fact.Deleted,
    Fact.LostHikingNodeTag,
    Fact.LostBicycleNodeTag,
    Fact.LostRouteTags,
    Fact.LostHorseNodeTag,
    Fact.LostMotorboatNodeTag,
    Fact.LostCanoeNodeTag,
    Fact.LostInlineSkateNodeTag,
    Fact.UnexpectedIntegrityCheck,
    Fact.NodeInvalidSurveyDate,
    Fact.RouteInvalidSurveyDate,
  )

  val networkFactsWithElementIds: Seq[Fact] = Seq(Fact.NetworkExtraMemberNode, Fact.NetworkExtraMemberWay, Fact.NetworkExtraMemberRelation)
  val networkFactsWithRefs: Seq[Fact] = Seq(Fact.NodeMemberMissing, Fact.UnexpectedIntegrityCheck)

  val reportedFacts: Seq[Fact] = {
    val errorFacts: Seq[Fact] = Fact.values.filter(Facts.isError).
      filterNot(_ == Fact.RouteNotForward).
      filterNot(_ == Fact.RouteNotBackward)
    val infoFacts: Seq[Fact] = Fact.values.filter(Facts.isInfo)
    errorFacts ++ infoFacts
  }
}
