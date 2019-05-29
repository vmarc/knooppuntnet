// TODO migrate to Angular
package kpn.client.modules

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.GlossaryPageArgs
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsNL
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.PageState
import kpn.client.components.common.UiAppBar
import kpn.client.components.common.UiData
import kpn.client.components.common.UiMarked
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.common.UiPageContents
import kpn.client.components.facts.UiFactDescription
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.shared.Fact
import kpn.shared.Fact.IntegrityCheck
import kpn.shared.Fact.IntegrityCheckFailed
import kpn.shared.Fact.NameMissing
import kpn.shared.Fact.NetworkExtraMemberNode
import kpn.shared.Fact.NetworkExtraMemberRelation
import kpn.shared.Fact.NetworkExtraMemberWay
import kpn.shared.Fact.NodeMemberMissing
import kpn.shared.Fact.OrphanNode
import kpn.shared.Fact.OrphanRoute
import kpn.shared.Fact.RouteBroken
import kpn.shared.Fact.RouteEndNodeMismatch
import kpn.shared.Fact.RouteFixmetodo
import kpn.shared.Fact.RouteIncomplete
import kpn.shared.Fact.RouteIncompleteOk
import kpn.shared.Fact.RouteInvalidSortingOrder
import kpn.shared.Fact.RouteNameMissing
import kpn.shared.Fact.RouteNodeMissingInWays
import kpn.shared.Fact.RouteNodeNameMismatch
import kpn.shared.Fact.RouteNotBackward
import kpn.shared.Fact.RouteNotContinious
import kpn.shared.Fact.RouteNotForward
import kpn.shared.Fact.RouteRedundantNodes
import kpn.shared.Fact.RouteReversed
import kpn.shared.Fact.RouteStartNodeMismatch
import kpn.shared.Fact.RouteTagInvalid
import kpn.shared.Fact.RouteTagMissing
import kpn.shared.Fact.RouteUnaccessible
import kpn.shared.Fact.RouteUnexpectedNode
import kpn.shared.Fact.RouteUnexpectedRelation
import kpn.shared.Fact.RouteUnusedSegments
import kpn.shared.Fact.RouteWithoutWays
import org.scalajs.dom

object UiGlossaryPage {

  private case class Props(args: GlossaryPageArgs)

  private case class State(pageState: PageState[Unit] = PageState.ready)

  private class Backend(scope: BackendScope[Props, State]) extends AbstractBackend[Unit] {

    protected def pageState: PageState[Unit] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[Unit]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.args.context

      val content = UiPageContent(
        nls("Glossary", "Woordenlijst"),
        state.pageState.ui.status,
        CallbackTo {
          new Renderer().render()
        }
      )

      val pageProps = pagePropsWithContext(context)

      UiPage(
        pageProps,
        Seq(
          UiAnalysisMenu(pageProps),
          UiSidebarFooter(pageProps)
        ),
        content
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("glossary")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
        dom.window.setTimeout(() => {
          val entry = scope.props.args.entry
          if (entry.isDefined) {
            val element = dom.document.getElementById(scope.props.args.entry.get)
            element.scrollIntoView()
            dom.window.scrollBy(0, -(UiAppBar.appBarHeight + 10))
          }
        }, 40)
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(args: GlossaryPageArgs): VdomElement = component(Props(args))

  private class Renderer(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        title(),
        UiPageContents(
          intro(),
          rwn(),
          rcn(),
          node(),
          networkRelation(),
          routeRelation(),
          orphanNode(),
          orphanRoute(),
          infoMap(),
          accessible(),
          <.h1(
            if (nlsNL) {
              "Feitenlijst"
            } else {
              "Factlist"
            }
          ),
          fact(RouteNotContinious),
          fact(RouteNotForward),
          fact(RouteNotBackward),
          fact(RouteUnusedSegments),
          fact(RouteNodeMissingInWays),
          fact(RouteRedundantNodes),
          fact(RouteWithoutWays),
          fact(RouteFixmetodo),
          fact(RouteNameMissing),
          fact(RouteEndNodeMismatch),
          fact(RouteStartNodeMismatch),
          fact(RouteTagMissing),
          fact(RouteTagInvalid),
          fact(RouteUnexpectedNode),
          fact(RouteUnexpectedRelation),
          fact(NetworkExtraMemberNode),
          fact(NetworkExtraMemberWay),
          fact(NetworkExtraMemberRelation),
          fact(NodeMemberMissing),
          fact(IntegrityCheckFailed),
          fact(NameMissing),
          fact(OrphanRoute),
          fact(OrphanNode),
          fact(RouteIncomplete),
          fact(RouteIncompleteOk),
          fact(RouteUnaccessible),
          fact(RouteInvalidSortingOrder),
          fact(RouteReversed),
          fact(RouteNodeNameMismatch),
          fact(RouteBroken),
          fact(IntegrityCheck)
        )
      )
    }

