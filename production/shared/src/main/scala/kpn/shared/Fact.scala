package kpn.shared

import scala.collection.mutable.ListBuffer

case class SimpleFact()

case class Fact(id: Int, name: String, nlName: String, level: FactLevel) {
  def isError: Boolean = level == FactLevel.ERROR

  def isInfo: Boolean = level == FactLevel.INFO

  def isOther: Boolean = level == FactLevel.OTHER

  override def toString: String = s"Fact.$name"

}

class FactFactory() {

  private val facts = ListBuffer[Fact]()

  def fact(id: Int, name: String, nlName: String, level: FactLevel): Fact = {
    val newFact = Fact(id, name, nlName, level)
    facts += newFact
    newFact
  }

  def all: Seq[Fact] = facts
}

object Fact {

  private val f = new FactFactory()

  // errors
  val RouteNotContinious: Fact = f.fact(101, "RouteNotContinious", "OnderBroken", FactLevel.ERROR)
  val RouteNotForward: Fact = f.fact(301, "RouteNotForward", "GeenHeenWeg", FactLevel.ERROR)
  val RouteNotBackward: Fact = f.fact(302, "RouteNotBackward", "GeenTerugWeg", FactLevel.ERROR)
  val RouteUnusedSegments: Fact = f.fact(102, "RouteUnusedSegments", "OngebruikteSegmenten", FactLevel.ERROR)

  val RouteNodeMissingInWays: Fact = f.fact(103, "RouteNodeMissingInWays", "OntbrekendKnoopunt", FactLevel.ERROR)
  val RouteRedundantNodes: Fact = f.fact(104, "RouteRedundantNodes", "BijkomendKnooppunt", FactLevel.ERROR)
  val RouteWithoutWays: Fact = f.fact(105, "RouteWithoutWays", "ZonderWegen", FactLevel.ERROR)

  val RouteFixmetodo: Fact = f.fact(106, "RouteFixmetodo", "Fixmetodo", FactLevel.ERROR)

  val RouteNameMissing: Fact = f.fact(109, "RouteNameMissing", "RouteZonderNaam", FactLevel.ERROR)

  // TODO not used anymore - remove when no longer in changes database
  val RouteEndNodeMismatch: Fact = f.fact(110, "RouteEndNodeMismatch", "EindKnooppuntFout", FactLevel.ERROR)

  // TODO not used anymore - remove when no longer in changes database
  val RouteStartNodeMismatch: Fact = f.fact(112, "RouteStartNodeMismatch", "StartKnooppuntFout", FactLevel.ERROR)

  val RouteTagMissing: Fact = f.fact(113, "RouteTagMissing", "OntbrekendeTag", FactLevel.ERROR)
  val RouteTagInvalid: Fact = f.fact(114, "RouteTagInvalid", "TagFout", FactLevel.ERROR)

  val RouteUnexpectedNode: Fact = f.fact(115, "RouteUnexpectedNode", "OnverwachteKnoopInRoute", FactLevel.ERROR)
  val RouteUnexpectedRelation: Fact = f.fact(116, "RouteUnexpectedRelation", "OnverwachteRelatieInRoute", FactLevel.ERROR)

  val NetworkExtraMemberNode: Fact = f.fact(117, "NetworkExtraMemberNode", "OnverwachteKnoop", FactLevel.ERROR)
  val NetworkExtraMemberWay: Fact = f.fact(118, "NetworkExtraMemberWay", "OnverwachteWeg", FactLevel.ERROR)
  val NetworkExtraMemberRelation: Fact = f.fact(119, "NetworkExtraMemberRelation", "OnverwachteRelatie", FactLevel.ERROR)
  val NodeMemberMissing: Fact = f.fact(120, "NodeMemberMissing", "OntbrekendKnooppuntLid", FactLevel.INFO)
  val IntegrityCheckFailed: Fact = f.fact(121, "IntegrityCheckFailed", "OnverwachtRouteAantal", FactLevel.ERROR)
  val NameMissing: Fact = f.fact(122, "NameMissing", "NetwerkZonderNaam", FactLevel.ERROR)
  val OrphanRoute: Fact = f.fact(123, "OrphanRoute", "RouteWees", FactLevel.OTHER)
  val OrphanNode: Fact = f.fact(124, "OrphanNode", "KnooppuntWees", FactLevel.OTHER)

