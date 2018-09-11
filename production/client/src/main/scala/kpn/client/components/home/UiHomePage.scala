package kpn.client.components.home

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsEN
import kpn.client.common.Nls.nlsNL
import kpn.client.common.User
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.DefaultBackend
import kpn.client.components.common.DefaultProps
import kpn.client.components.common.PageState
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContents
import kpn.shared.NetworkType
import kpn.shared.Subset
import scalacss.ScalaCssReact._

object UiHomePage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val items: StyleA = style(
      marginTop(20.px),
      borderTopColor.lightgray,
      borderTopStyle.solid,
      borderTopWidth(1.px),
      media.maxWidth(PageWidth.SmallMaxWidth.px)(
        marginLeft((-UiPage.smallContentsMargin).px),
        marginRight((-UiPage.smallContentsMargin).px)
      )
    )

    val item: StyleA = style(
      paddingTop(10.px),
      paddingBottom(10.px),
      paddingLeft(10.px),
      paddingRight(20.px),
      borderBottomColor.lightgray,
      borderBottomStyle.solid,
      borderBottomWidth(1.px)
    )

    val cardTitle: StyleA = style(
      unsafeChild("a")(
        paddingLeft(12.px),
        fontSize(18.px)
      ),
      height(32.px),
      display.flex,
      alignItems.center
    )

    val cardContents: StyleA = style(
    )
  }

  private class Backend(scope: BackendScope[DefaultProps, PageState[Unit]]) extends DefaultBackend(scope) {

    protected def contents(props: DefaultProps, state: PageState[Unit]): VdomElement = {
      implicit val context: Context = props.context
      new Renderer().render()
    }

    protected def title: String = nls("Node networks", "Knooppunt netwerken")
  }

  private val component = ScalaComponent.builder[DefaultProps]("home")
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

  private class Renderer()(implicit context: Context) {
    def render(): VdomElement = {
      <.div(
        <.h1(nls("Node networks", "Knooppunt netwerken")),
        UiPageContents(
          TagMod.when(nlsEN) {
            <.p(
              "This is an analysis of the node networks for walking and cycling in The Netherlands, " +
                "Belgium and Germany, as defined in ",
              <.a(
                ^.href := "https://www.openstreetmap.org",
                ^.cls := "external",
                ^.target := "_blank",
                "OpenStreetMap"
              ),
              "."
            )
          },
          TagMod.when(nlsEN) {
            <.p(
              "This website is the successor of osma.vmarc.be for which the contents is still available ",
              <.a(
                ^.href := "http://old.vmarc.be",
                ^.cls := "external",
                ^.target := "_blank",
                "here"
              ),
              " for a limited period."
            )
          },
          TagMod.when(nlsNL) {
            <.p(
              "Dit is een analyse van de knooppuntnetwerken voor wandelaars en fieters in Nederland, België en Duitsland, " +
                "zoals deze zijn opgenomen in ",
              <.a(
                ^.href := "https://www.openstreetmap.org",
                ^.cls := "external",
                ^.target := "_blank",
                "OpenStreetMap"
              ),
              "."
            )
          },
          TagMod.when(nlsNL) {
            <.p(
              "Deze website is de opvolger van osma.vmarc.be waarvan de inhoud tijdelijk ",
              <.a(
                ^.href := "http://old.vmarc.be",
                ^.cls := "external",
                ^.target := "_blank",
                "hier"
              ),
              " nog te raadplegen is."
            )
          },
          <.div(
            Styles.items,
            maps(),
            changes(),
            subsets(),
            statistics(),
            loginLogout()
          )
        )
      )
    }

    private def maps(): VdomElement = {
      <.div(
        Styles.item,
        NetworkType.all.toTagMod(mapCard)
      )
    }

    private def mapCard(networkType: NetworkType): VdomElement = {

      val title = networkType match {
        case NetworkType.hiking => nls("Hiking Map", "Wandelkaart")
        case NetworkType.bicycle => nls("Cycling Map", "Fietskaart")
        case NetworkType.horse => nls("Horse Map", "Ruiterkaart")
        case NetworkType.motorboat => nls("Motorboat Map", "Motorbootkaart")
        case NetworkType.canoe => nls("Canoe Map", "Kanokaart")
      }

      <.div(
        Styles.cardTitle,
        UiNetworkTypeIcon(networkType),
        " ",
        context.gotoMap(networkType, title)
      )
    }

    private def changes(): VdomElement = {
      <.div(
        Styles.item,
        <.div(
          Styles.cardTitle,
          UiChangesIcon(),
          " ",
          context.gotoChanges()
        ),
        <.div(
          Styles.cardContents,
          <.p(
            nls(
              "Look at the most recent changes to the node networks.",
              "Bekijk de meest recent wijzigingen aan de knooppuntnetwerken."
            )
          )
        )
      )
    }

    private def statistics(): VdomElement = {
      <.div(
        Styles.item,
        <.div(
          Styles.cardTitle,
          UiStatisticsIcon(),
          " ",
          context.gotoOverview()
        )
      )
    }

    private def loginLogout(): VdomElement = {
      <.div(
        Styles.item,
        <.div(
          Styles.cardTitle,
          User.get match {
            case Some(user) => context.gotoLogout()
            case None => context.gotoLogin()
          }
        )
      )
    }

    private def subsets(): VdomElement = {
      <.div(
        Styles.item,
        Subset.used.toTagMod(subsetCard)
      )
    }

    private def subsetCard(subset: Subset): VdomElement = {

      val networkTypeString = subset.networkType match {
        case NetworkType.hiking => nls("Hiking", "Wandelen")
        case NetworkType.bicycle => nls("Cycling", "Fietsen")
        case NetworkType.horse => nls("Horse", "Ruiter")
        case NetworkType.motorboat => nls("Motorboat", "Motorboot")
        case NetworkType.canoe => nls("Canoe", "Kano")
      }

      val countryString = Nls.country(Some(subset.country))

      val titleText = networkTypeString + " in " + countryString

      <.div(
        Styles.cardTitle,
        UiNetworkTypeIcon(subset.networkType),
        " ",
        context.gotoSubsetNetworks(subset, Some(titleText))
      )
    }
  }

}
