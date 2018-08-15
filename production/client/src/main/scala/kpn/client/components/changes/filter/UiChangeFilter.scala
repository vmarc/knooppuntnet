package kpn.client.components.changes.filter

import chandu0101.scalajs.react.components.materialui.TouchTapEvent
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Month
import kpn.client.common.Nls.nls
import kpn.client.components.filter.UiFilter
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.changes.filter.ChangesFilterPeriod

import scalacss.ScalaCssReact._

object UiChangeFilter {

  private case class Props(
    context: Context,
    changesFilter: ChangesFilter,
    filterChanged: (Option[String], Option[String], Option[String], Boolean) => Unit,
    itemsPerPage: Int,
    itemsPerPageChanged: (Int) => Unit
  )

  private val component = ScalaComponent.builder[Props]("changes-filter")
    .render_P { props =>
      new Renderer(
        props.changesFilter,
        props.filterChanged,
        props.itemsPerPage,
        props.itemsPerPageChanged
      )(props.context).render()
    }
    .build

  def apply(
    changesFilter: ChangesFilter,
    filterChanged: (Option[String], Option[String], Option[String], Boolean) => Unit,
    itemsPerPage: Int,
    itemsPerPageChanged: (Int) => Unit
  )(implicit context: Context): VdomElement = {
    component(
      Props(
        context,
        changesFilter,
        filterChanged,
        itemsPerPage,
        itemsPerPageChanged
      )
    )
  }

  private class Renderer(
    filter: ChangesFilter,
    filterChanged: (Option[String], Option[String], Option[String], Boolean) => Unit,
    itemsPerPage: Int,
    itemsPerPageChanged: (Int) => Unit
  )(implicit context: Context) {

    def render(): VdomElement = {

      val rowsPerPageChanged: (TouchTapEvent, Int, Int) => Callback = { (e, idx, a) =>
        CallbackTo {
          itemsPerPageChanged(a)
        }
      }

      <.div(
        UiFilter.Styles.filter,
        title(),
        legend(),
        totalPeriod(),
        filter.periods.flatMap { year =>
          yearPeriod(year) +:
            year.periods.flatMap { month =>
              monthPeriod(year, month) +: dayPeriods(year, month)
            }
        }.toTagMod,
        UiRowsPerPage(itemsPerPage, rowsPerPageChanged)
      )
    }

    private def title(): VdomElement = {
      <.div(
        UiFilter.Styles.title,
        "Filter"
      )
    }

    private def legend(): VdomElement = {
      <.div(
        UiFilter.Styles.row,
        <.div(
          UiFilter.Styles.countLinks,
          nls("impacted / all", "impact / alle")
        )
      )
    }

    private def totalPeriod(): VdomElement = {
      UiChangeFilterPeriod(
        UiFilter.Styles.year,
        nls("All", "Alle"),
        ChangesFilterPeriod(
          "",
          filter.totalCount,
          filter.impactedCount
        ),
        CallbackTo {
          filterChanged(None, None, None, true)
        },
        CallbackTo {
          filterChanged(None, None, None, false)
        }
      )
    }

    private def yearPeriod(year: ChangesFilterPeriod): VdomElement = {
      UiChangeFilterPeriod(
        UiFilter.Styles.year,
        year.name,
        year,
        CallbackTo {
          filterChanged(Some(year.name), None, None, true)
        },
        CallbackTo {
          filterChanged(Some(year.name), None, None, false)
        }
      )
    }

    private def monthPeriod(year: ChangesFilterPeriod, month: ChangesFilterPeriod): VdomElement = {
      UiChangeFilterPeriod(
        UiFilter.Styles.month,
        Month.name(month.name),
        month,
        CallbackTo {
          filterChanged(Some(year.name), Some(month.name), None, true)
        },
        CallbackTo {
          filterChanged(Some(year.name), Some(month.name), None, false)
        }
      )
    }

    private def dayPeriods(year: ChangesFilterPeriod, month: ChangesFilterPeriod): Seq[VdomElement] = {
      month.periods.map { day =>
        UiChangeFilterPeriod(
          UiFilter.Styles.day,
          day.name,
          day,
          CallbackTo {
            filterChanged(Some(year.name), Some(month.name), Some(day.name), true)
          },
          CallbackTo {
            filterChanged(Some(year.name), Some(month.name), Some(day.name), false)
          }
        )
      }
    }
  }

}
