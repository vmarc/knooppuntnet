package kpn.client

import japgolly.scalajs.react.extra.router.Redirect
import japgolly.scalajs.react.extra.router.RouterConfig
import japgolly.scalajs.react.extra.router.RouterConfigDsl
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.app.AppCircuit
import kpn.client.common.ChangeSetPageArgs
import kpn.client.common.ChangeSetsPageArgs
import kpn.client.common.ChangesPageArgs
import kpn.client.common.ComponentsPageArgs
import kpn.client.common.Context
import kpn.client.common.ContextImpl
import kpn.client.common.EN
import kpn.client.common.GlossaryPageArgs
import kpn.client.common.MapPageArgs
import kpn.client.common.NL
import kpn.client.common.NetworkPageArgs
import kpn.client.common.NodePageArgs
import kpn.client.common.OverviewPageArgs
import kpn.client.common.RoutePageArgs
import kpn.client.common.SubsetPageArgs
import kpn.client.components.UiNotFoundPage
import kpn.client.components.changes.UiChangesPage
import kpn.client.components.changeset.UiChangeSetPage
import kpn.client.components.changeset.UiChangeSetsPage
import kpn.client.components.demo.UiComponentsPage
import kpn.client.components.home.UiHomePage
import kpn.client.components.login.UiAuthenticatePage
import kpn.client.components.login.UiLoginPage
import kpn.client.components.login.UiLogoutPage
import kpn.client.components.map.UiMapPage
import kpn.client.components.network.changes.UiNetworkChangesPage
import kpn.client.components.network.details.UiNetworkDetailsPage
import kpn.client.components.network.facts.UiNetworkFactsPage
import kpn.client.components.network.map.UiNetworkMapPage
import kpn.client.components.network.nodes.UiNetworkNodesPage
import kpn.client.components.network.routes.UiNetworkRoutesPage
import kpn.client.components.node.UiNodePage
import kpn.client.components.overview.UiOverviewPage
import kpn.client.components.route.UiRoutePage
import kpn.client.components.subset.UiSubsetMap
import kpn.client.components.subset.changes.UiSubsetChangesPage
import kpn.client.components.subset.facts.UiSubsetFactDetailsPage
import kpn.client.components.subset.facts.UiSubsetFactsPage
import kpn.client.components.subset.networks.UiSubsetNetworksPage
import kpn.client.components.subset.nodes.UiSubsetOrphanNodesPage
import kpn.client.components.subset.routes.UiSubsetOrphanRoutesPage
import kpn.client.components.tryout.UiTryout
import kpn.client.modules.UiAboutPage
import kpn.client.modules.UiGlossaryPage
import kpn.client.modules.UiLinksPage
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import org.scalajs.dom

object RouteConfiguration {

  sealed trait Goto {
    def en: Goto

    def nl: Goto

    def language: String

    def context(router: RouterCtl[Goto]): Context = {
      val lang = language match {
        case "en/" => EN
        case "nl/" => NL
        case _ =>
          val browserLanguage = dom.window.navigator.language
          if (browserLanguage != null && browserLanguage.startsWith("nl")) {
            NL
          }
          else {
            EN
          }
      }
      val context = new ContextImpl(lang, router, en, nl)
      DocLinks.contextOption = Some(context)
      context
    }
  }

  case class GotoNotFound(path: String) extends Goto {

    def language: String = if (path.startsWith("nl/")) "nl/" else "en/"

    def en: GotoNotFound = this

    def nl: GotoNotFound = this

  }

  case class GotoTryout(language: String) extends Goto {

    def en: GotoTryout = copy(language = "en/")

    def nl: GotoTryout = copy(language = "nl/")
  }

  case class GotoHome(language: String) extends Goto {

    def en: GotoHome = copy(language = "en/")

    def nl: GotoHome = copy(language = "nl/")
  }

  case class GotoLogin(language: String) extends Goto {

    def en: GotoLogin = copy(language = "en/")

    def nl: GotoLogin = copy(language = "nl/")
  }

