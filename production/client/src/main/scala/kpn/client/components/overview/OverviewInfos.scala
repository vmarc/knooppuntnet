// Migrated to Angular: statistic-configurations.component.ts
package kpn.client.components.overview

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsEN
import kpn.client.common.Nls.nlsNL
import kpn.client.components.common.UiMarked
import kpn.shared.Fact
import kpn.shared.Fact.f
import kpn.shared.FactLevel
import kpn.shared.Subset
import kpn.shared.statistics.Statistic
import kpn.shared.statistics.Statistics

class OverviewInfos(statistics: Statistics)(implicit context: Context) {

  val lengthInfo = UiOverviewInfo(
    nls("Length", "Lengte") + " (km)",
    counts(statistics.get("km")),
    <.div(
      nls(
        "Total length in kilometers.",
        "Totale lengte in kilometer."
      )
    )
  )

  val networkCountInfo = UiOverviewInfo(
    nls("NetworkCount", "Aantal netwerken"),
    UiOverviewCounts(
      <.div(statistics.get("NetworkCount").total),
      context.gotoSubsetNetworks(Subset.nlBicycle, Some(statistics.get("NetworkCount").nl.rcn)),
      context.gotoSubsetNetworks(Subset.nlHiking, Some(statistics.get("NetworkCount").nl.rwn)),
      context.gotoSubsetNetworks(Subset.nlHorseRiding, Some(statistics.get("NetworkCount").nl.rhn)),
      context.gotoSubsetNetworks(Subset.nlMotorboat, Some(statistics.get("NetworkCount").nl.rmn)),
      context.gotoSubsetNetworks(Subset.nlCanoe, Some(statistics.get("NetworkCount").nl.rpn)),
      context.gotoSubsetNetworks(Subset.nlInlineSkates, Some(statistics.get("NetworkCount").nl.rin)),
      context.gotoSubsetNetworks(Subset.beBicycle, Some(statistics.get("NetworkCount").be.rcn)),
      context.gotoSubsetNetworks(Subset.beHiking, Some(statistics.get("NetworkCount").be.rwn)),
      context.gotoSubsetNetworks(Subset.beHorseRiding, Some(statistics.get("NetworkCount").be.rhn)),
      context.gotoSubsetNetworks(Subset.deBicycle, Some(statistics.get("NetworkCount").de.rcn)),
      context.gotoSubsetNetworks(Subset.deHiking, Some(statistics.get("NetworkCount").de.rwn)),
      context.gotoSubsetNetworks(Subset.deHorseRiding, Some(statistics.get("NetworkCount").de.rhn))
    ),
    <.div(
      nls(
        "Number of networks.",
        "Aantal netwerken."
      )
    )
  )

  val nodeCountInfo = UiOverviewInfo(
    nls("Node count", "Aantal nodes"),
    counts(statistics.get("NodeCount")),
    <.div(
      nls(
        "Number of network nodes.",
        "Aantal knooppunten."
      )
    )
  )

  val routeCountInfo = UiOverviewInfo(
    nls("Route count", "Aantal routes"),
    counts(statistics.get("RouteCount")),
    <.div(
      nls(
        "Number of routes.",
        "Aantal routes (verbindingen tussen knooppunten)."
      )
    )
  )

  val routeNotContiniousNetworkCountInfo = UiOverviewInfo(
    "RouteNotContiniousNetworkCount",
    counts(statistics.get("RouteNotContiniousNetworkCount")),
    <.div(
      nls(
        "Number of networks with broken routes.",
        "Aantal netwerken met onderbroken routes."
      )
    )
  )

  val routeNotContiniousInfo: UiOverviewInfo = factDetailCounts(Fact.RouteNotContinious) {
    <.div(
      nls(
        "Number of broken routes.",
        "Aantal onderbroken routes."
      )
    )
  }

  val routeNotContiniousPercentageInfo = UiOverviewInfo(
    nls("RouteNotContiniousPercentage", "OnderbrokenRoutePercentage"),
    counts(statistics.get("RouteNotContiniousPercentage") /*, "important-fact"*/),
    <.div(
      nls(
        "Percentage of broken routes.",
        "Percentage onderbroken routes."
      )
    )
  )

