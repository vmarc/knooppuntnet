// Migrated to Angular: tag-diffs-table.component.ts
package kpn.client.components.common

import chandu0101.scalajs.react.components.materialui.Mui
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.home.UiIcon
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs

import scalacss.ScalaCssReact._

object UiTagDiffsTable {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val gray: StyleA = style(
      color.lightgray
    )

    val neutral: StyleA = style(
    )

    val title: StyleA = style(
      marginTop(2.px),
      marginBottom(4.px)
    )
  }

  private case class Props(context: Context, tagDiffs: TagDiffs)

  private val component = ScalaComponent.builder[Props]("tag-diffs-table")
    .render_P { props =>
      new Renderer(props.tagDiffs)(props.context).render()
    }
    .build

  def apply(tagDiffs: TagDiffs)(implicit context: Context): VdomElement = component(Props(context, tagDiffs))

  private class Renderer(tagDiffs: TagDiffs)(implicit val context: Context) {

    def render(): VdomElement = {
      <.div(
        <.div(
          Styles.title,
          nls("Tag changes", "Label wijzigingen"),
          ":"
        ),
        <.table(
          ^.title := "tag differences",
          <.thead(
            <.tr(
              <.th(""),
              <.th(nls("Key", "Sleutel")),
              <.th(nls("Before", "Voor")),
              <.th(nls("After", "Na"))
            )
          ),
          <.tbody(
            (mainTags() ++ separator() ++ extraTags()).toTagMod
          )
        )
      )
    }

    private def mainTags(): Seq[VdomElement] = tagDetails(tagDiffs.mainTags)

    private def extraTags(): Seq[VdomElement] = tagDetails(tagDiffs.extraTags)

    private def tagDetails(details: Seq[TagDetail]): Seq[VdomElement] = {
      details.map { detail =>
        detail.action match {
          case TagDetailType.Same => tagDetail(detail, Styles.gray)
          case TagDetailType.Delete => tagDetail(detail, Styles.gray)
          case TagDetailType.Update => tagDetail(detail, Styles.neutral)
          case TagDetailType.Add => tagDetail(detail, Styles.neutral)
        }
      }
    }

    private def tagDetail(detail: TagDetail, style: StyleA): VdomElement = {
      <.tr(
        style,
        <.td(action(detail.action)),
        <.td(detail.key),
        <.td(detail.valueBefore.whenDefined),
        <.td(detail.valueAfter.whenDefined)
      )
    }

    private def action(tagAction: TagDetailType): TagMod = {
      tagAction match {
        case TagDetailType.Same => TagMod()
        case TagDetailType.Delete => UiIcon(Mui.SvgIcons.ContentClear, Mui.Styles.colors.grey600)
        case TagDetailType.Update => UiIcon(Mui.SvgIcons.AvReplay, Mui.Styles.colors.grey600)
        case TagDetailType.Add => UiIcon(Mui.SvgIcons.ContentAdd, Mui.Styles.colors.grey600)
      }
    }

    private def separator(): Seq[VdomElement] = {
      if (tagDiffs.mainTags.nonEmpty && tagDiffs.extraTags.nonEmpty) {
        Seq(
          <.tr(
            <.td(^.colSpan := 4)
          )
        )
      }
      else {
        Seq()
      }
    }
  }

}
