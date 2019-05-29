// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs

import scalacss.ScalaCssReact._

object UiTagDiffsText {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val title: StyleA = style(
      color.gray
    )

    val importantTitle: StyleA = style(
    )

    val tagDetail: StyleA = style(
      marginTop(3.px),
      marginBottom(10.px),
      borderLeftStyle.dotted,
      borderLeftWidth(1.px),
      borderLeftColor.gray
    )

    val label: StyleA = style(
      display.inlineBlock,
      color.gray,
      marginLeft(5.px),
      width(60.px)
    )

    val importantValue: StyleA = style(
      display.inlineBlock
    )

    val value: StyleA = style(
      display.inlineBlock,
      color.gray
    )
  }

  private case class Props(context: Context, tagDiffs: TagDiffs)

  private val component = ScalaComponent.builder[Props]("tag-diffs-text")
    .render_P { props =>
      new Renderer(props.tagDiffs)(props.context).render()
    }
    .build

  def apply(tagDiffs: TagDiffs)(implicit context: Context): VdomElement = component(Props(context, tagDiffs))

  private class Renderer(tagDiffs: TagDiffs)(implicit val context: Context) {

    def render(): VdomElement = {
      <.div(
        deletedTags,
        addedTags,
        updatedTags,
        sameTags
      )
    }

    private def deletedTags = {
      tags(deletedTagDetails, "Deleted tag", "Verwijderde label", Styles.importantTitle) { tagDetail: TagDetail =>
        <.div(
          Styles.tagDetail,
          key(tagDetail, Styles.importantValue),
          value(tagDetail.valueBefore.get, Styles.importantValue)
        )
      }
    }

    private def addedTags = {
      tags(addedTagDetails, "Added tag", "Toegevoegde label", Styles.importantTitle) { tagDetail: TagDetail =>
        <.div(
          Styles.tagDetail,
          key(tagDetail, Styles.importantValue),
          value(tagDetail.valueAfter.get, Styles.importantValue)
        )
      }
    }

    private def updatedTags = {
      tags(updatedTagDetails, "Updated tag", "Gewijzigde label", Styles.importantTitle) { tagDetail: TagDetail =>
        <.div(
          Styles.tagDetail,
          key(tagDetail, Styles.importantValue),
          before(tagDetail),
          after(tagDetail)
        )
      }
    }

    private def sameTags = {
      tags(sameTagDetails, "Unchanged tag", "Ongewijzigde label", Styles.title) { tagDetail: TagDetail =>
        <.div(
          Styles.tagDetail,
          key(tagDetail, Styles.value),
          value(tagDetail.valueAfter.get, Styles.value)
        )
      }
    }

    private def tags(tagDetails: Seq[TagDetail], en: String, nl: String, titleStyle: StyleA)(fn: (TagDetail) => TagMod): TagMod = {
      val titleText = (if (tagDetails.size == 1) nls(en, nl) else nls(en, nl) + "s") + ":"
      TagMod.when(tagDetails.nonEmpty) {
        <.div(
          <.div(
            titleStyle,
            titleText
          ),
          tagDetails.toTagMod(fn)
        )
      }
    }

    private def key(tagDetail: TagDetail, valueStyle: StyleA): TagMod = {
      tagDetailLine(nls("Key", "Sleutel"), tagDetail.key, valueStyle)
    }

    private def value(value: String, valueStyle: StyleA): TagMod = {
      tagDetailLine(nls("Value", "Waarde"), value, valueStyle)
    }

    private def before(tagDetail: TagDetail): TagMod = {
      tagDetailLine(nls("Before", "Voor"), tagDetail.valueBefore.get, Styles.value)
    }

    private def after(tagDetail: TagDetail): TagMod = {
      tagDetailLine(nls("After", "Na"), tagDetail.valueAfter.get, Styles.importantValue)
    }

    private def tagDetailLine(label: String, value: String, valueStyle: StyleA): TagMod = {
      <.div(
        <.div(
          Styles.label,
          label,
          ":"
        ),
        <.div(
          valueStyle,
          value
        )
      )
    }

    private def deletedTagDetails: Seq[TagDetail] = {
      allTagDetails.filter(_.action == TagDetailType.Delete)
    }

    private def addedTagDetails: Seq[TagDetail] = {
      allTagDetails.filter(_.action == TagDetailType.Add)
    }

    private def updatedTagDetails: Seq[TagDetail] = {
      allTagDetails.filter(_.action == TagDetailType.Update)
    }

    private def sameTagDetails: Seq[TagDetail] = {
      allTagDetails.filter(_.action == TagDetailType.Same)
    }

    private def allTagDetails: Seq[TagDetail] = {
      tagDiffs.mainTags ++ tagDiffs.extraTags
    }
  }

}
