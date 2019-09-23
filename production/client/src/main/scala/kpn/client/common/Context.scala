// Migrated to Angular
package kpn.client.common

import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.VdomTagOf
import japgolly.scalajs.react.vdom.html_<^.EmptyVdom
import kpn.client.RouteConfiguration.Goto
import kpn.shared.Fact
import kpn.shared.Language
import kpn.shared.NetworkType
import kpn.shared.Subset
import org.scalajs.dom.html

trait Context {

  def tempGetRouter: RouterCtl[Goto]

  def lang: String

  def language: Language

  def gotoEn(): VdomElement

  def gotoNl(): VdomElement

  def goto(target: Goto): VdomTagOf[html.Anchor]

  def gotoHome(): VdomElement

  def gotoLogin(): VdomElement

  def gotoLogout(): VdomElement

  def gotoAbout(): VdomElement

  def gotoGlossary(): VdomElement

  def gotoGlossaryEntry(arg: String, title: String): VdomElement

  def gotoLinks(): VdomElement

  def gotoOverview(): VdomElement

  def gotoNode(id: Long, name: String): VdomElement

  def gotoRoute(routeId: Long, name: String): VdomElement

  def gotoMap(networkType: NetworkType, title: String): VdomElement

  def gotoSubsetNetworks(subset: Subset, altTitle: Option[String] = None): VdomElement

  def gotoSubsetFacts(subset: Subset, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoSubsetFactDetails(subset: Subset, fact: Fact, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoSubsetOrphanNodes(subset: Subset, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoSubsetOrphanRoutes(subset: Subset, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoSubsetChanges(subset: Subset, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoSubsetMap(subset: Subset, title: String, tagmod: TagMod = EmptyVdom): VdomElement

  def gotoNetworkDetails(networkId: Long, title: String, cls: TagMod = EmptyVdom): VdomElement

  def gotoNetworkFacts(networkId: Long, title: String, cls: TagMod): VdomElement

  def gotoNetworkNodes(networkId: Long, title: String, cls: TagMod): VdomElement

  def gotoNetworkRoutes(networkId: Long, title: String, cls: TagMod): VdomElement

  def gotoNetworkChanges(networkId: Long, title: String, cls: TagMod): VdomElement

  def gotoChangeSets(changeSetId: Long): VdomElement

  def gotoChangeSet(changeSetId: Long, replicationNumber: Int): VdomElement

  def gotoChangeSetNetwork(changeSetId: Long, xxx: (Int, Long), title: String): VdomElement

  def gotoChanges(): VdomElement

}
