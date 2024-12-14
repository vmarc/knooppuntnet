package kpn.core.analysis

import kpn.api.common.Fact
import kpn.api.common.FactLevel
import Fact.Added
import Fact.Deleted
import Fact.IntegrityCheck
import Fact.IntegrityCheckFailed
import Fact.LostBicycleNodeTag
import Fact.LostCanoeNodeTag
import Fact.LostHikingNodeTag
import Fact.LostHorseNodeTag
import Fact.LostInlineSkateNodeTag
import Fact.LostMotorboatNodeTag
import Fact.LostRouteTags
import Fact.NameMissing
import Fact.NetworkExtraMemberNode
import Fact.NetworkExtraMemberRelation
import Fact.NetworkExtraMemberWay
import Fact.NetworkInvalidSurveyDate
import Fact.NodeInvalidSurveyDate
import Fact.NodeMemberMissing
import Fact.OrphanNode
import Fact.OrphanRoute
import Fact.RouteAnalysisFailed
import Fact.RouteBroken
import Fact.RouteFixmetodo
import Fact.RouteInaccessible
import Fact.RouteIncomplete
import Fact.RouteIncompleteOk
import Fact.RouteInvalidSortingOrder
import Fact.RouteInvalidSurveyDate
import Fact.RouteNameDeprecatedNoteTag
import Fact.RouteNameMissing
import Fact.RouteNodeMissingInWays
import Fact.RouteNodeNameMismatch
import Fact.RouteNotBackward
import Fact.RouteNotContinious
import Fact.RouteNotForward
import Fact.RouteNotOneWay
import Fact.RouteOneWay
import Fact.RouteOverlappingWays
import Fact.RouteRedundantNodes
import Fact.RouteSuspiciousWays
import Fact.RouteTagInvalid
import Fact.RouteTagMissing
import Fact.RouteUnexpectedNode
import Fact.RouteUnexpectedRelation
import Fact.RouteUnsupportedNetworkType
import Fact.RouteUnusedSegments
import Fact.RouteWithoutNodes
import Fact.RouteWithoutWays
import Fact.UnexpectedIntegrityCheck

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
      case RouteUnsupportedNetworkType => FactLevel.ERROR
      case RouteNotContinious => FactLevel.ERROR
      case RouteNotForward => FactLevel.ERROR
      case RouteNotBackward => FactLevel.ERROR
      case RouteUnusedSegments => FactLevel.ERROR

      case RouteNodeMissingInWays => FactLevel.ERROR
      case RouteRedundantNodes => FactLevel.ERROR
      case RouteWithoutWays => FactLevel.ERROR
      case RouteWithoutNodes => FactLevel.ERROR

      case RouteFixmetodo => FactLevel.ERROR

      case RouteNameMissing => FactLevel.ERROR

      case RouteTagMissing => FactLevel.ERROR
      case RouteTagInvalid => FactLevel.ERROR

      case RouteUnexpectedNode => FactLevel.ERROR
      case RouteUnexpectedRelation => FactLevel.ERROR

      case NetworkExtraMemberNode => FactLevel.ERROR
      case NetworkExtraMemberWay => FactLevel.ERROR
      case NetworkExtraMemberRelation => FactLevel.ERROR
      case NodeMemberMissing => FactLevel.ERROR
      case IntegrityCheckFailed => FactLevel.ERROR
      case UnexpectedIntegrityCheck => FactLevel.ERROR
      case NameMissing => FactLevel.ERROR
      case OrphanRoute => FactLevel.OTHER
      case OrphanNode => FactLevel.OTHER

      case RouteOverlappingWays => FactLevel.ERROR
      case RouteSuspiciousWays => FactLevel.ERROR
      case RouteAnalysisFailed => FactLevel.ERROR

      // informational
      case RouteIncomplete => FactLevel.INFO
      case RouteInaccessible => FactLevel.INFO
      case RouteInvalidSortingOrder => FactLevel.INFO

      case RouteNodeNameMismatch => FactLevel.INFO
      case RouteNameDeprecatedNoteTag => FactLevel.INFO
      case RouteOneWay => FactLevel.INFO
      case RouteNotOneWay => FactLevel.INFO
      case RouteIncompleteOk => FactLevel.INFO

      // other
      case RouteBroken => FactLevel.OTHER

      case IntegrityCheck => FactLevel.OTHER

      case Added => FactLevel.OTHER
      case Deleted => FactLevel.OTHER
      case LostHikingNodeTag => FactLevel.OTHER
      case LostBicycleNodeTag => FactLevel.OTHER
      case LostRouteTags => FactLevel.OTHER

      case LostHorseNodeTag => FactLevel.OTHER
      case LostMotorboatNodeTag => FactLevel.OTHER
      case LostCanoeNodeTag => FactLevel.OTHER
      case LostInlineSkateNodeTag => FactLevel.OTHER

      case NodeInvalidSurveyDate => FactLevel.ERROR
      case RouteInvalidSurveyDate => FactLevel.ERROR
      case NetworkInvalidSurveyDate => FactLevel.ERROR
    }
  }

  val locationFacts: Seq[Fact] = Seq(
    RouteNotContinious,
    RouteNotForward,
    RouteNotBackward,
    RouteUnusedSegments,
    RouteNodeMissingInWays,
    RouteRedundantNodes,
    RouteFixmetodo,
    RouteWithoutWays,
    RouteNameMissing,
    RouteTagMissing,
    RouteTagInvalid,
    RouteUnexpectedNode,
    RouteUnexpectedRelation,
    RouteSuspiciousWays,
    RouteAnalysisFailed,
    RouteIncomplete,
    RouteInaccessible,
    RouteInvalidSortingOrder, //
    RouteNodeNameMismatch,
    RouteNameDeprecatedNoteTag,
    RouteOneWay,
    RouteNotOneWay,
    RouteIncompleteOk,
    RouteBroken,
    Added,
    Deleted,
    LostHikingNodeTag,
    LostBicycleNodeTag,
    LostRouteTags,
    LostHorseNodeTag,
    LostMotorboatNodeTag,
    LostCanoeNodeTag,
    LostInlineSkateNodeTag,
    UnexpectedIntegrityCheck,
    NodeInvalidSurveyDate,
    RouteInvalidSurveyDate,
  )

  val networkFactsWithElementIds: Seq[Fact] = Seq(Fact.NetworkExtraMemberNode, Fact.NetworkExtraMemberWay, Fact.NetworkExtraMemberRelation)
  val networkFactsWithRefs: Seq[Fact] = Seq(Fact.NodeMemberMissing, Fact.UnexpectedIntegrityCheck)

  val reportedFacts: Seq[Fact] = {
    val errorFacts: Seq[Fact] = Fact.values.filter(Facts.isError).
      filterNot(_ == RouteNotForward).
      filterNot(_ == RouteNotBackward)
    val infoFacts: Seq[Fact] = Fact.values.filter(Facts.isInfo)
    errorFacts ++ infoFacts
  }
}
