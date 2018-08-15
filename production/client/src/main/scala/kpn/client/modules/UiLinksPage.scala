package kpn.client.modules

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nlsNL
import kpn.client.components.common.DefaultBackend
import kpn.client.components.common.DefaultProps
import kpn.client.components.common.PageState
import kpn.client.components.common.UiData
import kpn.client.components.common.UiPageContents

object UiLinksPage {

  private class Backend(scope: BackendScope[DefaultProps, PageState[Unit]]) extends DefaultBackend(scope) {

    protected def contents(props: DefaultProps, state: PageState[Unit]): VdomElement = {
      implicit val context: Context = props.context
      new Renderer().render()
    }

    protected def title: String = "Links"
  }

  private val component = ScalaComponent.builder[DefaultProps]("links")
    .initialState(PageState.ready)
    .renderBackend[Backend]
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
      }
    }
    .componentWillUnmount { scope =>
      scope.backend.removeResizeListener()
    }
    .build

  def apply(context: Context): VdomElement = component(DefaultProps(context))

  private class Renderer(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        <.h1("Links"),
        UiPageContents(
          UiData("General", "Algemeen")(
            <.ul(
              <.li(
                <.a(
                  ^.cls := "external",
                  ^.href := "https://openstreetmap.org",
                  ^.target := "_blank",
                  "OpenStreetMap"
                ),
                <.p(
                  if (nlsNL) {
                    "Start pagina voor OpenStreetMap."
                  } else {
                    "Home page for OpenStreetMap."
                  }
                )
              ),
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/NL:Hoofdpagina",
                      ^.target := "_blank",
                      "Wiki"
                    ),
                    <.p("OpenStreetMap documentatie.")
                  )
                }
                else {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/Main_Page",
                      ^.target := "_blank",
                      "Wiki"
                    ),
                    <.p("OpenStreetMap documentation.")
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.a(
                    ^.cls := "external",
                    ^.href := "https://www.osm.be/",
                    ^.target := "_blank",
                    "OSM België"
                  )
                }
                else {
                  <.a(
                    ^.href := "https://www.osm.be/",
                    ^.cls := "external",
                    ^.target := "_blank",
                    "OSM Belgium"
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://lists.openstreetmap.org/mailman/listinfo/talk-be",
                      ^.target := "_blank",
                      "Belgische email lijst"
                    ),
                    " (Engels)"
                  )
                }
                else {
                  <.a(
                    ^.href := "https://lists.openstreetmap.org/mailman/listinfo/talk-be",
                    ^.cls := "external",
                    ^.target := "_blank",
                    "Belgian mailing-list"
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.a(
                    ^.cls := "external",
                    ^.href := "https://forum.openstreetmap.org/viewforum.php?id=12",
                    ^.target := "_blank",
                    "OSM Forum Nederland"
                  )
                }
                else {
                  <.a(
                    ^.href := "https://forum.openstreetmap.org/viewforum.php?id=12",
                    ^.cls := "external",
                    ^.target := "_blank",
                    "OSM Forum The Netherlands"
                  )
                }
              )
            )
          ),
          UiData("Node Networks", "Knooppunt netwerken")(
            <.ul(
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/Cycle_Node_Network_Tagging",
                      ^.target := "_blank",
                      "Mappen van fietsnetwerken"
                    ),
                    " (Engels) (",
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/Talk:Cycle_Node_Network_Tagging",
                      ^.target := "_blank",
                      "discussie"
                    ),
                    " Nederlands)",
                    <.p("Details over het mappen van fietsnetwerken (ook geldig voor wandelnetwerken).")
                  )
                }
                else {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/Cycle_Node_Network_Tagging",
                      ^.target := "_blank",
                      "Cycle node tagging"
                    ),
                    "(",
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/Talk:Cycle_Node_Network_Tagging",
                      ^.target := "_blank",
                      "talk"
                    ),
                    "Dutch)",
                    <.p("Details about mapping bicyle node networks (also valid for mapping walking node networks).")
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/WikiProject_Nederland_Wandelroutes#Tagging",
                      ^.target := "_blank",
                      "Wandelnetwerken in Nederland"
                    ),
                    <.p("Achtergrond informatie bij het mappen van wandelnetwerken in Nederland.")
                  )
                }
                else {
                  <.div(
                    <.a(
                      ^.className := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/WikiProject_Nederland_Wandelroutes#Tagging",
                      "Walking networks in The Netherlands"
                    ),
                    "(Dutch)",
                    <.p("Background information related to mapping walking networks in The Netherlands.")
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/WikiProject_Belgium/Conventions/Walking_Routes",
                      ^.target := "_blank",
                      "Wandel routes in Belgie"
                    ),
                    " (Engels)",
                    <.p("Achtergrond informatie bij het mappen van wandelnetwerken in Belgie.")
                  )
                }
                else {
                  <.div(
                    <.a(
                      ^.href := "https://wiki.openstreetmap.org/wiki/WikiProject_Belgium/Conventions/Walking_Routes",
                      ^.cls := "external",
                      ^.target := "_blank",
                      "Belgian conventions"
                    ),
                    <.p("Background information related to mapping node networks in Belgium.")
                  )
                }
              ),
              <.li(
                if (nlsNL) {
                  <.div(
                    <.a(
                      ^.cls := "external",
                      ^.href := "https://wiki.openstreetmap.org/wiki/User:Polyglot/Some_ways_to_simplify_editing_cycle_node_routes_with_JOSM",
                      ^.target := "_blank",
                      "Hulpmiddel voor JOSM"
                    ),
                    " (Engels)",
                    <.p("Hulpmiddel om het mappen van knooppuntnetwerken te vereenvouding wanneer met JOSM gewerkt wordt.")
                  )
                }
                else {
                  <.div(
                    <.a(
                      ^.href := "https://wiki.openstreetmap.org/wiki/User:Polyglot/Some_ways_to_simplify_editing_cycle_node_routes_with_JOSM",
                      ^.cls := "external",
                      ^.target := "_blank",
                      "Edit help in JOSM"
                    ),
                    <.p("Describes how to simplify editing node networks in JOSM.")
                  )
                }
              )
            )
          )
        )
      )
    }
  }

}