  val routeBrokenNetworkCountInfo = UiOverviewInfo(
    nls("RouteBrokenNetworkCount", "TeOnderzoekenRouteNetwerkAantal"),
    counts(statistics.get("RouteBrokenNetworkCount")),
    <.div(
      nls(
        "Number of networks containing routes with issues.",
        "Aantal netwerken met routes met opmerkingen."
      )
    )
  )

  val routeBrokenCountInfo = UiOverviewInfo(
    nls("RouteBrokenCount", "TeOnderzoekenRouteAantal"),
    counts(statistics.get("RouteBrokenCount")),
    <.div(
      nls(
        "Number of routes with issues.",
        "Aantal routes met opmerkingen."
      )
    )
  )

  val routeBrokenPercentageInfo = UiOverviewInfo(
    nls("RouteBrokenPercentage", "TeOnderzoekenRoutePercentage"),
    counts(statistics.get("RouteBrokenPercentage")),
    <.div(
      nls(
        "Percentage of routes with issues.",
        "Percentage routes met opmerkingen."
      )
    )
  )

  val routeIncompleteInfo: UiOverviewInfo = factDetailCounts(Fact.RouteIncomplete) {
    UiMarked(
      if (nlsEN) {
        """|Number of routes that are marked as having an incomplete definition.
           |
           |A route definition is explicitely marked incomplete by adding a tag _"fixme"_ with
           |value _"incomplete"_ in the route relation.
        """.stripMargin
      } else {
        """|Aantal onvolledige routes.
           |
           |Deze routes zijn uitdrukkelijk gemarkeerd als onvolledig door het toevoegen van
           |een tag met sleutel _"fixme"_ en waarde _"incomplete"_ in de route relatie.
        """.stripMargin
      }
    )
  }

  val routeIncompleteOkInfo: UiOverviewInfo = factDetailCounts(Fact.RouteIncompleteOk) {
    UiMarked(
      if (nlsEN) {
        """|Number of routes that are marked as having an incomplete definition, but
           |that look ok after analysis.
           |
           |A route definition is explicitely marked incomplete by adding a tag _"fixme"_ with
           |value _"incomplete"_ in the route relation.
        """.stripMargin
      } else {
        """|Aantal routes die gemarkeerd zijn als onvolledig, maar die na analyse ok lijken te zijn.
           |
           |Deze routes zijn uitdrukkelijk gemarkeerd als onvolledig door het toevoegen van
           |een tag met sleutel _"fixme"_ en waarde _"incomplete"_ in de route relatie.
        """.stripMargin
      }
    )
  }

  val routeFixmetodoInfo: UiOverviewInfo = factDetailCounts(Fact.RouteFixmetodo) {
    UiMarked(
      if (nlsEN) {
        """Number of routes that are marked with _"fixmetodo"_."""
      }
      else {
        """Aantal routes gemarkeerd met _"fixmetodo"_."""
      }
    )
  }

  val orphanNodeCountInfo = UiOverviewInfo(
    nls("Orphan nodes", "Knooppunt wezen"),
    UiOverviewCounts(
      <.div(statistics.get("OrphanNodeCount").total),
      context.gotoSubsetOrphanNodes(Subset.nlBicycle, statistics.get("OrphanNodeCount").nl.rcn),
      context.gotoSubsetOrphanNodes(Subset.nlHiking, statistics.get("OrphanNodeCount").nl.rwn),
      context.gotoSubsetOrphanNodes(Subset.nlHorseRiding, statistics.get("OrphanNodeCount").nl.rhn),
      context.gotoSubsetOrphanNodes(Subset.nlMotorboat, statistics.get("OrphanNodeCount").nl.rmn),
      context.gotoSubsetOrphanNodes(Subset.nlCanoe, statistics.get("OrphanNodeCount").nl.rpn),
      context.gotoSubsetOrphanNodes(Subset.nlInlineSkates, statistics.get("OrphanNodeCount").nl.rin),
      context.gotoSubsetOrphanNodes(Subset.beBicycle, statistics.get("OrphanNodeCount").be.rcn),
      context.gotoSubsetOrphanNodes(Subset.beHiking, statistics.get("OrphanNodeCount").be.rwn),
      context.gotoSubsetOrphanNodes(Subset.beHorseRiding, statistics.get("OrphanNodeCount").be.rhn),
      context.gotoSubsetOrphanNodes(Subset.deBicycle, statistics.get("OrphanNodeCount").de.rcn),
      context.gotoSubsetOrphanNodes(Subset.deHiking, statistics.get("OrphanNodeCount").de.rwn),
      context.gotoSubsetOrphanNodes(Subset.deHorseRiding, statistics.get("OrphanNodeCount").de.rhn)
    ),
    if (nlsNL) {
      <.div(
        <.p(
          "Aantal op zichzelf staande knooppunten (niet teruggevonden in een netwerk)."
        ),
        <.p(
          "Deze ",
          context.gotoGlossaryEntry("node", "knooppunten"),
          " werden niet teruggevonden als deel van een geldige ",
          context.gotoGlossaryEntry("network-relation", "netwerk relatie"),
          ", of als deel van een geldige ",
          context.gotoGlossaryEntry("route-relation", "route relatie"),
          " (dit kan een route relatie zijn die deel uitmaakt van een network relatie, maar ook een op zichzelf ",
          "staande route relatie die geen deel uitmaakt van een network relatie)."
        )
      )
    } else {
      <.div(
        <.p("Number of network nodes that do not belong to a network."),
        <.p(
          "The ",
          context.gotoGlossaryEntry("node", "node"),
          " was not added as a member to a valid ",
          context.gotoGlossaryEntry("network-relation", "netwerk relation"),
          ", and also not added as a member to valid ",
          context.gotoGlossaryEntry("route-relation", "route relation"),
          " (that itself was added as a member to a valid network relation or is an orphan route)."
        )
      )
    }
  )

