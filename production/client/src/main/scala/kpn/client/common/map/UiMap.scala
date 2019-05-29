// TODO migrate to Angular
package kpn.client.common.map

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiAppBar
import kpn.client.components.common.UiPage

import scalacss.ScalaCssReact._

object UiMap {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val map: StyleA = style(
      position.absolute,
      borderColor.lightgray,
      borderStyle.solid,
      borderWidth(1.px)
    )

    val small: StyleA = style(
      top((UiAppBar.appBarHeight + 45).px), // appbar + title
      left(0.px),
      right(0.px),
      bottom(0.px)
    )

    val small2: StyleA = style(
      top(UiAppBar.appBarHeight.px), // no page title
      left(0.px),
      right(0.px),
      bottom(0.px)
    )

    val medium: StyleA = style(
      top((UiAppBar.appBarHeight + 60).px),
      left(UiPage.sideBarWidth.px),
      right(0.px),
      bottom(0.px)
    )

    val medium2: StyleA = style(
      top(UiAppBar.appBarHeight.px), // no page title
      left(UiPage.sideBarWidth.px),
      right(0.px),
      bottom(0.px)
    )

    val veryLarge: StyleA = style(
      right(UiPage.contentsMargin.px),
      top((UiAppBar.appBarHeight + UiPage.contentsMargin).px),
      width(640.px),
      height(400.px)
    )

    val switcher: StyleA = style(
      position.absolute,
      top(10.px),
      right(10.px),
      zIndex(100),
      backgroundColor.white,
      padding(5.px),
      borderColor.lightgray,
      borderStyle.solid,
      borderWidth(1.px),
      borderRadius(5.px)
    )

    val switcherPanel: StyleA = style(
      width(180.px)
    )

    val newSmallMap: StyleA = style(
      position.relative,
      borderColor.lightgray,
      borderStyle.solid,
      borderWidth(1.px),
      height(320.px),
      left(0.px),
      right(20.px),
      maxWidth(640.px)
    )
  }

  private case class Props(style: StyleA, map: MapDefinition)

  private val component = ScalaComponent.builder[Props]("map")
    .render_P { props =>
      <.div(
        props.style,
        ^.id := props.map.targetElementId,
        TagMod.when(props.map.layers.nonEmpty) {
          <.div(
            UiMap.Styles.switcher,
            LayerSwitcher(props.map.layers)
          )
        }
      )
    }
    .componentDidMount { scope =>
      Callback {
        scope.props.map.onMounted()
      }
    }
    .build

  def apply(style: StyleA, map: MapDefinition): VdomElement = component(Props(style, map))
}
