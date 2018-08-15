package kpn.client.components.common

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiAppBar
import chandu0101.scalajs.react.components.materialui.MuiFlatButton
import chandu0101.scalajs.react.components.materialui.MuiIconButton
import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactMouseEvent
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.RouteConfiguration.GotoHome
import kpn.client.UnsafeStyles
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.home.UiIcon

import scala.scalajs.js
import scalacss.ScalaCssReact._

object UiAppBar {

  val appBarHeight = 48

  object Styles extends StyleSheet.Inline {

    import dsl._

    val title: StyleA = style(
      fontSize(24.px),
      color(UnsafeStyles.textColor),
      cursor.pointer
    )

    val buttons: StyleA = style(
      display.inlineBlock,
      height(appBarHeight.px)
    )

    val languageLinks: StyleA = style(
      display.inlineBlock,
      lineHeight(appBarHeight.px),
      verticalAlign.middle,
      paddingLeft(15.px),
      paddingRight(15.px)
    )

    val languageDivider: StyleA = style(
      borderLeftStyle.solid,
      borderLeftColor.lightgray,
      borderLeftWidth(1.px),
      marginLeft(5.px),
      marginRight(5.px)
    )
  }

  private case class Props(pageProps: PageProps)

  private val component = ScalaComponent.builder[Props]("app-bar")
    .render_P { props =>
      new Renderer(props.pageProps).render()
    }
    .build

  def apply(pageProps: PageProps): VdomElement = component(Props(pageProps))

  private class Renderer(pageProps: PageProps) {

    private implicit val context: Context = pageProps.context

    def render(): VdomElement = {

      def gotoHome(e: ReactMouseEvent): Callback = {
        context.tempGetRouter.setEH(GotoHome(context.lang))(e)
      }

      val title = <.span(
        <.span(
          ^.onClick ==> gotoHome,
          Styles.title,
          "knooppuntnet"
        ),
        <.span("")
      )

      MuiAppBar(
        title = title,
        onLeftIconButtonTouchTap = (event: TouchTapEvent) => {
          pageProps.openSideBar
        },
        showMenuIconButton = isSmall,
        iconElementRight = rightButtons()
      )()
    }

    private def rightButtons(): VdomElement = {
      <.div(
        Styles.buttons,
        filterButton(),
        listButton(),
        mapButton(),
        languageLinks()
      )
    }

    private def filterButton(): TagMod = {
      val icon: VdomNode = UiFilterIcon()
      button(pageProps.hasFilter && isSmall, selected = false, icon, "Filter", pageProps.openFilterSideBar)
    }

    private def listButton(): TagMod = {
      val icon: VdomNode = UiIcon(Mui.SvgIcons.EditorFormatListBulleted)
      button(pageProps.hasMapButton, !pageProps.ui.isMapShown, icon, nls("List", "Lijst"), pageProps.showList)
    }

    private def mapButton(): TagMod = {
      val icon: VdomNode = UiIcon(Mui.SvgIcons.CommunicationLocationOn)
      button(pageProps.hasMapButton, pageProps.ui.isMapShown, icon, nls("Map", "Kaart"), pageProps.showMap)
    }

    private def button(isShown: Boolean, selected: Boolean, buttonIcon: VdomNode, title: String, action: Callback): TagMod = {

      val buttonStyle = if (selected) {
        js.Dynamic.literal(
          backgroundColor = "#f0f0f0"
        )
      }
      else {
        js.Dynamic.literal(
        )
      }

      if (isShown && isSmall) {
        MuiIconButton(
          style = buttonStyle,
          tooltip = title,
          onTouchTap = (event: TouchTapEvent) => {
            event.preventDefault()
            action
          }
        )(buttonIcon)
      }
      else if (isShown && !isSmall) {
        MuiFlatButton(
          style = buttonStyle,
          label = title,
          icon = buttonIcon,
          onTouchTap = (event: TouchTapEvent) => {
            event.preventDefault()
            action
          }
        )()
      }
      else {
        TagMod()
      }
    }

    private def languageLinks(): TagMod = {
      TagMod.when(!isSmall) {
        <.div(
          Styles.languageLinks,
          context.gotoEn(),
          <.span(Styles.languageDivider),
          context.gotoNl()
        )
      }
    }

    private def isSmall: Boolean = pageProps.ui.isSmall

    private def isMedium: Boolean = pageProps.ui.isMedium

  }

}
