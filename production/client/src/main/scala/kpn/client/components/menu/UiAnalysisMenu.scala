// Migrated to Angular: analysis-page.component.ts
package kpn.client.components.menu

import chandu0101.scalajs.react.components.materialui.MuiMenuItem
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.raw.ReactElement
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.VdomNode
import kpn.client.RouteConfiguration.GotoSubsetNetworks
import kpn.client.RouteConfiguration.GotoSubsetPage
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.PageProps
import kpn.shared.Subset
import kpn.shared.Subset.beBicycle
import kpn.shared.Subset.beHiking
import kpn.shared.Subset.beHorseRiding
import kpn.shared.Subset.deBicycle
import kpn.shared.Subset.deHiking
import kpn.shared.Subset.deHorseRiding
import kpn.shared.Subset.frBicycle
import kpn.shared.Subset.frHiking
import kpn.shared.Subset.frHorseRiding
import kpn.shared.Subset.nlBicycle
import kpn.shared.Subset.nlCanoe
import kpn.shared.Subset.nlHiking
import kpn.shared.Subset.nlHorseRiding
import kpn.shared.Subset.nlInlineSkates
import kpn.shared.Subset.nlMotorboat

import scala.scalajs.js

object UiAnalysisMenu {

  private case class Props(
    pageProps: PageProps,
    active: Boolean,
    currentPage: Option[GotoSubsetPage]
  )

  private val component = ScalaComponent.builder[Props]("analysis-menu")
    .render_P { props =>

      implicit val context: Context = props.pageProps.context

      val subsets = Seq(
        nlBicycle,
        beBicycle,
        deBicycle,
        frBicycle,
        nlHiking,
        beHiking,
        deHiking,
        frHiking,
        nlHorseRiding,
        beHorseRiding,
        nlCanoe,
        nlMotorboat,
        nlInlineSkates
      )

      val links = js.Array[ReactElement]()
      subsets.foreach { subset =>
        val page = GotoSubsetNetworks(context.lang, subset.country.domain, subset.networkType.name)
        val active = props.currentPage.exists(_.subset == subset)
        val title = new SubsetTitle().get(page.subset)
        val vde: VdomElement = UiMenuItem(
          title,
          active,
          None,
          page
        )
        links.push(vde.rawElement)
      }

      val title: VdomNode = nls("Analysis", "Analyse")

      MuiMenuItem[String](
        key = "Analysis",
        primaryText = title,
        initiallyOpen = props.active,
        primaryTogglesNestedList = true,
        nestedItems = links
      )()
    }
    .build

  def apply[T](pageProps: PageProps, active: Boolean = false, currentPage: Option[GotoSubsetPage] = None): VdomElement = {
    component(Props(pageProps, active, currentPage))
  }

}
