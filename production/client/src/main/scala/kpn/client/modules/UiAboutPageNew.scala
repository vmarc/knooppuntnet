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
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContents
import kpn.client.services.AjaxClient
import org.scalajs.dom

object UiAboutPageNew {

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

    def retrieve(props: DefaultProps): Unit = {

      scope.modState(s => s.copy(pageState = s.pageState.loadStarting())).runNow()

      def updatePageStatus(status: PageStatus.Value): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.withStatus(status))).runNow()
      }

      def updateResult(request: dom.XMLHttpRequest): Unit = {
        scope.modState(s => s.copy(pageState = s.pageState.copy(ui = s.pageState.ui.copy(status = PageStatus.Ready)), page = Some(request))).runNow()
      }

      AjaxClient.getResponse("test.html", updateResult)
    }
  }

  private val component = ScalaComponent.builder[DefaultProps]("about")
    .initialState(State())
    .renderBackend[Backend]
    .componentWillReceiveProps { scope =>
      Callback {
        scope.backend.retrieve(scope.nextProps)
      }
    }
    .componentDidMount { scope =>
      Callback {
        scope.backend.installResizeListener()
        scope.backend.retrieve(scope.props)
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
        sideBar(),
        content()
      )
    }

    private def sideBar(): Seq[VdomElement] = Seq(<.div("SIDEBAR"))

    private def content(): VdomElement = {
      if (nlsNL) {
        nl()
      }
      else {
        en()
      }
    }

    private def nl(): VdomElement = {

      val text = state.pageState.ui.status match {
        case (PageStatus.Ready) =>
          state.page match {
            case Some(request) => request.responseText
            case None => "NO RESPONSE"
          }
        case _ => "LOADING"
      }

      <.div(
        //        <.h1("Introductie"),
        //        <.div(^.dangerouslySetInnerHtml := """Open <a href="/docs/nl/test.html#_create_the_angular_module" target="knooppuntnet-docs">documentation</a> in separate window."""),
        //        <.div(^.dangerouslySetInnerHtml :=
        //          """This tries to be a link to the glossary entry on <a href="../glossary#orphan-node" >orphan-node</a>, but does not work?"""),
        //        <.div(^.dangerouslySetInnerHtml :=
        //          """
        //            |Javascript: <a href="javascript:docLink('test-anchor');">link</a>.<br/>
        //            |Link on same page: <a href=#anchor-1>werkt niet?</a><br/>
        //            |text<br/>
        //            |text<br/>
        //            |text<br/>
        //            |text<br/>
        //            |text<br/>
        //            |text<br/>
        //            |<a id=anchor-1></a>Hier staat het anchor<br/>
        //            |text<br/>
        //            |text<br/>
        //            |text<br/>
        //            |
        //            |""".stripMargin
        //        ),
        //        <.div(
        //          "Inner html anchor: ",
        //          context.gotoGlossaryEntry("test-anchor", "test-anchor")
        //        ),
        //        <.div(
        //          "Deze werkt wel: ",
        //          context.gotoGlossaryEntry("orphan-node", "Knooppuntwees")
        //        ),
        <.div(^.dangerouslySetInnerHtml := text) // ,
        //        <.p(
        //          "Deze web-site bevat een analyse van de wandel- en fietsknooppuntnetwerken in ",
        //          <.a(
        //            ^.cls := "external",
        //            ^.href := "https://openstreetmap.org",
        //            "OpenStreetMap"
        //          ),
        //          "."
        //        ),
        //        <.p(
        //          """Het is de bedoeling om de knooppuntnetwerk definities meer toegankelijk te maken, en om een
        //          aantal controles uit te voeren om na te gaan of de netwerken volgens de regels van de kunst
        //          zijn aangemaakt. We hopen dat deze analyse kan helpen bij het mappen van de netwerken, en bij
        //          het bewaken en verbeteren van de kwaliteit van de netwerk definities."""
        //        ),
        //        <.p(
        //          """De informatie van de analyse wordt elke nacht ververst. Het tijdstip waarop de
        //          informatie de laatste keer is ververst wordt getoond bij het kopje """,
        //          <.i(""""Situatie op""""),
        //          "op de ",
        //          <.a(^.href := "@routes.Analyzer.overview(context.language)", "overzicht"),
        //          " pagina."
        //        ),
        //        <.p("De web-site werd opgebouwd met behulp van:"),
        //        <.ul(
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://www.scala-lang.org/",
        //              "Scala"
        //            ),
        //            " programeer taal voor de analyse logica"
        //          ),
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://wiki.openstreetmap.org/wiki/Overpass_API",
        //              "OverpassAPI"
        //            ),
        //            " voor het verzamelen van de OpenStreetMap informatie"
        //          ),
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://couchdb.apache.org/",
        //              "Couchdb"
        //            ),
        //            " document database voor het opslaan van de analyse resultaten"
        //          ),
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://www.playframework.com/",
        //              "Play"
        //            ),
        //            " voor het bouwen van de web-site"
        //          ),
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://openlayers.org/",
        //              "OpenLayers"
        //            ),
        //            " voor het tonen van kaarten"
        //          ),
        //          <.li(
        //            <.a(
        //              ^.cls := "external",
        //              ^.href := "https://www.cs.cmu.edu/~quake/triangle.html",
        //              "Triangle"
        //            ),
        //            " voor de berekening van netwerk omtrekken"
        //          )
        //        )
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