  val orphanRouteCountInfo = UiOverviewInfo(
    nls("Orphan routes", "Route wezen"),
    UiOverviewCounts(
      <.div(statistics.get("OrphanRouteCount").total),
      context.gotoSubsetOrphanRoutes(Subset.nlBicycle, statistics.get("OrphanRouteCount").nl.rcn),
      context.gotoSubsetOrphanRoutes(Subset.nlHiking, statistics.get("OrphanRouteCount").nl.rwn),
      context.gotoSubsetOrphanRoutes(Subset.nlHorseRiding, statistics.get("OrphanRouteCount").nl.rhn),
      context.gotoSubsetOrphanRoutes(Subset.nlMotorboat, statistics.get("OrphanRouteCount").nl.rmn),
      context.gotoSubsetOrphanRoutes(Subset.nlCanoe, statistics.get("OrphanRouteCount").nl.rpn),
      context.gotoSubsetOrphanRoutes(Subset.nlInlineSkates, statistics.get("OrphanRouteCount").nl.rin),
      context.gotoSubsetOrphanRoutes(Subset.beBicycle, statistics.get("OrphanRouteCount").be.rcn),
      context.gotoSubsetOrphanRoutes(Subset.beHiking, statistics.get("OrphanRouteCount").be.rwn),
      context.gotoSubsetOrphanRoutes(Subset.beHorseRiding, statistics.get("OrphanRouteCount").be.rhn),
      context.gotoSubsetOrphanRoutes(Subset.deBicycle, statistics.get("OrphanRouteCount").de.rcn),
      context.gotoSubsetOrphanRoutes(Subset.deHiking, statistics.get("OrphanRouteCount").de.rwn),
      context.gotoSubsetOrphanRoutes(Subset.deHorseRiding, statistics.get("OrphanRouteCount").de.rhn)
    ),
    if (nlsNL) {
      <.div(
        <.p(
          "Aantal routes die niet tot een netwerk behoren."
        ),
        <.p(
          "Deze routes worden niet teruggevonden in een geldige ",
          context.gotoGlossaryEntry("network-relation", /*"lookup 'network relation' in glossary"*/ "netwerk relatie"),
          "."
        )
      )
    } else {
      <.div(
        <.p("Number of network routes that do not belong to a network."),
        <.p(
          "The route was not added as a member to a value ",
          context.gotoGlossaryEntry("network-relation", "network relation"),
          "."
        )
      )
    }
  )

  val orphanRouteKmInfo = UiOverviewInfo(
    nls("Orphan route length (km)", "Route wezen lengte (km)"),
    counts(statistics.get("OrphanRouteKm")),
    <.div(
      nls(
        "Total length (km) of the orphan routes (not included in network length above).",
        "Totale lengte (km) van de route wezen (deze worden niet meegeteld in de netwerk lengte hierboven)."
      )
    )
  )


