// TODO migrate to Angular
package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.Mui
import chandu0101.scalajs.react.components.materialui.MuiDivider
import chandu0101.scalajs.react.components.materialui.MuiDrawer
import chandu0101.scalajs.react.components.materialui.MuiMenuItem
import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.home.UiIcon

import scalacss.ScalaCssReact._

object UiPage {

  val sideBarWidth = 250
  val smallContentsMargin = 10
  val contentsMargin = 20

  object Styles extends StyleSheet.Inline {

    import dsl._

    val appBar: StyleA = style(
      position.fixed,
      top(0.px),
      width(100.%%),
      zIndex(1100)
    )

    val sideBar: StyleA = style(
      position.fixed,
      top(UiAppBar.appBarHeight.px),
      bottom(0.px),
      width(sideBarWidth.px),
      borderRightColor.lightgray,
      borderRightStyle.solid,
      borderRightWidth(1.px),
      overflowY.scroll,
      overflowX.hidden
    )

    val contents: StyleA = style(
      paddingTop(UiAppBar.appBarHeight.px),
      paddingLeft((sideBarWidth + contentsMargin).px),
      paddingRight(contentsMargin.px),
      paddingBottom(50.px)
    )

    val contentsSmall: StyleA = style(
      paddingTop(UiAppBar.appBarHeight.px),
      paddingLeft(smallContentsMargin.px),
      paddingRight(smallContentsMargin.px),
      paddingBottom(30.px)
    )

    val dimmer: StyleA = style(
      position.absolute,
      display.block,
      top(0.px),
      bottom(0.px),
      left(0.px),
      right(0.px),
      zIndex(1150),
      backgroundColor(rgba(0, 0, 0, 0.3))
    )

    val clickToCloseSideBar: StyleA = style(
      position.absolute,
      display.block,
      top(0.px),
      bottom(0.px),
      left(sideBarWidth.px),
      right(0.px),
      zIndex(1160)
    )
  }

  private case class Props(props: PageProps, sideBar: Seq[VdomElement], contents: TagMod)

  private class Backend(scope: BackendScope[Props, Unit]) {

    var sideBarOpen = false

    def render(props: Props): VdomElement = {
      val sidebarChange = props.props.ui.isSideBarOpen != sideBarOpen
      sideBarOpen = props.props.ui.isSideBarOpen
      new Renderer(props, sidebarChange).render()
    }
  }

  private val component = ScalaComponent.builder[Props]("page")
    .renderBackend[Backend]
    .build

  def apply[T](props: PageProps, sideBar: Seq[VdomElement], contents: TagMod): VdomElement = {
    component(Props(props, sideBar, contents))
  }

  private class Renderer[T](props: Props, sidebarChange: Boolean) {

    private implicit val context: Context = props.props.context

    def render(): VdomElement = {
      if (isSmall) {
        renderSmall()
      }
      else {
        renderMediumAndLarge()
      }
    }

    private def renderSmall(): VdomElement = {

      val children = Seq[VdomNode](
        backButton(),
        MuiDivider(
          key = "divider"
        )()
      ) ++ props.sideBar

      <.div(
        UiSpinner(props.props.ui.status == PageStatus.Loading || props.props.ui.status == PageStatus.Updating),
        <.div(
          Styles.appBar,
          UiAppBar(props.props)
        ),
        <.div(
          Styles.contentsSmall,
          props.contents
        ),
        MuiDrawer(
          key = "sidebar",
          width = sideBarWidth.toDouble,
          open = props.props.ui.isSideBarOpen
        )(
          children: _*
        ),
        dimmer(),
        clickToCloseSideBar()
      )
    }

    private def backButton(): VdomElement = {

      val title: VdomNode = nls("back", "terug")

      MuiMenuItem[String](
        key = "back",
        primaryText = title,
        leftIcon = UiIcon(Mui.SvgIcons.NavigationArrowBack),
        onTouchTap = (event: TouchTapEvent) => {
          props.props.closeSideBar
        }
      )()
    }

    private def renderMediumAndLarge(): VdomElement = {
      <.div(
        UiSpinner(props.props.ui.status == PageStatus.Loading || props.props.ui.status == PageStatus.Updating),
        <.div(
          Styles.appBar,
          UiAppBar(props.props)
        ),
        <.div(
          Styles.sideBar,
          <.div(
            props.sideBar.toTagMod
          )
        ),
        <.div(
          Styles.contents,
          props.contents
        )
      )
    }

    private def clickToCloseSideBar(): TagMod = {
      TagMod.when(isSideBarOpen) {
        <.div(
          Styles.clickToCloseSideBar,
          ^.onClick --> props.props.closeSideBar
        )
      }
    }

    private def isSmall: Boolean = props.props.ui.isSmall

    private def dimmer(): TagMod = {
      TagMod.when(isSideBarOpen) {
        <.div(
          Styles.dimmer
        )
      }
    }

    private def isSideBarOpen: Boolean = props.props.ui.isSideBarOpen

  }

}
