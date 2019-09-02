// TODO migrate to Angular
package kpn.client

import kpn.client.RouteConfiguration.Goto
import kpn.client.RouteConfiguration.GotoAbout
import kpn.client.RouteConfiguration.GotoAuthenticate
import kpn.client.RouteConfiguration.GotoChangeSet
import kpn.client.RouteConfiguration.GotoChangeSetNetwork
import kpn.client.RouteConfiguration.GotoChangeSets
import kpn.client.RouteConfiguration.GotoChanges
import kpn.client.RouteConfiguration.GotoComponents
import kpn.client.RouteConfiguration.GotoGlossary
import kpn.client.RouteConfiguration.GotoGlossaryEntry
import kpn.client.RouteConfiguration.GotoHome
import kpn.client.RouteConfiguration.GotoLinks
import kpn.client.RouteConfiguration.GotoLogin
import kpn.client.RouteConfiguration.GotoLogout
import kpn.client.RouteConfiguration.GotoMap
import kpn.client.RouteConfiguration.GotoNetworkChanges
import kpn.client.RouteConfiguration.GotoNetworkDetails
import kpn.client.RouteConfiguration.GotoNetworkFacts
import kpn.client.RouteConfiguration.GotoNetworkMap
import kpn.client.RouteConfiguration.GotoNetworkNodes
import kpn.client.RouteConfiguration.GotoNetworkPage
import kpn.client.RouteConfiguration.GotoNetworkRoutes
import kpn.client.RouteConfiguration.GotoNode
import kpn.client.RouteConfiguration.GotoNotFound
import kpn.client.RouteConfiguration.GotoOverview
import kpn.client.RouteConfiguration.GotoRoute
import kpn.client.RouteConfiguration.GotoSubsetChanges
import kpn.client.RouteConfiguration.GotoSubsetFactDetails
import kpn.client.RouteConfiguration.GotoSubsetFacts
import kpn.client.RouteConfiguration.GotoSubsetMap
import kpn.client.RouteConfiguration.GotoSubsetNetworks
import kpn.client.RouteConfiguration.GotoSubsetOrphanNodes
import kpn.client.RouteConfiguration.GotoSubsetOrphanRoutes
import kpn.client.RouteConfiguration.GotoSubsetPage
import kpn.client.RouteConfiguration.GotoTryout
import kpn.shared.Subset
import org.scalajs.dom

object PageTitleBuilder {

  private val defaultTitle = "knooppuntnet"

  def setTitle(prefix: String): Unit = {
    dom.document.title = prefix + " | " + defaultTitle
  }

  def setNetworkPageTitle(prefix: String, networkName: String): Unit = {
    dom.document.title = networkName + " | " + prefix + " | " + defaultTitle
  }

  def title(goto: Goto): String = {

    def nls(enString: String, nlString: String): String = {
      if (goto.language == "en/") enString else nlString
    }

    def buildTitle(enString: String, nlString: String): String = {
      val prefix = nls(enString, nlString)
      prefix + " | " + defaultTitle
    }

    def changeSetTitle(changeSetId: Long): String = {
      nls("Changeset", "Wijzigingenset") + ": " + changeSetId + " | " + defaultTitle
    }

    def buildSubsetTitle(page: GotoSubsetPage, enString: String, nlString: String): String = {
      val part1 = page.subset match {
        case Subset.nlBicycle => nls("Cycling in The Netherlands", "Fietsen in Nederland")
        case Subset.beBicycle => nls("Cycling in Belgium", "Fietsen in België")
        case Subset.deBicycle => nls("Cycling in Germany", "Fietsen in Duitsland")
        case Subset.nlHiking => nls("Hiking in The Netherlands", "Wandelen in Nederland")
        case Subset.beHiking => nls("Hiking in Belgium", "Wandelen in België")
        case Subset.nlHorseRiding => nls("Horse riding in The Netherlands", "Ruiter in Nederland")
        case Subset.beHorseRiding => nls("Horse riding in Belgium", "Ruiter in België")
        case Subset.deHorseRiding => nls("Horse riding in Germany", "Ruiter in Duitsland")
        case Subset.nlCanoe => nls("Canoe in The Netherlands", "Kano in Nederland")
        case Subset.nlMotorboat => nls("Motorboat in The Netherlands", "Motorboot in Nederland")
        case _ => ""
      }
      val part2 = buildTitle(enString, nlString)
      part1 + " | " + part2
    }

    goto match {

      case page: GotoNotFound => buildTitle("NotFound", "Niet gevonden")
      case page: GotoTryout => defaultTitle
      case page: GotoHome => defaultTitle
      case page: GotoLogin => buildTitle("Login", "Aanmelden")
      case page: GotoLogout => buildTitle("Logout", "Afmelden")
      case page: GotoAuthenticate => defaultTitle
      case page: GotoAbout => buildTitle("About", "Introductie")
      case page: GotoGlossary => buildTitle("Glossary", "Woordenlijst")
      case page: GotoGlossaryEntry => buildTitle("Glossary", "Woordenlijst")
      case page: GotoLinks => buildTitle("Links", "Links")
      case page: GotoMap => defaultTitle
      case page: GotoNode => defaultTitle // note: more specific title is set in UiNodePage
      case page: GotoRoute => defaultTitle // note: more specific title is set in UiRoutePage

      case page: GotoSubsetNetworks => buildSubsetTitle(page, "Networks", "Netwerken")
      case page: GotoSubsetFacts => buildSubsetTitle(page, "Facts", "Feiten")
      case page: GotoSubsetFactDetails => buildSubsetTitle(page, "Facts", "Feiten")
      case page: GotoSubsetOrphanNodes => buildSubsetTitle(page, "Orphan nodes", "Knoopuntwezen")
      case page: GotoSubsetOrphanRoutes => buildSubsetTitle(page, "Orphan routes", "Routewezen")
      case page: GotoSubsetChanges => buildSubsetTitle(page, "Changes", "Wijzigingen")
      case page: GotoSubsetMap => buildSubsetTitle(page, "Map", "Kaart")
      case page: GotoSubsetPage => defaultTitle

      case page: GotoNetworkDetails => defaultTitle // note: more specific title is set in UiNetworkDetailsPage
      case page: GotoNetworkMap => defaultTitle // note: more specific title is set in UiNetworkMapPage
      case page: GotoNetworkFacts => defaultTitle // note: more specific title is set in UiNetworkFactsPage
      case page: GotoNetworkNodes => defaultTitle // note: more specific title is set in UiNetworkNodesPage
      case page: GotoNetworkRoutes => defaultTitle // note: more specific title is set in UiNetworkRoutesPage
      case page: GotoNetworkChanges => defaultTitle // note: more specific title is set in UiNetworkChangesPage
      case page: GotoNetworkPage => defaultTitle

      case page: GotoChangeSets => buildTitle("Changeset", "Wijziging")
      case page: GotoChangeSet => changeSetTitle(page.changeSetId)
      case page: GotoChangeSetNetwork => changeSetTitle(page.changeSetId)
      case page: GotoChanges => buildTitle("Changes", "Wijzigingen")
      case page: GotoComponents => defaultTitle
      case page: GotoOverview => buildTitle("Overview", "Overzicht")
    }
  }
}
