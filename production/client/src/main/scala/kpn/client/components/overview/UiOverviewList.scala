// Migrated to Angular: overview-list.component.ts
package kpn.client.components.overview

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.statistics.Statistics
import scalacss.ScalaCssReact._

object UiOverviewList {

  private case class Props(context: Context, statistics: Statistics)

  private val component = ScalaComponent.builder[Props]("overview-list")
    .render_P { props =>
      new Renderer(props.statistics)(props.context).render()
    }
    .build

  def apply(statistics: Statistics)(implicit context: Context): VdomElement = component(Props(context, statistics))

  private class Renderer(statistics: Statistics)(implicit context: Context) {

    private val infos = new OverviewInfos(statistics)

    def render(): VdomElement = {
      <.div(
        UiOverviewPage.Styles.items,
        infoRow(infos.lengthInfo),
        infoRow(infos.networkCountInfo),
        infoRow(infos.nodeCountInfo),
        infoRow(infos.routeCountInfo),
        infoRow(infos.routeNotContiniousNetworkCountInfo),
        infoRow(infos.routeNotContiniousInfo),
        infoRow(infos.routeNotContiniousPercentageInfo),
        infoRow(infos.routeBrokenNetworkCountInfo),
        infoRow(infos.routeBrokenCountInfo),
        infoRow(infos.routeBrokenPercentageInfo),
        infoRow(infos.routeIncompleteInfo),
        infoRow(infos.routeIncompleteOkInfo),
        infoRow(infos.routeFixmetodoInfo),
        infoRow(infos.orphanNodeCountInfo),
        infoRow(infos.orphanRouteCountInfo),
        infoRow(infos.orphanRouteKmInfo),
        infoRow(infos.integrityCheckNetworkCountInfo),
        infoRow(infos.integrityCheckCount),
        infoRow(infos.integrityCheckFailedCount),
        infoRow(infos.integrityCheckPassRateInfo),
        infoRow(infos.integrityCheckCoverageInfo),
        infoRow(infos.networkTypeNotTaggedInfo),
        infoRow(infos.routeNetworkTypeNotTaggedInfo),
        infoRow(infos.nodeNetworkTypeNotTaggedInfo),
        infoRow(infos.routeUnusedSegmentsInfo),
        infoRow(infos.routeNodeMissingInWaysInfo),
        infoRow(infos.routeRedundantNodesInfo),
        infoRow(infos.routeWithoutWaysInfo),
        infoRow(infos.routeNameMissingInfo),
        infoRow(infos.routeTagMissingInfo),
        infoRow(infos.routeTagInvalidInfo),
        infoRow(infos.routeUnexpectedNode),
        infoRow(infos.routeUnexpectedRelation),
        infoRow(infos.networkExtraMemberNode),
        infoRow(infos.networkExtraMemberWay),
        infoRow(infos.networkExtraMemberRelation),
        infoRow(infos.nodeMemberMissing),
        infoRow(infos.nameMissingInfo),
        infoRow(infos.routeUnaccessibleInfo),
        infoRow(infos.routeInvalidSortingOrderInfo),
        infoRow(infos.routeReversedInfo),
        infoRow(infos.routeNodeNameMismatchInfo)
      )
    }

    private def infoRow(info: UiOverviewInfo): VdomElement = {
      <.div(
        UiOverviewPage.Styles.item,
        <.div(
          UiOverviewPage.Styles.title,
          info.title
        ),
        <.div(
          UiOverviewPage.Styles.comment,
          info.comment
        ),
        counts(info)
      )
    }

    private def counts(info: UiOverviewInfo): VdomElement = {
      <.table(
        <.tbody(
          countryCounts(Country.nl, nls("The Netherlands", "Nederland"), info.counts.nlRcn, info.counts.nlRwn, info.counts.nlRhn, info.counts.nlRmn, info.counts.nlRpn,
            info.counts.nlRin),
          countryCounts(Country.be, nls("Belgium", "België"), info.counts.beRcn, info.counts.beRwn, info.counts.beRhn, null, null, null),
          countryCounts(Country.de, nls("Germany", "Duitsland"), info.counts.deRcn, info.counts.deRwn, info.counts.deRhn, null, null, null),
          <.tr(
            <.td(
              ^.colSpan := 2,
              nls("Total", "Totaal")
            ),
            <.td(
              UiOverviewPage.Styles.valueColumn,
              info.counts.total
            )
          )
        )
      )
    }

    private def countryCounts(country: Country, countryName: String, rcn: VdomElement, rwn: VdomElement, rhn: VdomElement, rmn: VdomElement, rpn: VdomElement,
      rin: VdomElement): TagMod = {
      Seq(
        <.tr(
          <.td(
            ^.rowSpan := (if (Country.nl == country) 6 else 3),
            countryName
          ),
          <.td(
            UiNetworkTypeIcon(NetworkType.bicycle)
          ),
          <.td(
            UiOverviewPage.Styles.valueColumn,
            rcn
          )
        ),
        <.tr(
          <.td(
            UiNetworkTypeIcon(NetworkType.hiking)
          ),
          <.td(
            UiOverviewPage.Styles.valueColumn,
            rwn
          )
        ),
        <.tr(
          <.td(
            UiNetworkTypeIcon(NetworkType.horseRiding)
          ),
          <.td(
            UiOverviewPage.Styles.valueColumn,
            rhn
          )
        ),
        TagMod.when(Country.nl == country) {
          <.tr(
            <.td(
              UiNetworkTypeIcon(NetworkType.motorboat)
            ),
            <.td(
              UiOverviewPage.Styles.valueColumn,
              rmn
            )
          )
        },
        TagMod.when(Country.nl == country) {
          <.tr(
            <.td(
              UiNetworkTypeIcon(NetworkType.canoe)
            ),
            <.td(
              UiOverviewPage.Styles.valueColumn,
              rpn
            )
          )
        },
        TagMod.when(Country.nl == country) {
          <.tr(
            <.td(
              UiNetworkTypeIcon(NetworkType.inlineSkates)
            ),
            <.td(
              UiOverviewPage.Styles.valueColumn,
              rin
            )
          )
        }
      ).toTagMod
    }
  }

}
