package kpn.client.components.changes.filter

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.components.filter.UiFilter
import kpn.shared.changes.filter.ChangesFilterPeriod

import scalacss.ScalaCssReact._
import scalacss.StyleA

object UiChangeFilterPeriod {

  private case class Props(titleStyle: StyleA, title: String, period: ChangesFilterPeriod, impactedCountChanged: Callback, totalCountChanged: Callback)

  private val component = ScalaComponent.builder[Props]("changes-filter-period")
    .render_P { props =>
      val period = props.period
      val title = props.title + (if (period.current) "  *" else "")

      <.div(
        UiFilter.Styles.row,
        <.div(
          props.titleStyle,
          title
        ),
        <.div(
          UiFilter.Styles.countLinks,
          <.a(
            UiFilter.Styles.link,
            ^.onClick --> props.impactedCountChanged,
            "" + props.period.impactedCount
          ),
          " / ",
          <.a(
            UiFilter.Styles.link,
            ^.onClick --> props.totalCountChanged,
            "" + props.period.totalCount
          )
        )
      )
    }
    .build

  def apply(titleStyle: StyleA, title: String, period: ChangesFilterPeriod, impactedCountChanged: Callback, totalCountChanged: Callback): VdomElement = {
    component(Props(titleStyle, title, period, impactedCountChanged, totalCountChanged))
  }
}