  val RouteOverlappingWays: Fact = f.fact(125, "RouteOverlappingWays", "OverlappendeWegen", FactLevel.ERROR)
  val RouteSuspiciousWays: Fact = f.fact(126, "RouteSuspiciousWays", "RouteVerdachteWegen", FactLevel.ERROR)
  val RouteAnalysisFailed: Fact = f.fact(127, "RouteAnalysisFailed", "RouteAnalyseFout", FactLevel.ERROR)

  // informational
  val RouteIncomplete: Fact = f.fact(201, "RouteIncomplete", "OnvolledigeRoute", FactLevel.INFO)
  val RouteUnaccessible: Fact = f.fact(202, "RouteUnaccessible", "Ontoegankelijk", FactLevel.INFO)
  val RouteInvalidSortingOrder: Fact = f.fact(203, "RouteInvalidSortingOrder", "SorteerFout", FactLevel.INFO)

  // TODO not used anymore - remove when no longer in changes database
  val RouteReversed: Fact = f.fact(204, "RouteReversed", "OmgekeerdeVolgorde", FactLevel.INFO)

  val RouteNodeNameMismatch: Fact = f.fact(205, "RouteNodeNameMismatch", "OnverwachteNaam", FactLevel.INFO)
  val RouteOneWay: Fact = f.fact(206, "RouteOneWay", "RouteEnkeleRichting", FactLevel.INFO)
  val RouteNotOneWay: Fact = f.fact(207, "RouteNotOneWay", "RouteNietEnkeleRichting", FactLevel.INFO)
  val RouteIncompleteOk: Fact = f.fact(208, "RouteIncompleteOk", "OnvolledigeRouteOk", FactLevel.INFO)

  // TODO remove after migration and when no longer in changes database
  val NodeNetworkTypeNotTagged: Fact = f.fact(209, "NodeNetworkTypeNotTagged", "GeenKnooppuntNetwerkTypeTag", FactLevel.INFO)
  val RouteNetworkTypeNotTagged: Fact = f.fact(210, "RouteNetworkTypeNotTagged", "GeenRouteNetwerkTypeTag", FactLevel.INFO)
  val NetworkTypeNotTagged: Fact = f.fact(211, "NetworkTypeNotTagged", "GeenNetwerkTypeTag", FactLevel.INFO)

  // other
  val RouteBroken: Fact = f.fact(303, "RouteBroken", "RouteFout", FactLevel.OTHER)

  val IntegrityCheck: Fact = f.fact(304, "IntegrityCheck", "RouteAantal", FactLevel.OTHER)

  val IgnoreForeignCountry: Fact = f.fact(305, "IgnoreForeignCountry", "Buitenland", FactLevel.OTHER)
  val IgnoreNotNodeNetwork: Fact = f.fact(306, "IgnoreNotNodeNetwork", "GeenKnooppuntNetwerk", FactLevel.OTHER)
  val IgnoreNetworkTaggedAsRoute: Fact = f.fact(307, "IgnoreNetworkTaggedAsRoute", "GeenRoute", FactLevel.OTHER)
  val IgnoreNoNetworkNodes: Fact = f.fact(308, "IgnoreNoNetworkNodes", "ZonderKnooppunten", FactLevel.OTHER)
  val IgnoreNetworkCollection: Fact = f.fact(309, "IgnoreNetworkCollection", "NetwerkVerzameling", FactLevel.OTHER)
  val IgnoreTagBased: Fact = f.fact(310, "IgnoreTagBased", "UitgeslotenOpBasisVanLabels", FactLevel.OTHER)
  val IgnoreUnsupportedSubset: Fact = f.fact(311, "IgnoreUnsupportedSubset", "UitgeslotenSubset", FactLevel.OTHER)