    private def title(): VdomElement = {
      <.h1(
        if (nlsNL) {
          "Woordenlijst"
        } else {
          "Glossary"
        }
      )
    }

    private def intro(): VdomElement = {
      <.p(
        if (nlsNL) {
          "Hier vind je wat meer uitleg bij sommige van de termen die in de analyse gebruikt worden."
        } else {
          "This page documents terms used in the analyzer."
        }
      )
    }

    private def rwn(): VdomElement = {
      UiData("rwn", "rwn", Some("rwn"))(
        UiMarked(s"""__R__egional __W__alking __N__etwork${if (nlsNL) " (regionaal wandelnetwerk)" else ""}""")
      )
    }

    private def rcn(): VdomElement = {
      UiData("rcn", "rcn", Some("rcn"))(
        UiMarked(s"""__R__egional __C__ycle __N__etwork${if (nlsNL) " (regionaal fietsnetwerk)" else ""}""")
      )
    }

    private def node(): VdomElement = {
      UiData("Node", "Knooppunt", Some("node"))(
        <.div("...")
      )
    }

    private def networkRelation(): VdomElement = {

      val nl =
        """
          | Relatie met knoopunten en routes.
          |
          | Een wandelnetwerk relatie heeft minimaal de volgende tags:
          |
          || Sleutel | Waarde  | Commentaar |
          || ------- | ------- | ---------- |
          || network | rwn     | rwn = regional walking network |
          || type    | network | |
          || name    | _naam_  | netwerk naam  |
          |
          | Een fietsnetwerk relatie heeft minimaal de volgende tags:
          |
          || Sleutel | Waarde  | Commentaar |
          || ------- | ------- | ---------- |
          || network | rcn     | rcn = regional cyclde network |
          || type    | network | |
          || name    | _naam_  | netwerk naam  |
        """.stripMargin

      val en =
        """
          |Relation with node and route members.
          |
          |A walking network relation has the following required tags:
          |
          || Tag     | Value   | Comment |
          || ------- | ------- | ------- |
          || network | rwn     | rwn = regional walking network |
          || type    | network | |
          || name    | _name_  | network name |
          |
          |A cycle network relation has the following required tags:
          |
          || Tag     | Value   | Comment |
          || ------- | ------- | ------- |
          || network | rcn     | rcn = regional cycle network |
          || type    | network | |
          || name    | _name_  | network name |
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Network relation", "Netwerkrelatie", Some("network-relation"))(
        UiMarked(text)
      )
    }

    private def routeRelation(): VdomElement = {

      val commentaar = "We verwachten dat de route naam  wordt samengesteld uit de namen van het start knooppunt en het " +
        "eind knooppunt, van elkaar gescheiden door een koppelteken. Bijvoorbeeld: de naam van de met start " +
        "knooppunt _23_ en eindknooppunt _68_ is: _23-68_."

      val nl =
        s"""
           |Een route relatie bevat de wegen die de knooppunten met elkaar verbinden, en optioneel ook de
           |knopen van de knooppunten zelf.
           |
           |Een wandelnetwerk route relatie heeft minimaal de volgende tags:
           |
           || Sleutel | Waarde | Commentaar |
           || ------- | ------ | ---------- |
           || network | rwn    | rwn = regional walking network |
           || type    | route  | |
           || route   | foot   | In plaats van _foot_ laat de analyse logica ook de waarden _hiking en _walking_ toe. |
           || note    | _naam_ | $commentaar|
           |
           |Een fietsnetwerk route relatie heeft minimaal de volgende tags:
           |
           || Sleutel | Waarde  | Commentaar |
           || ------- | ------- | ---------- |
           || network | rcn     | rcn = regional cycling network |
           || type    | route   | |
           || route   | bicycle | |
           || note    | _naam_  | route naam |
           |
        """.stripMargin

      val comment = "The route name is expected to include the start network node name and the end network node name " +
        "separated by a dash. Example: _23-68_ for the route between start node _23_ and end node _68_."

      val en =
        s"""
           |The route relation contains the ways connecting the network nodes, and optionally also
           |the network nodes themselves.
           |
           |A walking network route relation has the following required tags:
           |
           || Tag     | Value  | Comment |
           || ------- | ------ | ------- |
           || network | rwn    | rwn = regional walking network |
           || type    | route  | |
           || route   | foot   | Instead of _foot_, the analyzer also allows _hiking_ and _walking_. |
           || note    | _name_ | $comment|
           |
           |A bicyle network route relation has the following required tags:
           |
           || Tag     | Value   | Comment |
           || ------- | ------- | ------- |
           || network | rcn     | rcn = regional cycle network |
           || type    | route   | |
           || route   | bicycle | |
           || note    | _name_  | route name |
           |
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Route relation", "Route relatie", Some("route-relation"))(
        UiMarked(text)
      )
    }

