// Migrated to Angular: filter.component.ts
package kpn.client.components.filter

import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiPage
import kpn.client.filter.FilterOptionGroup
import kpn.client.filter.FilterOptions

import scalacss.ScalaCssReact._

object UiFilter {

  private val paddingHorizontal = 15
  private val filterWidth = UiPage.sideBarWidth - (2 * paddingHorizontal)

  private val filterRowWidth = 210
  private val filterOptionNameWidth = 140

  object Styles extends StyleSheet.Inline {

    import dsl._

    val filter: StyleA = style(
      width(filterWidth.px),
      paddingTop(10.px),
      paddingBottom(15.px),
      paddingLeft(paddingHorizontal.px),
      paddingRight(paddingHorizontal.px),
      borderTopWidth(1.px),
      borderTopStyle.solid,
      borderTopColor.lightgray
    )

    val title: StyleA = style(
      display.inlineBlock,
      fontWeight.bold,
      paddingBottom(10.px)
    )

    val total: StyleA = style(
      display.inlineBlock,
      paddingRight(10.px),
      fontWeight.normal,
      float.right
    )

    val groupTitle: StyleA = style(
      paddingTop(15.px)
    )

    val row: StyleA = style(
      display.block,
      paddingTop(5.px),
      minHeight(17.px),
      width(filterRowWidth.px),
      unsafeChild("i")(
        verticalAlign.top
      )
    )

    val optionName: StyleA = style(
      paddingLeft(5.px),
      display.inlineBlock,
      width(filterOptionNameWidth.px),
      maxWidth(filterOptionNameWidth.px),
      wordWrap.breakWord
    )

    val optionCount: StyleA = style(
      float.right,
      display.inlineBlock
    )

    val countLinks: StyleA = style(
      paddingRight(10.px),
      float.right,
      display.inlineBlock
    )

    val year: StyleA = style(
      display.inlineBlock
    )

    val month: StyleA = style(
      display.inlineBlock,
      paddingLeft(20.px)
    )

    val day: StyleA = style(
      display.inlineBlock,
      paddingLeft(40.px)
    )

    val link: StyleA = style(
      cursor.pointer
    )
  }

  class FilterRenderer(filterOptions: FilterOptions) {

    def render(): VdomElement = {
      <.div(
        Styles.filter,
        UiFilterTitle(filterOptions),
        options()
      )
    }

    private def options(): TagMod = {
      filterOptions.groups.toTagMod { group =>
        optionGroup(group)
      }
    }

    private def optionGroup(group: FilterOptionGroup): TagMod = {
      if (group.name == "user" || group.name == "role") {
        UiFilterCheckboxGroup(group)
      }
      else {
        UiFilterRadioGroup(group)
      }
    }
  }

}
