// TODO migrate to Angular
package kpn.client.components.shared

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.shared.changes.details.ChangeKey

import scalacss.ScalaCssReact._

object UiChangeSet {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val block: StyleA = style(
      paddingTop(5.px),
      paddingBottom(10.px)
    )

    val blockContents: StyleA = style(
      paddingTop(3.px)
    )

    val comment: StyleA = style(
      paddingTop(5.px),
      paddingBottom(5.px),
      fontStyle.italic
    )

    val refBlock: StyleA = style(
      display.inlineBlock,
      marginTop(2.px),
      marginBottom(2.px),
      marginRight(4.px),
      height(20.px),
      padding(2.px),
      paddingRight(5.px),
      backgroundColor(rgb(250, 250, 250)),
      borderStyle.solid,
      borderWidth(1.px),
      borderRadius(3.px),
      verticalAlign.top
    )

    val ref: StyleA = style(
      display.inlineBlock,
      paddingTop(1.px),
      paddingLeft(4.px),
      paddingRight(2.px),
      verticalAlign.top
    )

    val icon: StyleA = style(
      display.inlineBlock,
      width(20.px),
      height(20.px)
    )

    val add: StyleA = icon + style(
      background := "url('/assets/images/add.svg') no-repeat"
    )

    val update: StyleA = icon + style(
      background := "url('/assets/images/update.svg') no-repeat"
    )

    val delete: StyleA = icon + style(
      background := "url('/assets/images/delete.svg') no-repeat"
    )

    val red: StyleA = style(
      borderColor.red
    )

    val gray: StyleA = style(
      borderColor.gray
    )

    val green: StyleA = style(
      borderColor(rgb(0, 225, 0))
    )

    val node: StyleA = icon + style(
      background := "url('/assets/images/node.svg') no-repeat",
      marginRight(2.px),
      marginLeft(2.px)
    )

    val route: StyleA = icon + style(
      background := "url('/assets/images/route.svg') no-repeat",
      marginRight(2.px),
      marginLeft(2.px)
    )
  }

  private case class Props(
    context: Context,
    key: ChangeKey,
    happy: Boolean,
    investigate: Boolean,
    comment: Option[String],
    contents: VdomElement
  )

  private val component = ScalaComponent.builder[Props]("change-set")
    .render_P { props =>

      val key = props.key
      implicit val context: Context = props.context

      <.div(
        TagMod.when(PageWidth.isSmall) {
          <.div(
            UiLine(
              UiThick(context.gotoChangeSet(key.changeSetId, key.replicationNumber)),
              TagMod.when(props.happy)(UiHappy()),
              TagMod.when(props.investigate)(UiInvestigate())
            ),
            <.div(
              UiThin(key.timestamp.yyyymmddhhmm)
            )
          )
        },
        TagMod.when(!PageWidth.isSmall) {
          UiLine(
            UiThick(context.gotoChangeSet(key.changeSetId, key.replicationNumber)),
            UiThin(key.timestamp.yyyymmddhhmm),
            TagMod.when(props.happy)(UiHappy()),
            TagMod.when(props.investigate)(UiInvestigate())
          )
        },
        TagMod.when(props.comment.isDefined) {
          <.div(
            Styles.comment,
            props.comment.get
          )
        },
        props.contents
      )
    }
    .build

  def apply(
    key: ChangeKey,
    happy: Boolean,
    investigate: Boolean,
    comment: Option[String],
    contents: VdomElement
  )(implicit context: Context): VdomElement = {
    component(
      Props(
        context,
        key,
        happy,
        investigate,
        comment,
        contents
      )
    )
  }
}
