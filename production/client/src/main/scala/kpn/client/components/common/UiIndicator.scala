// TODO migrate to Angular
package kpn.client.components.common

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.MuiDialog
import chandu0101.scalajs.react.components.materialui.MuiFlatButton
import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import scalacss.ScalaCssReact._

object UiIndicator {

  object Color extends Enumeration {
    val RED, GREEN, BLUE, ORANGE, GRAY = Value
  }

  private val avatarHeight = 25

  object Styles extends StyleSheet.Inline {

    import dsl._

    val title: StyleA = style(
      lineHeight(avatarHeight.px),
      verticalAlign.middle,
      paddingLeft(10.px),
      fontWeight.bold
    )

    val indicatorBox: StyleA = style(
      display.inlineBlock,
      paddingLeft(5.px),
      paddingRight(5.px),
      cursor.help
    )

    val dialogAndIndicator: StyleA = style(
      display.inlineBlock,
      cursor.help
    )

    val indicator: StyleA = style(
      color.white,
      display.inlineFlex,
      alignItems.center,
      justifyContent.center,
      fontSize(12.px),
      borderRadius(50.%%),
      height(avatarHeight.px),
      width(avatarHeight.px)
    )

    val red: StyleA = style(
      mixin(indicator),
      backgroundColor(rgb(239, 83, 80))
    )

    val green: StyleA = style(
      mixin(indicator),
      backgroundColor(rgb(102, 187, 106))
    )

    val blue: StyleA = style(
      mixin(indicator),
      backgroundColor(rgb(33, 150, 243))
    )

    val orange: StyleA = style(
      mixin(indicator),
      backgroundColor(rgb(255, 163, 0))
    )

    val gray: StyleA = style(
      mixin(indicator),
      backgroundColor(rgb(224, 224, 224))
    )
  }

  private case class Props(context: Context, letter: String, color: Color.Value, title: String, description: String)

  private case class State(isOpen: Boolean)

  private class Backend(scope: BackendScope[Props, State]) {

    private val openDescription = scope.setState(State(true))
    private val closeDescription1 = (event: TouchTapEvent) => {
      event.preventDefault()
      scope.setState(State(false))
    }
    private val closeDescription2 = (open: Boolean) => scope.setState(State(false))

    def render(props: Props, state: State): VdomElement = {

      implicit val context: Context = props.context

      if (!state.isOpen) {
        indicator(props.color, props.letter)
      }
      else {
        <.div(
          Styles.dialogAndIndicator,
          TagMod.when(state.isOpen) {
            MuiDialog(
              actions = MuiFlatButton(key = "1", label = nls("Close", "Sluiten"), secondary = true, onTouchTap = closeDescription1)(),
              open = true,
              onRequestClose = closeDescription2
            )(
              <.div(
                indicator(props.color, props.letter),
                <.span(
                  Styles.title,
                  props.title
                ),
                UiMarked(props.description)
              )
            )
          },
          indicator(props.color, props.letter)
        )
      }
    }

    private def indicator(color: Color.Value, letter: String): VdomElement = {
      val style = color match {
        case Color.RED => Styles.red
        case Color.GREEN => Styles.green
        case Color.BLUE => Styles.blue
        case Color.ORANGE => Styles.orange
        case _ => Styles.gray
      }

      <.div(
        Styles.indicatorBox,
        ^.onClick --> openDescription,
        <.div(
          style,
          letter
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("indicator")
    .initialState(State(false))
    .renderBackend[Backend]
    .build

  def apply(letter: String, color: Color.Value, title: String, description: String)(implicit context: Context): VdomElement = {
    component(Props(context, letter, color, title, description))
  }

}
