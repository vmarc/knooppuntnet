package kpn.client

import chandu0101.scalajs.react.components.materialui.MuiMuiThemeProvider
import japgolly.scalajs.react.extra.router.BaseUrl
import japgolly.scalajs.react.extra.router.Router
import japgolly.scalajs.react.extra.router.RouterConfig
import kpn.client.RouteConfiguration.Goto
import kpn.client.common.map.UiMap
import kpn.client.components.UiTheme
import kpn.client.components.changes.UiImpactToggle
import kpn.client.components.changeset.UiMetaInfo
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.GlobalStyles
import kpn.client.components.common.UiAppBar
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiData
import kpn.client.components.common.UiDetail
import kpn.client.components.common.UiFacts
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiIndicator
import kpn.client.components.common.UiItems
import kpn.client.components.common.UiLevel1
import kpn.client.components.common.UiLevel2
import kpn.client.components.common.UiLevel3
import kpn.client.components.common.UiLevel4
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.client.components.common.UiNote
import kpn.client.components.common.UiPage
import kpn.client.components.common.UiPageContents
import kpn.client.components.common.UiPager
import kpn.client.components.common.UiSpinner
import kpn.client.components.common.UiTagDiffsTable
import kpn.client.components.common.UiTagDiffsText
import kpn.client.components.common.UiTagsTable
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.client.components.demo.UiComponentsPage
import kpn.client.components.filter.UiFilter
import kpn.client.components.home.UiHomePage
import kpn.client.components.map.UiMapDetail
import kpn.client.components.menu.UiSidebarFooter
import kpn.client.components.network.nodes.UiNetworkNodes
import kpn.client.components.network.routes.UiNetworkRoutes
import kpn.client.components.node.UiNodeNetworkReferences
import kpn.client.components.node.UiNodePage
import kpn.client.components.overview.UiOverviewPage
import kpn.client.components.route.UiRouteHistoryMap
import kpn.client.components.route.UiRouteMembers
import kpn.client.components.route.UiRouteNode
import kpn.client.components.shared.UiChangeSet
import kpn.client.components.shared.UiChangeSetTags
import kpn.client.components.shared._UiChangeSetInfo
import kpn.client.components.subset.networks.UiSubsetNetworksPage
import org.scalajs.dom
import scalacss.ScalaCssReact._

import scala.language.postfixOps

object Main {

  def main(args: Array[String]): Unit = {

    UnsafeStyles.addToDocument()
    GlobalStyles.addToDocument()

    UiAppBar.Styles.addToDocument()
    UiChangeSet.Styles.addToDocument()
    UiChangeSetTags.Styles.addToDocument()
    _UiChangeSetInfo.Styles.addToDocument()
    UiCommaList.Styles.addToDocument()
    UiComponentsPage.Styles.addToDocument()
    UiData.Styles.addToDocument()
    UiDetail.Styles.addToDocument()
    UiFilter.Styles.addToDocument()
    UiHomePage.Styles.addToDocument()
    UiImage.Styles.addToDocument()
    UiImpactToggle.Styles.addToDocument()
    UiIndicator.Styles.addToDocument()
    UiItems.Styles.addToDocument()
    UiLevel1.Styles.addToDocument()
    UiLevel2.Styles.addToDocument()
    UiLevel3.Styles.addToDocument()
    UiLevel4.Styles.addToDocument()
    UiLine.Styles.addToDocument()
    UiMap.Styles.addToDocument()
    UiMapDetail.Styles.addToDocument()
    UiNetworkNodes.Styles.addToDocument()
    UiNetworkRoutes.Styles.addToDocument()
    UiNetworkTypeAndText.Styles.addToDocument()
    UiNodeNetworkReferences.Styles.addToDocument()
    UiNodePage.Styles.addToDocument()
    UiNote.Styles.addToDocument()
    UiMetaInfo.Styles.addToDocument()
    UiOverviewPage.Styles.addToDocument()
    UiPage.Styles.addToDocument()
    UiPageContents.Styles.addToDocument()
    UiPager.Styles.addToDocument()
    UiFacts.Styles.addToDocument()
    UiRouteHistoryMap.Styles.addToDocument()
    UiRouteMembers.Styles.addToDocument()
    UiRouteNode.Styles.addToDocument()
    UiSidebarFooter.Styles.addToDocument()
    UiSpinner.Styles.addToDocument()
    UiSubsetNetworksPage.Styles.addToDocument()
    UiTagDiffsTable.Styles.addToDocument()
    UiTagDiffsText.Styles.addToDocument()
    UiTagsTable.Styles.addToDocument()
    UiThick.Styles.addToDocument()
    UiThin.Styles.addToDocument()

    val routeConfig: RouterConfig[Goto] = RouteConfiguration.routerConfig
    // .logToConsole //.renderWith(BaseLayout.layout)
    val router = Router(BaseUrl.fromWindowOrigin_/, routeConfig)

    val xx = router()

    val r = MuiMuiThemeProvider(muiTheme = UiTheme.custom) {
      xx.vdomElement
    }

    r.renderIntoDOM(dom.document.getElementById("root"))
  }
}