    private def orphanNode(): VdomElement = {

      val nl =
        """
          | Een _knooppunt wees_ is een knooppunt waarvan we niet weten bij welk knooppuntnetwerk
          | het hoort.
          |
          | We kunnen zien dat de knoop een knooppunt is uit een knooppuntennetwerk omdat er een tag
          | met sleutel *"rwn_ref"* of *"rcn_ref"* is, maar deze knoop werd niet toegevoegd
          | aan een netwerk- of een routerelatie.
          |
          | Indien er iets mis is met de relatie waaraan de knoop werd toegevoegd (bijvoorbeeld omdat de
          | relatie niet voldoet aan de regels voor een geldige netwerk- of routerelatie), dan wordt
          | het knooppunt ook beschouwd als _"wees"_.
        """.stripMargin

      val en =
        """
          | An orphan node is a network node without a known network that is belongs to.
          |
          | We can see that the node is a network node because it has the required *"rwn_ref"* or
          | *"rcn_ref"* tag, but the node was not added as a member to a known network relation,
          | and also not added as a member to known route relation that itself was added as a member
          | to a known network relation.
          |
          | If there is something wrong the relation that the node is added to (for example when that
          | relation is not following the rules for a valid network or route relation), then the network
          | node is also considered _"orphan"_.
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Orphan node", "Knooppuntwees", Some("orphan-node"))(
        UiMarked(text)
      )
    }

    private def orphanRoute(): VdomElement = {

      val nl =
        """
          | Een _route wees_ is een route waarvan we niet weten bij welk knooppuntnetwerk die hoort.
          |
          | De relatie wordt herkend als een geldige knooppuntnetwerkroute, maar de relatie werd niet
          | toegevoegd in een gekende en geldige netwerkrelatie.
          |
          | Als er iets mis is met de netwerk relatie waaraan de route relatie toegevoegd is, dan wordt de route
          | ook als _"wees"_ beschouwd.
        """.stripMargin

      val en =
        """
          | An orphan route is a network route without a known network that is belongs to.
          |
          | We can see that the relation represents a network route because it has the required tags,
          | but the relation was not added as a member to a known network relation.
          |
          | If there is something wrong the relation that the route is added to, then the route is
          | also considered _"orphan"_.
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Orphan route", "Route wees", Some("orphan-route"))(
        UiMarked(text)
      )
    }

    private def infoMap(): VdomElement = {

      val nl =
        """
          | Verspreid over het netwerk bevinden er zich vaak informatie borden met daarop
          | een kaart van het netwerk en wat verdere toelichting. Deze borden kunnen gemapped
          | worden door middel van knopen. Deze knopen kunnen eventueel toegevoegd worden
          | in de netwerk relatie. De analyse logica verwacht dat de knoop ten minste de
          | volgende tags bevat:
          |
          || Sleutel     | Waarde      |
          || ----------- | ----------- |
          || tourism     | information |
          || information | map         |
        """.stripMargin

      val en =
        """
          | Information maps are situated along the network routes and provide an overview
          | of the network. They can be mapped using nodes that can be optionally be added
          | to the network relation. The analysis logic expects at least the following tags
          | on the node to recognize it as an information map:
          |
          || Tag         | Value       |
          || ----------- | ----------- |
          || tourism     | information |
          || information | map         |
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Information Map", "Informatie bord", Some("info-map"))(
        UiMarked(text)
      )
    }

    private def accessible(): VdomElement = {

      val nl =
        """
          |De analyse logica probeert na te gaan of de routes in het knooppuntennetwerk ook daadwerkelijk
          |toegankelijk zijn voor de gebruiker waar het netwerk voor bedoeld is.
          |
          |Een weg (way) in een wandel-, fiets-, ruiter-, of skateroute wordt als _"toegankelijk"_ beschouwd indien
          |aan minstens 1 van de volgende voorwaarden voldaan is:
          |
          |* De weg heeft een tag met sleutel _"highway"_ of  _"highway:virtual"_.
          |* De weg heeft een tag met sleutel _"route"_ en waarde _"ferry"_.
          |* De weg in een fietsnetwerk heeft een tag met sleutel _"bicycle"_ en waarde _"yes"_.
          |* De weg in een wandelnetwerk heeft een tag met sleutel _"foot"_ en waarde _"yes"_.
          |* De weg in een ruiternetwerk heeft een tag met sleutel _"horse"_ en waarde _"yes"_.
          |* De weg in een skatenetwerk heeft een tag met sleutel _"inline_skates"_ en waarde _"yes"_.
          |
          |Motorboot netwerk:
          |
          |* De weg heeft een tag met sleutel _"waterway"_ of  _"waterway:virtual"_.
          |
          |Kano netwerk, minstens 1 van:
          |
          |* De weg heeft een tag met sleutel _"waterway"_ of  _"waterway:virtual"_.
          |* De weg heeft een tag met sleutel _"canoe"_ en waarde  _"yes"_.
          |* De weg heeft een tag met sleutel _"canoe"_ en waarde  _"portage"_.
          |
        """.stripMargin

      val en =
        """
          |The analyzer tries to determine whether the routes as defined in the network are actually
          |accessible for the user for which the network is intended.
          |
          |The validation rules for hiking-, bicycle-, horse- and skateroutes dictate that one of the
          |following is true for each way in the route relation for the route to be considered _"accessible"_:
          |
          |* The way has a value for tag _"highway"_ or _"highway:virtual"_.
          |* The way has tag _"route"_ with value _"ferry"_.
          |* The way in the bicycle network has tag _"bicycle"_ with value _"yes"_.
          |* The way in the hiking network has tag _"foot"_ with value _"yes"_.
          |* The way in the horse network has tag _"horse"_ with value _"yes"_.
          |* The way in the skate network has tag _"inline_skates"_ with value _"yes"_.
          |
          |Motorboat network:
          |
          |* The way has a tag with key _"waterway"_ or  _"waterway:virtual"_.
          |
          |Canoe network, at least 1 of:
          |
          |* The way has a tag with key  _"waterway"_ or _"waterway:virtual"_.
          |* The way has a tag with key  _"canoe"_ and value  _"yes"_.
          |* The way has a tag with key  _"canoe"_ and value _"portage"_.
          |
        """.stripMargin

      val text = if (nlsNL) nl else en

      UiData("Accessible", "Toegankelijk", Some("accessible"))(
        UiMarked(text)
      )
    }

    private def fact(fact: Fact): VdomElement = {
      UiData(fact.name, fact.nlName, Some(fact.name))(
        UiFactDescription(fact)
      )
    }
  }

}
