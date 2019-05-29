// TODO migrate to Angular
package kpn.client.components.subset

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
import kpn.client.components.menu.SubsetTitle
import kpn.client.components.menu.UiAnalysisMenu
import kpn.client.components.menu.UiSidebarFooter
import kpn.client.components.subset.UiSubsetMenu.Target

abstract class SubsetPageRenderer(target: Target, state: PageStatus.Value)(implicit ssProps: SubsetPageProps) {

  protected implicit val context: Context = ssProps.args.context

  def title: String = new SubsetTitle().get(ssProps.args.subset)

  def render(): VdomElement = {

    val sideBar: Seq[VdomElement] = {
      if (ssProps.pageProps.ui.isSideBarFilter) {
        Seq(
          filter()
        )
      }
      else if (ssProps.pageProps.hasFilter) {
        Seq(
          analysis(),
          MuiDivider()(),
          subsetMenu(),
          filter(),
          UiSidebarFooter(ssProps.pageProps)
        )
      }
      else {
        Seq(
          analysis(),
          MuiDivider()(),
          subsetMenu(),
          UiSidebarFooter(ssProps.pageProps)
        )
      }
    }

    val content = UiPageContent(
      title,
      state,
      CallbackTo {
        contents()
      }
    )

    UiPage(
      ssProps.pageProps,
      sideBar,
      content
    )
  }

  protected def contents(): TagMod

  private def analysis(): VdomElement = {
    UiAnalysisMenu(ssProps.pageProps, active = true, Some(ssProps.args.currentPage))
  }

  private def subsetMenu(): VdomElement = {
    val subsetInfo = SubsetInfoCache.get(ssProps.args.subset)
    UiSubsetMenu(ssProps.args, target, subsetInfo)
  }

  protected def filter(): VdomElement = {
    <.span()
  }
}