  val ignoreFacts: Seq[Fact] = Seq(
    IgnoreForeignCountry,
    IgnoreNotNodeNetwork,
    IgnoreNetworkTaggedAsRoute,
    IgnoreNoNetworkNodes,
    IgnoreNetworkCollection,
    IgnoreTagBased,
    IgnoreUnsupportedSubset
  )

  val Added: Fact = f.fact(401, "Added", "Toegevoegd", FactLevel.OTHER)
  val Deleted: Fact = f.fact(402, "Deleted", "Verwijderd", FactLevel.OTHER)
  val LostHikingNodeTag: Fact = f.fact(403, "LostHikingNodeTag", "GeenWandelKnooppuntMeer", FactLevel.OTHER)
  val LostBicycleNodeTag: Fact = f.fact(404, "LostBicycleNodeTag", "GeenFietsKnooppuntMeer", FactLevel.OTHER)
  val BecomeOrphan: Fact = f.fact(405, "BecomeOrphan", "WeesGeworden", FactLevel.OTHER)
  val WasOrphan: Fact = f.fact(406, "WasOrphan", "WasWees", FactLevel.OTHER)
  val LostRouteTags: Fact = f.fact(409, "LostRouteTags", "GeenRouteMeer", FactLevel.OTHER)

  val LostHorseNodeTag: Fact = f.fact(410, "LostHorseNodeTag", "GeenRuiterKnooppuntMeer", FactLevel.OTHER)
  val LostMotorboatNodeTag: Fact = f.fact(411, "LostMotorboatNodeTag", "GeenMotorbootKnooppuntMeer", FactLevel.OTHER)
  val LostCanoeNodeTag: Fact = f.fact(412, "LostCanoeNodeTag", "GeenKanoKnooppuntMeer", FactLevel.OTHER)
  val LostInlineSkateNodeTag: Fact = f.fact(413, "LostInlineSkateNodeTag", "GeenInlineSkateKnooppuntMeer", FactLevel.OTHER)


  val all: Seq[Fact] = f.all

  val routeDetailFacts: Seq[Fact] = all.filterNot(f => f == RouteBroken || f == RouteNotContinious)

  def withName(factName: String): Option[Fact] = {
    val currentFactName = factName match {
      case "NodeNetwerkTypeNotTagged" => "NodeNetworkTypeNotTagged"
      case "RouteNetwerkTypeNotTagged" => "RouteNetworkTypeNotTagged"
      case _ => factName
    }
    all.find(fact => fact.name == currentFactName)
  }

  val errorFacts: Seq[Fact] = all.filter(_.isError).
    filterNot(_ == RouteNotForward).
    filterNot(_ == RouteNotBackward)

  val infoFacts: Seq[Fact] = all.filter(_.isInfo)

  val reportedFacts: Seq[Fact] = errorFacts ++ infoFacts

  val reportedFactNames: Seq[String] = reportedFacts.map(_.name)

  /*
    Determines whether a given fact should be reported during route analysis, depending on
    other facts that have already been discovered. For some facts it does not make sense to
    report it, if another fact has already been discovered.
   */
  def canReport(fact: Fact, otherFacts: Seq[Fact]): Boolean = {

    def reportIfNotIn(mutuallyExclusiveFacts: Fact*): Boolean = {
      !otherFacts.exists(mutuallyExclusiveFacts.contains)
    }

    fact match {
      case RouteUnusedSegments => reportIfNotIn(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteInvalidSortingOrder => reportIfNotIn(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)
      case RouteNotContinious => reportIfNotIn(RouteNodeMissingInWays, RouteWithoutWays, RouteIncomplete)
      case RouteNotForward => reportIfNotIn(RouteWithoutWays)
      case RouteNotBackward => reportIfNotIn(RouteWithoutWays)
      case RouteNodeMissingInWays => reportIfNotIn(RouteWithoutWays, RouteIncomplete)
      case RouteRedundantNodes => reportIfNotIn(RouteWithoutWays, RouteIncomplete)
      case _ => true
    }
  }
}
