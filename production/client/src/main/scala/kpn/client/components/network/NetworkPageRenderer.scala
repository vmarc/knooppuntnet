// Migrated to Angular
package kpn.client.components.network

import chandu0101.scalajs.react.components.materialui.MuiDivider
import japgolly.scalajs.react.CallbackTo
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.PageStatus
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContent
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.client.components.subset.UiSubsetMenu.Target
import kpn.shared.network.NetworkSummary

abstract class NetworkPageRenderer(
  target: Target,
  status: PageStatus.Value,
  networkName: Option[String],
  networkSummary: Option[NetworkSummary]
)(implicit props: NetworkPageProps) {

  protected implicit val context: Context = props.args.context

  def render(): VdomElement = {
    UiPage(
      props.pageProps,
      sideBar(),
      content()
    )
  }

  private def content(): VdomElement = {
    UiPageContent(
      networkName.getOrElse(""),
      status,
      CallbackTo {
        contents()
      }
    )
  }

  private def sideBar(): Seq[VdomElement] = {
    if (props.pageProps.ui.isSideBarFilter) {
      Seq(
        filter()
      )
    }
    else if (props.pageProps.hasFilter) {
      Seq(
        UiAnalysisMenu(props.pageProps),
        MuiDivider()(),
        UiNetworkMenu(props.args, target, networkSummary),
        filter(),
        UiSidebarFooter(props.pageProps)
      )
    }
    else {
      Seq(
        UiAnalysisMenu(props.pageProps),
        MuiDivider()(),
        UiNetworkMenu(props.args, target, networkSummary),
        UiSidebarFooter(props.pageProps)
      )
    }
  }

  protected def filter(): VdomElement = <.div("(>) FILTER")

  protected def contents(): TagMod

  private def title(): VdomElement = {
    <.h1("Title")
  }
}