  val integrityCheckNetworkCountInfo = UiOverviewInfo(
    nls("IntegrityCheck", "IntegrityCheck"),
    counts(statistics.get("IntegrityCheckNetworkCount")),
    <.div(
      nls(
        "Number of networks that include integrity check.",
        "Aantal netwerken met integriteitscontroles."
      )
    )
  )

  val integrityCheckCount = UiOverviewInfo(
    nls("IntegrityCheck", "IntegrityCheck"),
    counts(statistics.get("IntegrityCheckCount")),
    <.div(
      nls(
        "Number of nodes with integrity check.",
        "Aantal knooppunten met integriteitscontrole."
      )
    )
  )

  val integrityCheckFailedCount: UiOverviewInfo = factDetailCounts(Fact.IntegrityCheckFailed) {
    <.div(
      nls(
        "Number of failed integrity checks.",
        "Aantal falende integriteitscontroles."
      )
    )
  }

  val integrityCheckPassRateInfo = UiOverviewInfo(
    nls("IntegrityCheckPassRate", "Slaagpercentage"),
    counts(statistics.get("IntegrityCheckPassRate")),
    <.div(
      nls(
        "Integrity check pass rate (percentage of ok checks).",
        "Slaagpercentage van de integriteitscontroles."
      )
    )

  )

  val integrityCheckCoverageInfo = UiOverviewInfo(
    nls("IntegrityCheckCoverage", "Dekkingsgraad"),
    counts(statistics.get("IntegrityCheckCoverage")),
    <.div(
      nls(
        "Integrity check coverage (percentage of nodes that do have integrity check information).",
        "Dekkingsgraad van de integriteitscontroles (percentage van knooppunten met integriteitscontrole informatie)."
      )
    )
  )

  val nodeNetworkTypeNotTaggedInfo: UiOverviewInfo = factDetailCounts(Fact.NodeNetworkTypeNotTagged) {
    <.div(
      nls(
        "Number of nodes that are not tagged with 'network:type=node_network'.",
        "Aantal knooppunten zonder 'network:type=node_network' tag."
      )
    )
  }

  val routeNetworkTypeNotTaggedInfo: UiOverviewInfo = factDetailCounts(Fact.RouteNetworkTypeNotTagged) {
    <.div(
      nls(
        "Number of routes that are not tagged with 'network:type=node_network'.",
        "Aantal routes zonder 'network:type=node_network' tag."
      )
    )
  }

  val networkTypeNotTaggedInfo: UiOverviewInfo = factDetailCounts(Fact.NetworkTypeNotTagged) {
    <.div(
      nls(
        "Number of networks that are not tagged with 'network:type=node_network'.",
        "Aantal netwerken zonder 'network:type=node_network' tag."
      )
    )
  }

  val routeUnusedSegmentsInfo: UiOverviewInfo = factDetailCounts(Fact.RouteUnusedSegments) {
    <.div(
      nls(
        "Number of routes where one or more of the  ways (or part of ways) are not used in the " +
          "forward or backward path or in one of the tentacles.",
        "Aantal routes waarbij niet alle wegen (of stukken van wegen) gebruikt worden in de heen of " +
          "terugweg of in een van de tentakels."
      )
    )
  }

  val routeNodeMissingInWaysInfo: UiOverviewInfo = factDetailCounts(Fact.RouteNodeMissingInWays) {
    <.div(
      nls(
        "Number of routes for which the end node and/or the begin node is not found in any of the ways of the route.",
        "Aantal routes waarvan de begin en/of eind knooppunten niet voorkomen in een van de wegen van deze route."
      )
    )
  }

  val routeRedundantNodesInfo: UiOverviewInfo = factDetailCounts(Fact.RouteRedundantNodes) {
    <.div(
      nls(
        "Number of routes where the ways of the route contain extra nodes other than the start and end nodes.",
        "Aantal routes waarin er zich naast de begin en eind knooppunten nog andere vreemde knooppunten " +
          "bevinden in de wegen van de route."
      )
    )
  }

  val routeWithoutWaysInfo: UiOverviewInfo = factDetailCounts(Fact.RouteWithoutWays) {
    UiMarked(
      if (nlsEN) {
        "Routes without ways (a route is expected to have at least 1 way)."
      }
      else {
        """Aantal routes zonder wegen. We verwachten tenmiste 1 weg _("way")_."""
      }
    )
  }

