// TODO migrate to Angular
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
import kpn.client.components.common.AbstractBackend
import kpn.client.components.common.DefaultProps
import kpn.client.components.common.PageProps
import kpn.client.components.common.PageState
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContents
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import org.scalajs.dom

object UiAboutPage {

  private case class State(pageState: PageState[Unit] = PageState(), page: Option[dom.XMLHttpRequest] = None)

  private class Backend(scope: BackendScope[DefaultProps, State]) extends AbstractBackend[Unit] {

    protected def pageState: PageState[Unit] = scope.state.runNow().pageState

    protected def updatePageState(pageState: PageState[Unit]): Unit = {
      scope.modState(_.copy(pageState = pageState)).runNow()
    }

    def render(args: DefaultProps, state: State): VdomElement = {

      implicit val context: Context = args.context
      val pageProps = pagePropsWithContext(args.context)

      new Renderer(pageProps, state).render()
    }
  }

  private val component = ScalaComponent.builder[DefaultProps]("about")
    .initialState(State())
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

  private class Renderer(pageProps: PageProps, state: State)(implicit context: Context) {

    def render(): VdomElement = {
      UiPage(
        pageProps,
        Seq(
          UiAnalysisMenu(pageProps),
          UiSidebarFooter(pageProps)
        ),
        content()
      )
    }

    private def content(): VdomElement = {
      if (nlsNL) {
        nl()
      }
      else {
        en()
      }
    }

    private def nl(): VdomElement = {

      <.div(
        <.h1("Introductie"),
        UiPageContents(
          <.p(
            "Deze web-site bevat een analyse van de wandel- en fietsknooppuntnetwerken in ",
            <.a(
              ^.cls := "external",
              ^.href := "https://openstreetmap.org",
              ^.target := "_blank",
              "OpenStreetMap"
            ),
            "."
          ),
          <.p(
            """Het is de bedoeling om de knooppuntnetwerk definities meer toegankelijk te maken, en om een
          aantal controles uit te voeren om na te gaan of de netwerken volgens de regels van de kunst
          zijn aangemaakt. We hopen dat deze analyse kan helpen bij het mappen van de netwerken, en bij
          het bewaken en verbeteren van de kwaliteit van de netwerk definities."""
          ),
          <.p("De web-site werd opgebouwd met behulp van:"),
          <.ul(
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.scala-lang.org/",
                ^.target := "_blank",
                "Scala"
              ),
              " programeer taal voor de analyse logica"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://wiki.openstreetmap.org/wiki/Overpass_API",
                ^.target := "_blank",
                "OverpassAPI"
              ),
              " voor het verzamelen van de OpenStreetMap informatie"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://couchdb.apache.org/",
                ^.target := "_blank",
                "Couchdb"
              ),
              " document database voor het opslaan van de analyse resultaten"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.playframework.com/",
                ^.target := "_blank",
                "Play"
              ),
              " voor het bouwen van de web-site"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://openlayers.org/",
                ^.target := "_blank",
                "OpenLayers"
              ),
              " voor het tonen van kaarten"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.cs.cmu.edu/~quake/triangle.html",
                ^.target := "_blank",
                "Triangle"
              ),
              " voor de berekening van netwerk omtrekken"
            )
          )
        )
      )
    }

    private def en(): VdomElement = {
      <.div(
        <.h1("About"),
        UiPageContents(
          <.p(
            "This node network analyzer performs an analysis of the hiking and bicycle node networks in ",
            <.a(
              ^.cls := "external",
              ^.href := "https://openstreetmap.org",
              ^.target := "_blank",
              "OpenStreetMap"
            ),
            "."
          ),
          <.p(
            """The intention is to make it easier to look at node network definitions, and to check the network definitions
      against a number of validation rules. The hope is that this analyzer can assist in guarding and improving the
      quality of the networks."""
          ),
          <.p(
            "The information in the analysis is refreshed overnight. The time of the last refresh is shown after the ",
            <.i(""""Situation on""""),
            " label in the overview page."
          ),
          <.p("The analyzer uses:"),
          <.ul(
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.scala-lang.org/",
                ^.target := "_blank",
                "Scala"
              ),
              " programming language for analysis logic"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://wiki.openstreetmap.org/wiki/Overpass_API",
                ^.target := "_blank",
                "OverpassAPI"
              ),
              " for collecting OpenStreetMap information"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://couchdb.apache.org/",
                ^.target := "_blank",
                "Couchdb"
              ),
              " document database for storing and serving analysis results"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.playframework.com/",
                ^.target := "_blank",
                "Play"
              ),
              " framework for building the web-site"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://openlayers.org/",
                ^.target := "_blank",
                "OpenLayers"
              ),
              " for map display"
            ),
            <.li(
              <.a(
                ^.cls := "external",
                ^.href := "https://www.cs.cmu.edu/~quake/triangle.html",
                ^.target := "_blank",
                "Triangle"
              ),
              " for network boundary polygon calculation"
            )
          )
        )
      )
    }
  }

}