  case class GotoLogout(language: String) extends Goto {

    def en: GotoLogout = copy(language = "en/")

    def nl: GotoLogout = copy(language = "nl/")
  }

  case class GotoAuthenticate(language: String, queryParams: String) extends Goto {

    def en: GotoAuthenticate = copy(language = "en/")

    def nl: GotoAuthenticate = copy(language = "nl/")
  }

  case class GotoAbout(language: String) extends Goto {

    def en: GotoAbout = copy(language = "en/")

    def nl: GotoAbout = copy(language = "nl/")
  }

  case class GotoGlossary(language: String) extends Goto {

    def en: GotoGlossary = copy(language = "en/")

    def nl: GotoGlossary = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): GlossaryPageArgs = {
      GlossaryPageArgs(context(router), this, None)
    }
  }

  case class GotoGlossaryEntry(language: String, keyword: String) extends Goto {

    def en: GotoGlossaryEntry = copy(language = "en/")

    def nl: GotoGlossaryEntry = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): GlossaryPageArgs = {
      GlossaryPageArgs(context(router), GotoGlossary(language), Some(keyword))
    }
  }

  case class GotoLinks(language: String) extends Goto {

    def en: GotoLinks = copy(language = "en/")

    def nl: GotoLinks = copy(language = "nl/")
  }

  case class GotoMap(language: String, networkType: String) extends Goto {

    def en: GotoMap = copy(language = "en/")

    def nl: GotoMap = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): MapPageArgs = {
      MapPageArgs(context(router), this, NetworkType.withName(networkType).get)
    }

  }

  case class GotoNode(language: String, nodeId: Long) extends Goto {
    def en: GotoNode = copy(language = "en/")

    def nl: GotoNode = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): NodePageArgs = {
      NodePageArgs(context(router), this, nodeId)
    }
  }

  case class GotoRoute(language: String, routeId: Long) extends Goto {
    def en: GotoRoute = copy(language = "en/")

    def nl: GotoRoute = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): RoutePageArgs = {
      RoutePageArgs(context(router), this, routeId)
    }
  }

  trait GotoSubsetPage extends Goto {
    def country: String

    def networkType: String

    def fact: String

    def subset: Subset = Subset.of(country, networkType).get

    private def factOption: Option[Fact] = Fact.withName(fact)

    def props(router: RouterCtl[Goto]): SubsetPageArgs = {
      SubsetPageArgs(context(router), this, subset, factOption)
    }
  }

  trait GotoNetworkPage extends Goto {
    def networkId: Long

    def props(router: RouterCtl[Goto]): NetworkPageArgs = {
      NetworkPageArgs(context(router), this, networkId)
    }
  }

  case class GotoSubsetNetworks(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetNetworks = copy(language = "en/")

    def nl: GotoSubsetNetworks = copy(language = "nl/")
  }

  case class GotoSubsetFacts(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetFacts = copy(language = "en/")

    def nl: GotoSubsetFacts = copy(language = "nl/")
  }

  case class GotoSubsetFactDetails(language: String, fact: String, country: String, networkType: String) extends GotoSubsetPage {
    def en: GotoSubsetFactDetails = copy(language = "en/")

    def nl: GotoSubsetFactDetails = copy(language = "nl/")
  }

  case class GotoSubsetOrphanNodes(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetOrphanNodes = copy(language = "en/")

    def nl: GotoSubsetOrphanNodes = copy(language = "nl/")
  }

  case class GotoSubsetOrphanRoutes(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetOrphanRoutes = copy(language = "en/")

    def nl: GotoSubsetOrphanRoutes = copy(language = "nl/")
  }

  case class GotoSubsetChanges(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetChanges = copy(language = "en/")

    def nl: GotoSubsetChanges = copy(language = "nl/")
  }

  case class GotoSubsetMap(language: String, country: String, networkType: String) extends GotoSubsetPage {
    def fact = ""

    def en: GotoSubsetMap = copy(language = "en/")

    def nl: GotoSubsetMap = copy(language = "nl/")
  }

  case class GotoNetworkDetails(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkDetails = copy(language = "en/")

    def nl: GotoNetworkDetails = copy(language = "nl/")
  }

  case class GotoNetworkMap(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkMap = copy(language = "en/")

    def nl: GotoNetworkMap = copy(language = "nl/")
  }

  case class GotoNetworkFacts(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkFacts = copy(language = "en/")

    def nl: GotoNetworkFacts = copy(language = "nl/")
  }

  case class GotoNetworkNodes(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkNodes = copy(language = "en/")

    def nl: GotoNetworkNodes = copy(language = "nl/")
  }

  case class GotoNetworkRoutes(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkRoutes = copy(language = "en/")

    def nl: GotoNetworkRoutes = copy(language = "nl/")
  }

  case class GotoNetworkChanges(language: String, networkId: Long) extends GotoNetworkPage {
    def en: GotoNetworkChanges = copy(language = "en/")

    def nl: GotoNetworkChanges = copy(language = "nl/")
  }

  case class GotoChangeSets(language: String, changeSetId: Long) extends Goto {
    def en: GotoChangeSets = copy(language = "en/")

    def nl: GotoChangeSets = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): ChangeSetsPageArgs = {
      ChangeSetsPageArgs(context(router), this)
    }
  }

  case class GotoChangeSet(language: String, changeSetId: Long, replicationNumber: Int) extends Goto {
    def en: GotoChangeSet = copy(language = "en/")

    def nl: GotoChangeSet = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): ChangeSetPageArgs = {
      ChangeSetPageArgs(context(router), this, changeSetId, replicationNumber, 0L)
    }
  }

  case class GotoChangeSetNetwork(language: String, changeSetId: Long, xxx: (Int, Long)) extends Goto {
    def en: GotoChangeSetNetwork = copy(language = "en/")

    def nl: GotoChangeSetNetwork = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): ChangeSetPageArgs = {
      ChangeSetPageArgs(context(router), this, changeSetId, xxx._1, xxx._2)
    }
  }

  case class GotoChanges(language: String) extends Goto {
    def en: GotoChanges = copy(language = "en/")

    def nl: GotoChanges = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): ChangesPageArgs = {
      ChangesPageArgs(context(router), this)
    }
  }

  case class GotoComponents(language: String) extends Goto {
    def en: GotoComponents = copy(language = "en/")

    def nl: GotoComponents = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): ComponentsPageArgs = {
      ComponentsPageArgs(context(router), this)
    }
  }

  case class GotoOverview(language: String) extends Goto {
    def en: GotoOverview = copy(language = "en/")

    def nl: GotoOverview = copy(language = "nl/")

    def props(router: RouterCtl[Goto]): OverviewPageArgs = {
      OverviewPageArgs(context(router), this)
    }
  }

  val routerConfig: RouterConfig[Goto] = RouterConfigDsl[Goto].buildConfig { dsl =>
    import dsl.Rule
    import dsl._

    val language = string("(?:en/|nl/|)")
    val country = string("(?:be|de|nl)")
    val networkType = string("(?:rwn|rcn|rhn|rmn|rpn|rin)")
    val fact = string(Fact.all.map(_.name).mkString("(?:", "|", ")"))
    val remainingPath = string(".+")

    val homeRule: Rule = {
      val route = language.caseClass[GotoHome]
      val renderer = dynRenderR[GotoHome, VdomElement] { case (n, ctl) =>
        UiHomePage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val loginRule: Rule = {
      val route = (language ~ "login").caseClass[GotoLogin]
      val renderer = dynRenderR[GotoLogin, VdomElement] { case (n, ctl) =>
        UiLoginPage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val logoutRule: Rule = {
      val route = (language ~ "logout").caseClass[GotoLogout]
      val renderer = dynRenderR[GotoLogout, VdomElement] { case (n, ctl) =>
        UiLogoutPage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val tryoutRule: Rule = {
      val route = (language ~ "tryout").caseClass[GotoTryout]
      val statisticsWrapper = AppCircuit.connect(_.statistics)
      val renderer = dynRenderR[GotoTryout, VdomElement] { case (n, ctl) =>
        statisticsWrapper(s => UiTryout(n.context(ctl), s))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val aboutRule: Rule = {
      val route = (language ~ "about").caseClass[GotoAbout]
      val renderer = dynRenderR[GotoAbout, VdomElement] { case (n, ctl) =>
        UiAboutPage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val glossaryRule: Rule = {
      val route = (language ~ "glossary").caseClass[GotoGlossary]
      val renderer = dynRenderR[GotoGlossary, VdomElement] { case (n, ctl) =>
        UiGlossaryPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val glossaryEntryRule: Rule = {
      val route = (language ~ "glossary" ~ "#" ~ remainingPath).caseClass[GotoGlossaryEntry]
      val renderer = dynRenderR[GotoGlossaryEntry, VdomElement] { case (n, ctl) =>
        UiGlossaryPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val linksRule: Rule = {
      val route = (language ~ "links").caseClass[GotoLinks]
      val renderer = dynRenderR[GotoLinks, VdomElement] { case (n, ctl) =>
        UiLinksPage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val nodeRule: Rule = {
      val route = (language ~ "node" / long).caseClass[GotoNode]
      val renderer = dynRenderR[GotoNode, VdomElement] { case (n, ctl) =>
        UiNodePage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val routeRule: Rule = {
      val route = (language ~ "route" / long).caseClass[GotoRoute]
      val renderer = dynRenderR[GotoRoute, VdomElement] { case (n, ctl) =>
        UiRoutePage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val mapRule: Rule = {
      val route = (language ~ "map" / networkType).caseClass[GotoMap]
      val renderer = dynRenderR[GotoMap, VdomElement] { case (n, ctl) =>
        UiMapPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetNetworksRule: Rule = {
      val route = (language ~ "networks" / country / networkType).caseClass[GotoSubsetNetworks]
      val renderer = dynRenderR[GotoSubsetNetworks, VdomElement] { case (n, ctl) =>
        UiSubsetNetworksPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetFactsRule: Rule = {
      val route = (language ~ "facts" / country / networkType).caseClass[GotoSubsetFacts]
      val renderer = dynRenderR[GotoSubsetFacts, VdomElement] { case (n, ctl) =>
        UiSubsetFactsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetFactDetailsRule: Rule = {
      val route = (language ~ fact / country / networkType).caseClass[GotoSubsetFactDetails]
      val renderer = dynRenderR[GotoSubsetFactDetails, VdomElement] { case (n, ctl) =>
        UiSubsetFactDetailsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetOrphanNodesRule: Rule = {
      val route = (language ~ "orphan-nodes" / country / networkType).caseClass[GotoSubsetOrphanNodes]
      val renderer = dynRenderR[GotoSubsetOrphanNodes, VdomElement] { case (n, ctl) =>
        UiSubsetOrphanNodesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetOrphanRoutesRule: Rule = {
      val route = (language ~ "orphan-routes" / country / networkType).caseClass[GotoSubsetOrphanRoutes]
      val renderer = dynRenderR[GotoSubsetOrphanRoutes, VdomElement] { case (n, ctl) =>
        UiSubsetOrphanRoutesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetChangesRule: Rule = {
      val route = (language ~ "changes" / country / networkType).caseClass[GotoSubsetChanges]
      val renderer = dynRenderR[GotoSubsetChanges, VdomElement] { case (n, ctl) =>
        UiSubsetChangesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val subsetMapRule: Rule = {
      val route = (language ~ "network-map" / country / networkType).caseClass[GotoSubsetMap]
      val renderer = dynRenderR[GotoSubsetMap, VdomElement] { case (n, ctl) =>
        UiSubsetMap(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkDetailsRule = {
      val route = (language ~ "network" / long).caseClass[GotoNetworkDetails]
      val renderer = dynRenderR[GotoNetworkDetails, VdomElement] { case (n, ctl) =>
        UiNetworkDetailsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkMapRule = {
      val route = (language ~ "network-map" / long).caseClass[GotoNetworkMap]
      val renderer = dynRenderR[GotoNetworkMap, VdomElement] { case (n, ctl) =>
        UiNetworkMapPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkFactsRule: Rule = {
      val route = (language ~ "network-facts" / long).caseClass[GotoNetworkFacts]
      val renderer = dynRenderR[GotoNetworkFacts, VdomElement] { case (n, ctl) =>
        UiNetworkFactsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkNodesRule: Rule = {
      val route = (language ~ "network-nodes" / long).caseClass[GotoNetworkNodes]
      val renderer = dynRenderR[GotoNetworkNodes, VdomElement] { case (n, ctl) =>
        UiNetworkNodesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkRoutesRule: Rule = {
      val route = (language ~ "network-routes" / long).caseClass[GotoNetworkRoutes]
      val renderer = dynRenderR[GotoNetworkRoutes, VdomElement] { case (n, ctl) =>
        UiNetworkRoutesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val networkChangesRule: Rule = {
      val route = (language ~ "network-changes" / long).caseClass[GotoNetworkChanges]
      val renderer = dynRenderR[GotoNetworkChanges, VdomElement] { case (n, ctl) =>
        UiNetworkChangesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val changeSetsRule: Rule = {
      val route = (language ~ "changeset" / long).caseClass[GotoChangeSets]
      val renderer = dynRenderR[GotoChangeSets, VdomElement] { case (n, ctl) =>
        UiChangeSetsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val changeSetRule: Rule = {
      val route = (language ~ "changeset" / long / int).caseClass[GotoChangeSet]
      val renderer = dynRenderR[GotoChangeSet, VdomElement] { case (n, ctl) =>
        UiChangeSetPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val changeSetNetworkRule: Rule = {
      val route = (language ~ "changeset" / long / int ~ "#" ~ long).caseClass[GotoChangeSetNetwork]
      val renderer = dynRenderR[GotoChangeSetNetwork, VdomElement] { case (n, ctl) =>
        UiChangeSetPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val changesRule: Rule = {
      val route = (language ~ "changes").caseClass[GotoChanges]
      val renderer = dynRenderR[GotoChanges, VdomElement] { case (n, ctl) =>
        UiChangesPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val componentsRule: Rule = {
      val route = (language ~ "components").caseClass[GotoComponents]
      val renderer = dynRenderR[GotoComponents, VdomElement] { case (n, ctl) =>
        UiComponentsPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val overviewRule: Rule = {
      val route = (language ~ "overview").caseClass[GotoOverview]
      val renderer = dynRenderR[GotoOverview, VdomElement] { case (n, ctl) =>
        UiOverviewPage(n.props(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    val notFoundRule: Rule = {
      val route = remainingPath.caseClass[GotoNotFound]
      val renderer = dynRenderR[GotoNotFound, VdomElement] { case (n, ctl) =>
        UiNotFoundPage(n.context(ctl), n.path)
      }
      dynamicRouteCT(route) ~> renderer
    }

    val authenticateRule: Rule = {
      val route = (language ~ "authenticate" ~ remainingPath).caseClass[GotoAuthenticate]
      val renderer = dynRenderR[GotoAuthenticate, VdomElement] { case (n, ctl) =>
        UiAuthenticatePage(n.context(ctl))
      }
      dynamicRouteCT(route) ~> renderer
    }

    (homeRule
      | loginRule
      | logoutRule
      | authenticateRule
      | tryoutRule
      | aboutRule
      | glossaryRule
      | glossaryEntryRule
      | linksRule
      | nodeRule
      | routeRule
      | mapRule
      | subsetNetworksRule
      | subsetFactsRule
      | subsetFactDetailsRule
      | subsetOrphanNodesRule
      | subsetOrphanRoutesRule
      | subsetChangesRule
      | subsetMapRule
      | networkDetailsRule
      | networkMapRule
      | networkFactsRule
      | networkNodesRule
      | networkRoutesRule
      | networkChangesRule
      | changeSetsRule
      | changeSetRule
      | changeSetNetworkRule
      | changesRule
      | componentsRule
      | overviewRule
      | notFoundRule
      ).notFound(redirectToPath("")(Redirect.Replace)) // redirect to home page; normally this will not happen given the "notFoundRule" above
      .setTitle(PageTitleBuilder.title)
  }
}