  val routeNameMissingInfo: UiOverviewInfo = factDetailCounts(Fact.RouteNameMissing) {
    UiMarked(
      if (nlsEN) {
        """Routes without a _"note"_ tag with the route name."""
      }
      else {
        """Aantal routes zonder tag met sleutel _"note"_ met de route naam in de route relatie."""
      }
    )
  }

  val routeTagMissingInfo: UiOverviewInfo = factDetailCounts(Fact.RouteTagMissing) {
    UiMarked(
      if (nlsEN) {
        """The route relation does not contain a _"route"_ tag."""
      }
      else {
        """De _"route"_ tag ontbreekt in de route relatie."""
      }
    )
  }

  val routeTagInvalidInfo: UiOverviewInfo = factDetailCounts(Fact.RouteTagInvalid) {
    UiMarked(
      if (nlsEN) {
        """The value in the _"route"_ tag in the route relation is unexpected."""
      }
      else {
        """De waarde in de _"route"_ tag in de route relatie is onverwacht."""
      }
    )
  }

  val routeUnexpectedNode: UiOverviewInfo = factDetailCounts(Fact.RouteUnexpectedNode) {
    UiMarked(
      if (nlsEN) {
        "Number of routes with one or more unexpected node members. " +
          """In route relations we expect only nodes with tag _"rwn_ref"_ or _"rcn_ref"_."""
      }
      else {
        "Aantal routes met 1 of meer overwachte knopen. " +
          """In route relaties verwachten we enkel knopen met een tag met sleutel _"rwn_ref"_ of _"rcn_ref"_."""
      }
    )
  }

  val routeUnexpectedRelation: UiOverviewInfo = factDetailCounts(Fact.RouteUnexpectedRelation) {
    UiMarked(
      if (nlsEN) {
        """Number of routes with one or more unexpected members. In route relations we expect """ +
          """only members of type _"way"_, or members of type _"node"_ with a """ +
          """tag _"rwn_ref"_ or _"rcn_ref"_."""
      }
      else {
        """Aantal routes met 1 of meer overwachte relaties. In route relaties verwachten """ +
          """we enkel onderdelen van het type _"way"_, of onderdelen van het type _"node"_ met een tag""" +
          """met sleutel _"rwn_ref"_ of _"rcn_ref"_."""
      }
    )
  }

  val networkExtraMemberNode: UiOverviewInfo = factDetailCounts(Fact.NetworkExtraMemberNode) {
    if (nlsEN) {
      <.div(
        "Number of network relation members of type ",
        <.i("\"node\""),
        " that are unexpected (expect only ",
        context.gotoGlossaryEntry("node", "network nodes"),
        " or ",
        context.gotoGlossaryEntry("infoMap", "information maps"),
        " as members in the network relation)."
      )
    }
    else {
      <.div(
        "Aantal onverwachte knopen in netwerkrelaties (we verwachten enkel knopen dit ook echt een ",
        context.gotoGlossaryEntry("node", "knooppunt"),
        " definitie zijn, of ",
        context.gotoGlossaryEntry("infoMap", "informatie kaarten"),
        " in de netwerk relatie)."
      )
    }
  }

  val networkExtraMemberWay: UiOverviewInfo = factDetailCounts(Fact.NetworkExtraMemberWay) {
    UiMarked(
      if (nlsEN) {
        """Number of network relation members of type _"way"_ (expect only route relations or network nodes as members in the node network relation)."""
      }
      else {
        """Overwachte wegen _("ways")_ in netwerk relaties (in network relaties verwachten we enkel route relaties of knooppunten, geen wegen)."""
      }
    )
  }

  val networkExtraMemberRelation: UiOverviewInfo = factDetailCounts(Fact.NetworkExtraMemberRelation) {
    UiMarked(
      if (nlsEN) {
        """Number of network relation members of type _"relation"_ that are unexpected (expect only """ +
          "valid route relations or network nodes as members in the node network relation)."
      }
      else {
        "Aantal overwachte relaties in network relaties. In network relaties verwachten we " +
          "enkel route relaties of knooppunten, geen relaties anders dan route relaties."
      }
    )
  }

  val nodeMemberMissing: UiOverviewInfo = factDetailCounts(Fact.NodeMemberMissing) {
    UiMarked(
      if (nlsEN) {
        "Number of nodes that are not included in the network relation."
      }
      else {
        "Aantal knooppunten dat niet is opgenomen als lid in een netwerk relatie."
      }
    )
  }

