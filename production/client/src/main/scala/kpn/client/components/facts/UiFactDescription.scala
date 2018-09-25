package kpn.client.components.facts

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsNL
import kpn.client.components.common.UiMarked
import kpn.shared.Fact
import kpn.shared.Fact._

object UiFactDescription {

  private case class Props(context: Context, fact: Fact)

  private val component = ScalaComponent.builder[Props]("fact-description")
    .render_P { props =>
      implicit val context: Context = props.context

      props.fact match {

        case RouteNotContinious =>
          <.p(
            if (nlsNL) {
              "De route is onderbroken: de begin- en eindknooppunten van deze route kunnen niet door wegen " +
                "verbonden worden in de heen en/of terug richting."
            } else {
              "The route is broken: the begin- and end-networknodes cannot be connected through ways either " +
                "in the forward direction or the backward direction or both."
            }
          )

        case RouteUnusedSegments =>
          <.p(
            if (nlsNL) {
              "Er zijn wegen of delen van wegen in de route definitie die niet passen in de verbindingen tussen de knooppunten."
            } else {
              "The route contains ways or part of ways that are not used in the connection between the end nodes."
            }
          )

        case RouteNodeMissingInWays =>
          <.p(
            if (nlsNL) {
              "Begin en/of eind knooppunt(en) komen niet voor in een van de wegen van deze route."
            } else {
              "The end node and/or the begin node is not found in any of the ways of this route."
            }
          )

        case RouteRedundantNodes =>
          <.p(
            if (nlsNL) {
              "Naast de begin en eind knooppunten bevinden er zich nog bijkomende knooppunten in de wegen van deze route."
            } else {
              "The ways of this route contain extra nodes other than the start and end nodes."
            }
          )

        case RouteWithoutWays =>
          <.p(
            if (nlsNL) {
              "De route bevat geen enkele weg (we verwachten ten minste 1 weg)."
            } else {
              "The route does not contain any ways (we expect the route to contain at least 1 way)."
            }
          )

        case RouteFixmetodo =>
          UiMarked(
            if (nlsNL) {
              """Er is nog werk aan deze route (heeft tag _"fixmetodo"_)."""
            } else {
              """Route definition needs work (has tag _"fixmetodo"_)."""
            }
          )
        case RouteNameMissing =>
          UiMarked(
            if (nlsNL) {
              """De route relatie bevat geen _"note"_ tag met de route naam."""
            } else {
              """The route relation does not have a _"note"_ tag with the route name."""
            }
          )

        case RouteEndNodeMismatch =>
          <.p(
            if (nlsNL) {
              "Het eind knooppunt stemt niet overeen met het laatste knooppunt in de laatste weg."
            } else {
              "The end node does not match the last node in the last way."
            }
          )

        case RouteStartNodeMismatch =>
          <.p(
            if (nlsNL) {
              "Het start knooppunt stemt niet overeen met het eerste knooppunt in de eerste weg."
            } else {
              "The start node does not match the first node in the first way."
            }
          )
        case RouteTagMissing =>
          UiMarked(
            if (nlsNL) {
              """De verplichte _"route"_ tag ontbreekt in de routerelatie."""
            } else {
              """Routerelation does not contain the required _route_ tag."""
            }
          )

        case RouteTagInvalid =>
          UiMarked {
            if (nlsNL) {
              """
                |De verplichte _"route"_ tag heeft een ongeldige waarde.
                |
                |Een fietsnetwerk route relatie moet minimaal een tag met sleutel _"route"_ en waarde _"bicycle"_ hebben.
                |
                |Een wandelnetwerk route relatie moet minimaal een tag met sleutel _"route"_ en waarde _"foot"_,
                | _"hiking"_ of _"walking"_ hebben. De waarde _"walking"_ wordt ook vaak gevonden en door de analyse
                | logica aanvaard, al is deze waarde niet gedocumenteerd in de OSM wiki paginas.
              """.stripMargin
            } else {
              """
                |Invalid value in required tag _"route"_ in route relation.
                |
                |A bicycle route relation needs to have value _"bicycle"_ in its _"route"_ tag.
                |
                |A hiking route relation needs to have one of the following values in its _"route"_ tag: _"foot"_,
                |_"hiking"_, or _"walking"_. Note that _"walking"_ is a value that is frequently found,
                |but not actually documented as a valid value in the OSM wiki pages.
              """.stripMargin
            }
          }

        case RouteUnexpectedNode =>
          <.p(
            if (nlsNL) {
              "De route relatie bevat 1 of meerdere onverwachte knopen"
            } else {
              "The route relation contains 1 or more unexpected nodes"
            }
          )

        case RouteUnexpectedRelation =>
          UiMarked(
            if (nlsNL) {
              """
                |De route relatie bevat 1 of meer overwachte relaties. In route relaties verwachten we enkel
                |onderdelen van het type _"way"_, of onderdelen van het type _"node"_ met een tag met sleutel
                |_"rwn_ref"_ of _"rcn_ref"_.""".stripMargin
            } else {
              """
                |The route relation contains one or more unexpected relation members. In route relations
                |we expect only members of type _"way"_, or members of type _"node"_ with a tag
                |_"rwn_ref"_ or _"rcn_ref"_.""".stripMargin
            }
          )

        case NetworkExtraMemberNode =>
          UiMarked(
            if (nlsNL) {
              """De netwerk relatie bevat 1 of meerdere onverwachte knopen (we verwachten enkel knopen die
              ook echt een knooppunt definitie zijn, or een netwerk kaart)."""
            } else {
              """The network relation contains members of type _"node"_ that are unexpected (we expect
              only network nodes or information maps as members in the network relation)."""
            }
          )

        case NetworkExtraMemberWay =>
          UiMarked(
            if (nlsNL) {
              """
                |De netwerk relatie bevat overwachte wegen (in network relaties verwachten we enkel route
                |relaties of knooppunten, geen wegen).""".stripMargin
            } else {
              """
                |The network relation contains members of type _"way"_ (expect only route relations or
                |network nodes as members in the node network relation).""".stripMargin
            }
          )

        case NetworkExtraMemberRelation =>
          UiMarked(
            if (nlsNL) {
              """De netwerk relatie bevat overwachte relaties. In network relaties verwachten we enkel
              route relaties of knooppunten, geen relaties anders dan route relaties."""
            } else {
              """The network relation contains members of type _"relation"_ that are unexpected
              (expect only valid route relations or network nodes as members in the node network relation)."""
            }
          )

        case NodeMemberMissing =>
          UiMarked(
            if (nlsNL) {
              """Het knooppunt is niet opgenomen als lid in de netwerk relatie."""
            } else {
              """The node is not member of the network relationn."""
            }
          )

        case IntegrityCheckFailed =>
          <.p(
            if (nlsNL) {
              """Het aantal routes die in het knooppunt aankomen of vertrekken is niet het verwachte aantal.
                |Routes met rol "connection" in de netwerk relatie, of tag "state" gelijk aan "connection"
                | of "alternate" worden niet meegeteld.""".stripMargin
            } else {
              """The actual number of routes does not match the expected number of routes. Routes with
                |role "connection" in the network relation, or tag "state" equal to "connection"
                |or "alternate" are not counted.""".stripMargin
            }
          )

        case OrphanRoute =>
          <.p(
            if (nlsNL) {
              """Deze route maakt geen deel uit van een netwerk. Deze route wordt niet
                |teruggevonden in een geldige netwerk relatie.""".stripMargin
            } else {
              """This route does not belong to a network. The route was not added as a member to
                |a valid network relation.""".stripMargin
            }
          )

        case OrphanNode =>
          UiMarked(
            if (nlsNL) {
              """Op zichzelf staande knooppunt (niet teruggevonden in een netwerk).
                |Dit knooppunt werd niet teruggevonden als deel van een geldige netwerk relatie,
                |of als deel van een geldige route relatie (dit kan een route relatie zijn die
                |deel uitmaakt van een network relatie, maar ook een op zichzelf staande route
                |relatie die geen deel uitmaakt van een network relatie).""".stripMargin
            } else {
              """This node does not belong to a network.
                |The node was not added as a member to a valid network relation, and also not added
                |as a member to valid route relation (that itself was added as a member to a valid
                |network relation or is an orphan route).""".stripMargin
            }
          )

        case RouteIncomplete =>
          UiMarked(
            if (nlsNL) {
              """De route is onvolledig. De routes zijn uitdrukkelijk gemarkeerd als onvolledig door het
                |toevoegen van een tag met sleutel _"fixme"_ en waarde _"incomplete"_ in de route relatie.""".stripMargin
            } else {
              """The route is marked as having an incomplete definition. A route definition is explicitely
                |marked incomplete by adding a tag _"fixme"_ with value _"incomplete"_ in the route relation.""".stripMargin
            }
          )

        case RouteUnaccessible =>
          if (nlsNL) {
            <.p(
              "Een deel van de route lijkt niet ",
              context.gotoGlossaryEntry("accessible", "toegankelijk"),
              "."
            )
          } else {
            <.p(
              "Part of the route does not seem ",
              context.gotoGlossaryEntry("accessible", "accessible"),
              "."
            )
          }

        case RouteInvalidSortingOrder =>
          <.p(
            if (nlsNL) {
              "De wegen in deze route zijn volledig maar de volgorde is door elkaar gehaald."
            } else {
              "The route is valid, but the sorting order of the ways is incorrect."
            }
          )

        case RouteReversed =>
          <.p(
            if (nlsNL) {
              "De wegen in deze route zijn in omgekeerde volgorde (van hoog knooppunt nummer naar laag knooppunt nummer)."
            } else {
              "The ways in this route are in reverse order (from high node number to low node number)."
            }
          )

        case RouteNodeNameMismatch =>
          UiMarked(
            if (nlsNL) {
              """De route naam in de _"note"_ tag in de route relatie is anders dan wat we verwachten op basis van """ +
                "de start en eind knooppunten van de route."
            } else {
              """The route name in the _note_ tag in the route relation does not match the expected name as """ +
                "derived from the start and end node of the route."
            }
          )

        case RouteNotForward =>
          <.p(
            if (nlsNL) {
              "Er is geen verbinding van het startknooppunt naar het eindknooppunt (heen weg)."
            } else {
              "There is no path in the forward direction (from start node to end node)."
            }
          )

        case RouteNotBackward =>
          <.p(
            if (nlsNL) {
              "Er is geen verbinding van het eindknooppunt naar het startknooppunt (terug weg)."
            } else {
              "There is no path in the backward direction (from end node to start node)."
            }
          )

        case RouteAnalysisFailed =>
          <.p(
            if (nlsNL) {
              "Er heeft zich een probleem voorgedaan tijdens de route analyse. De route is waarschijnlijk te complex."
            } else {
              "The route could not be analyzed (too complex?)."
            }
          )

        case RouteOverlappingWays =>
          <.p(
            if (nlsNL) {
              "Er is geen gedetailleerde route analyse gedaan omdat er overlappende wegen zijn in de route."
            } else {
              "No detailed route analysis is performed because the route contains overlapping ways."
            }
          )

        case RouteSuspiciousWays =>
          <.p(
            if (nlsNL) {
              "Route met rare wegen (bijvoorbeeld wegen met slechts 1 punt)."
            } else {
              "Route with funny ways (for example ways with only 1 node)."
            }
          )

        case RouteBroken =>
          <.p(
            if (nlsNL) {
              "TODO"
            } else {
              "TODO"
            }
          )

        case RouteOneWay =>
          <.p(
            if (nlsNL) {
              """De route is getagged als bruikbaar in slechts 1 richting. Dit is OK."""
            } else {
              """The route is tagged as useable in one direction only. This is OK."""
            }
          )

        case RouteNotOneWay =>
          <.p(
            if (nlsNL) {
              """De route is getagged als bruikbaar in slechts 1 richting, maar de analyse logica heeft zowel een heenweg als een terugweg gevonden."""
            } else {
              """The route is tagged as useable in one direction only, but the analysis logic does find ways in both directions."""
            }
          )

        case NameMissing =>
          <.p(
            if (nlsNL) {
              """De netwerk relatie bevat geen verplichte tag met sleutel "name"."""
            } else {
              """The network relation does not contain the mandatory tag with key "name"."""
            }
          )

        case IgnoreForeignCountry =>
          <.p(
            if (nlsNL) {
              """Niet opgenomen in analyse: bevind zich in ander land dan Nederland, België of Duitsland."""
            } else {
              """Not included in analysis: located in a country different from The Netherlands, Belgium or Germany."""
            }
          )

        case IgnoreNoNetworkNodes =>
          <.p(
            if (nlsNL) {
              """Niet opgenomen in analyse: route bevat geen knooppunten."""
            } else {
              """Not included in analysis: route does not contain network nodes."""
            }
          )

        case IgnoreUnsupportedSubset =>
          <.p(
            if (nlsNL) {
              """Niet opgenomen in analyse: geen wandelnetwerken in Duitsland."""
            } else {
              """Not included in analysis: no node networks in Germany."""
            }
          )

        case Added =>
          <.p(
            if (nlsNL) {
              """Toegevoegd aan de analyse."""
            } else {
              """Added to the analysis."""
            }
          )

        case BecomeIgnored =>
          <.p(
            if (nlsNL) {
              """Niet langer opgenomen in de analyse."""
            } else {
              """No longer included in the analysis."""
            }
          )

        case BecomeOrphan =>
          <.p(
            if (nlsNL) {
              """Wees geworden."""
            } else {
              """Has become orphan."""
            }
          )

        case Deleted =>
          <.p(
            if (nlsNL) {
              """Verwijderd uit de OpenStreetMap database."""
            } else {
              """Deleted from the OpenStreetMap database."""
            }
          )

        case IgnoreNetworkCollection =>
          <.p(
            if (nlsNL) {
              """Niet opgenomen in de analyse omdat deze relatie een verzameling van netwerken is."""
            } else {
              """Not included in the analysis because this relation is a collection of networks."""
            }
          )

        case IntegrityCheck =>
          <.p(
            if (nlsNL) {
              """Dit knooppunt heeft een label dat het aantal verwachte routes dat aankomt/vertrekt op dit knooppunt weergeeft."""
            } else {
              """This network node has a tag that indicates the expected number of routes that arrive/depart in this node."""
            }
          )

        case LostBicycleNodeTag =>
          <.p(
            if (nlsNL) {
              """Deze punt is niet langer een geldig fietsnetwerk knooppunt omdat de rcn_ref label verwijderd is."""
            } else {
              """This node is no longer a valid bicylenetwork node because the rcn_ref tag has been removed."""
            }
          )

        case LostHikingNodeTag =>
          <.p(
            if (nlsNL) {
              """Deze punt is niet langer een geldig wandelnetwerk knooppunt omdat de rwn_ref label verwijderd is."""
            } else {
              """This node is no longer a valid hikingnetwork node because the rwn_ref tag has been removed."""
            }
          )

        case LostRouteTags =>
          <.p(
            if (nlsNL) {
              """Deze relatie is niet langer een geldige netwerk route omdat een benodigde label verwijderd is."""
            } else {
              """This relation is no longer a valid network route because a required tag has been removed."""
            }
          )

        case WasIgnored =>
          <.p(
            if (nlsNL) {
              """Niet langer uitgesloten uit de analysis."""
            } else {
              """No longer excluded from the analysis."""
            }
          )

        case WasOrphan =>
          <.p(
            if (nlsNL) {
              """Niet langer een wees."""
            } else {
              """No longer an orphan."""
            }
          )

        case _ =>
          <.p(
            nls(
              s"${props.fact.name} description still missing",
              s"${props.fact.nlName} beschrijving ontbreekt nog"
            )
          )
      }
    }
    .build

  def apply(fact: Fact)(implicit context: Context): VdomElement = {
    component(Props(context, fact))
  }
}
