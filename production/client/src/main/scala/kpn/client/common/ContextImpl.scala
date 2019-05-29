// TODO migrate to Angular
package kpn.client.common

import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.VdomTagOf
import kpn.client.RouteConfiguration.Goto
import kpn.client.RouteConfiguration.GotoAbout
import kpn.client.RouteConfiguration.GotoChangeSet
import kpn.client.RouteConfiguration.GotoChangeSetNetwork
import kpn.client.RouteConfiguration.GotoChanges
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
import kpn.client.RouteConfiguration.GotoNetworkNodes
import kpn.client.RouteConfiguration.GotoNetworkRoutes
import kpn.client.RouteConfiguration.GotoNode
import kpn.client.RouteConfiguration.GotoOverview
import kpn.client.RouteConfiguration.GotoRoute
import kpn.client.RouteConfiguration.GotoSubsetChanges
import kpn.client.RouteConfiguration.GotoSubsetFactDetails
import kpn.client.RouteConfiguration.GotoSubsetFacts
import kpn.client.RouteConfiguration.GotoSubsetMap
import kpn.client.RouteConfiguration.GotoSubsetNetworks
import kpn.client.RouteConfiguration.GotoSubsetOrphanNodes
import kpn.client.RouteConfiguration.GotoSubsetOrphanRoutes
import kpn.client.common.Nls.nls
import kpn.client.components.menu.SubsetTitle
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import org.scalajs.dom.html

class ContextImpl(
  val language: Language,
  router: RouterCtl[Goto],
  en: Goto,
  nl: Goto

) extends Context {

  implicit val context: Context = this

  override def tempGetRouter: RouterCtl[Goto] = router


  override def lang: String = {
    language.toString.toLowerCase + "/"
  }

  def gotoHome(): VdomElement = {
    router.link(GotoHome(lang))(nls("Home", "Start"))
  }

  def gotoLogin(): VdomElement = {
    User.registerLoginCallbackPage()
    router.link(GotoLogin(lang))(nls("login", "aanmelden"))
  }

  def gotoLogout(): VdomElement = {
    router.link(GotoLogout(lang))(nls("logout", "afmelden"))
  }

  def gotoAbout(): VdomElement = {
    router.link(GotoAbout(lang))(nls("About", "Introductie"))
  }

  def gotoGlossary(): VdomElement = {
    router.link(GotoGlossary(lang))(nls("Glossary", "Woordenlijst"))
  }

  def gotoGlossaryEntry(arg: String, title: String): VdomElement = {
    router.link(GotoGlossaryEntry(lang, arg))(title)
  }

  def gotoLinks(): VdomElement = {
    router.link(GotoLinks(lang))("Links")
  }

  def gotoOverview(): VdomElement = {
    router.link(GotoOverview(lang))(nls("Overview in numbers", "Overzicht in cijfers"))
  }

  def gotoNode(id: Long, name: String): VdomElement = {
    router.link(GotoNode(lang, id))(name)
  }

  def gotoRoute(id: Long, name: String): VdomElement = {
    router.link(GotoRoute(lang, id))(name)
  }

  def gotoMap(networkType: NetworkType, title: String): VdomElement = {
    router.link(GotoMap(lang, networkType.name))(title)
  }

  def gotoSubsetNetworks(subset: Subset, altTitle: Option[String]): VdomElement = {
    val title = altTitle.getOrElse(new SubsetTitle().get(subset))
    router.link(GotoSubsetNetworks(lang, subset.country.domain, subset.networkType.name))(title)
  }

  def gotoSubsetFacts(subset: Subset, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetFacts(lang, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoSubsetFactDetails(subset: Subset, fact: Fact, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetFactDetails(lang, fact.name, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoSubsetOrphanNodes(subset: Subset, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetOrphanNodes(lang, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoSubsetOrphanRoutes(subset: Subset, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetOrphanRoutes(lang, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoSubsetChanges(subset: Subset, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetChanges(lang, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoSubsetMap(subset: Subset, title: String, cls: TagMod): VdomElement = {
    router.link(GotoSubsetMap(lang, subset.country.domain, subset.networkType.name))(title)(cls)
  }

  def gotoNetworkDetails(networkId: Long, name: String, cls: TagMod): VdomElement = {
    router.link(GotoNetworkDetails(lang, networkId))(name)(cls)
  }

  def gotoNetworkFacts(networkId: Long, title: String /*, level: String*/ , cls: TagMod): VdomElement = {
    router.link(GotoNetworkFacts(lang, networkId))(title)(cls)
  }

  def gotoNetworkNodes(networkId: Long, title: String, cls: TagMod): VdomElement = {
    router.link(GotoNetworkNodes(lang, networkId))(title)(cls)
  }

  def gotoNetworkRoutes(networkId: Long, title: String, cls: TagMod): VdomElement = {
    router.link(GotoNetworkRoutes(lang, networkId))(title)(cls)
  }

  def gotoNetworkChanges(networkId: Long, title: String, cls: TagMod): VdomElement = {
    router.link(GotoNetworkChanges(lang, networkId))(title)(cls)
  }

  def gotoChangeSets(changeSetId: Long): VdomElement = {
    <.div("TODO")
  }

  def gotoChangeSet(changeSetId: Long, replicationNumber: Int): VdomElement = {
    router.link(GotoChangeSet(lang, changeSetId, replicationNumber))(changeSetId)
  }

  def gotoChangeSetNetwork(changeSetId: Long, xxx: (Int, Long), title: String): VdomElement = {
    router.link(GotoChangeSetNetwork(lang, changeSetId, xxx))(title)
  }

  def gotoChanges(): VdomElement = {
    val title = nls("Network changes", "Netwerk wijzigingen")(this)
    router.link(GotoChanges(lang))(title)
  }

  def goto(target: Goto): VdomTagOf[html.Anchor] = {
    router.link(target)
  }

  def gotoEn(): VdomElement = {
    router.link(en)("English")
  }

  def gotoNl(): VdomElement = {
    router.link(nl)("Nederlands")
  }
}