  val nameMissingInfo: UiOverviewInfo = factDetailCounts(Fact.NameMissing) {
    UiMarked(
      if (nlsEN) {
        """Number of networks without _"name"_ tag in the network relation."""
      }
      else {
        """Aantal netwerken zonder tag met sleutel _"name"_ in de netwerk relatie."""
      }
    )
  }

  val routeUnaccessibleInfo: UiOverviewInfo = factDetailCounts(Fact.RouteUnaccessible) {
    if (nlsEN) {
      <.div(
        "Number of ",
        context.gotoGlossaryEntry("accessible", "unaccessible"),
        " routes."
      )
    }
    else {
      <.div(
        "Aantal ",
        context.gotoGlossaryEntry("accessible", "ontoegankelijke"),
        " routes."
      )
    }
  }

  val routeInvalidSortingOrderInfo: UiOverviewInfo = factDetailCounts(Fact.RouteInvalidSortingOrder) {
    <.div(
      nls(
        "Number of routes with ways in wrong sorting order.",
        "Aantal routes met wegen in verkeerde volgorde."
      )
    )
  }

  val routeReversedInfo: UiOverviewInfo = factDetailCounts(Fact.RouteReversed) {
    <.div(
      nls(
        "Number of routes where the ways are in reverse order (from high node number to low node number).",
        "Aantal routes met de wegen in omgekeerde volgorde (van hoog knooppunt nummer naar laag knooppunt nummer)."
      )
    )
  }

  val routeNodeNameMismatchInfo: UiOverviewInfo = factDetailCounts(Fact.RouteNodeNameMismatch) {
    <.div(
      nls(
        "Routes where the route name does not match with the names of the start node and the end node. The " +
          "route name is expected to contain the start node name and the end node name, separated by a dash. The " +
          "start node is expected to have a lower number than the end node.",
        "Aantal routes waarvan de route naam niet overeenkomt met de namen van de start en eind nodes. " +
          "We verwachten dat de route naam de namen van de start en eindknooppunten bevat, gescheiden door " +
          "een koppelteken. Het knooppunt met het laagste nummer verwachten we vooraan."
      )
    )
  }

  private def counts(stat: Statistic): UiOverviewCounts = {
    UiOverviewCounts(
      count(stat.total),
      count(stat.nl.rcn),
      count(stat.nl.rwn),
      count(stat.nl.rhn),
      count(stat.nl.rmn),
      count(stat.nl.rpn),
      count(stat.nl.rin),
      count(stat.be.rcn),
      count(stat.be.rwn),
      count(stat.be.rhn),
      count(stat.de.rcn),
      count(stat.de.rwn),
      count(stat.de.rhn)
    )
  }

  private def count(value: String): VdomElement = {
    <.span(
      value
    )
  }

  private def factDetailCounts(fact: Fact)(comment: VdomElement): UiOverviewInfo = {
    val stat = statistics.get(fact.name + "Count")

    val title = nls(fact.name, fact.nlName)

    UiOverviewInfo(
      title,
      UiOverviewCounts(
        count(stat.total),
        context.gotoSubsetFactDetails(Subset.nlBicycle, fact, "", stat.nl.rcn),
        context.gotoSubsetFactDetails(Subset.nlHiking, fact, "", stat.nl.rwn),
        context.gotoSubsetFactDetails(Subset.nlHorseRiding, fact, "", stat.nl.rhn),
        context.gotoSubsetFactDetails(Subset.nlMotorboat, fact, "", stat.nl.rmn),
        context.gotoSubsetFactDetails(Subset.nlCanoe, fact, "", stat.nl.rpn),
        context.gotoSubsetFactDetails(Subset.nlInlineSkates, fact, "", stat.nl.rin),
        context.gotoSubsetFactDetails(Subset.beBicycle, fact, "", stat.be.rcn),
        context.gotoSubsetFactDetails(Subset.beHiking, fact, "", stat.be.rwn),
        context.gotoSubsetFactDetails(Subset.beHorseRiding, fact, "", stat.be.rhn),
        context.gotoSubsetFactDetails(Subset.deBicycle, fact, "", stat.de.rcn),
        context.gotoSubsetFactDetails(Subset.deHiking, fact, "", stat.de.rwn),
        context.gotoSubsetFactDetails(Subset.deHorseRiding, fact, "", stat.de.rhn)
      ),
      comment
    )
  }

}
