package kpn.client.components.node

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.UiSmallMap
import kpn.client.components.common.GlobalStyles
import kpn.client.components.common.UiDetail
import kpn.client.components.common.UiFactDiffs
import kpn.client.components.common.UiTagDiffs
import kpn.client.components.shared.UiChangeSetTags
import kpn.shared.common.Ref
import kpn.shared.node.NodeChangeInfo

import scalacss.ScalaCssReact._

object UiNodeChangeDetail {

  private case class Props(context: Context, key: String, nodeChange: NodeChangeInfo)

  private val component = ScalaComponent.builder[Props]("node-change-detail")
    .render_P { props =>
      new Renderer(props.key, props.nodeChange)(props.context).render()
    }
    .build

  def apply(key: String, nodeChange: NodeChangeInfo)(implicit context: Context): VdomElement = {
    component(Props(context, key, nodeChange))
  }

  private class Renderer(key: String, nodeChange: NodeChangeInfo)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        facts,
        connectionChanges,
        roleConnectionChanges,
        definedInNetworkChanges,
        referenceChanges(nodeChange.addedToRoute, nls("Added to route", "Toegevoegd aan route"), routeLink),
        referenceChanges(nodeChange.addedToNetwork, nls("Added to network", "Toegevoegd aan netwerk"), networkLink),
        referenceChanges(nodeChange.removedFromRoute, nls("Removed from route", "Verwijderd uit route"), routeLink),
        referenceChanges(nodeChange.removedFromNetwork, nls("Removed from network", "Verwijderd uit netwerk"), networkLink),
        factDiffs,
        tagDiffs,
        nodeMoved
      )
    }

    private def changeSetTags: TagMod = {
      TagMod.when(nodeChange.changeTags.nonEmpty) {
        UiChangeSetTags(nodeChange.changeTags)
      }
    }

    private def version: TagMod = {
      nodeChange.version.whenDefined { version =>
        UiDetail(
          nls("Version", "Versie") + " " + version
        )
      }
    }

    private def definedInNetworkChanges: TagMod = {
      TagMod.when(nodeChange.definedInNetworkChanges.nonEmpty) {
        <.div(
          nodeChange.definedInNetworkChanges.toTagMod { refBooleanChange =>
            val text = if (refBooleanChange.after) {
              nls("Added to network relation of ", "Toegevoegd aan netwerkrelatie van")
            }
            else {
              nls("Removed from network relation of ", "Verwijderd uit netwerkrelatie van")
            }
            networkMessage(text, refBooleanChange.ref)
          }
        )
      }
    }

    private def connectionChanges = {
      TagMod.when(nodeChange.connectionChanges.nonEmpty) {
        nodeChange.connectionChanges.toTagMod { refBooleanChange =>
          val text = if (refBooleanChange.after) {
            nls(
              """This node belongs to another network.""",
              """Dit knooppunt behoort tot een ander netwerk."""
            )
          }
          else {
            nls(
              """This node is no longer belongs to another network.""",
              """Dit knooppunt behoort niet langer tot een ander netwerk."""
            )
          }
          networkMessage(text, refBooleanChange.ref)
        }
      }
    }

    private def roleConnectionChanges = {
      TagMod.when(nodeChange.roleConnectionChanges.nonEmpty) {
        nodeChange.roleConnectionChanges.toTagMod { refBooleanChange =>
          val text = if (refBooleanChange.after) {
            nls(
              """This node has role "connection" in the network relation.""",
              """Dit knooppunt heeft rol "connection" in de netwerk relatie."""
            )
          }
          else {
            nls(
              """This node is no longer has role "connection" in the network relation.""",
              """Dit knooppunt heeft niet langer de rol "connectinon" in de netwerk relatie."""
            )
          }
          networkMessage(text, refBooleanChange.ref)
        }
      }
    }

    private def factDiffs = {
      TagMod.when(nodeChange.factDiffs.nonEmpty) {
        UiFactDiffs(nodeChange.factDiffs)
      }
    }

    private def facts = {
      nodeChange.facts.toTagMod { fact =>
        UiDetail(
          nls(fact.name, fact.nlName)
        )
      }
    }

    private def tagDiffs = {
      nodeChange.tagDiffs.whenDefined{ tagDiffs =>
        UiDetail(
          UiTagDiffs(tagDiffs)
        )
      }
    }

    private def nodeMoved = {
      nodeChange.nodeMoved.whenDefined { nodeMoved =>
        <.div(
          TagMod.when(nodeMoved.distance > 0) {
            UiDetail(
              nls(
                s"The node has moved ${nodeMoved.distance} meters.",
                s"Het knooppunt is ${nodeMoved.distance} meter verplaatst."
              )
            )
          },
          TagMod.when(nodeMoved.distance == 0) {
            UiDetail(
              nls(
                s"The node has moved less than 1 meter.",
                s"Het knooppunt is minder dan 1 meter verplaatst."
              )
            )
          },
          UiDetail(
            <.div(
              UiSmallMap(new NodeHistoryMap("map-" + key, nodeMoved.after, nodeMoved.before)),
              <.div(
                GlobalStyles.note,
                nls(
                  s"Note: Node position is shown as it was at ${nodeChange.changeKey.timestamp.yyyymmddhhmm}, while the map background is shown as it is today.",
                  s"Noot: De knooppunt positie wordt getoond zoals die was op ${nodeChange.changeKey.timestamp.yyyymmddhhmm}, de kaart achtergrond toont " +
                    s"de toestand van vandaag."
                )
              )
            )
          )
        )
      }
    }

    private def referenceChanges(refs: Seq[Ref], title: String, link: (Ref) => VdomElement): TagMod = {
      TagMod.when(refs.nonEmpty) {
        refs.toTagMod { ref =>
          UiDetail(
            <.div(
              title + ": ",
              link(ref)
            )
          )
        }
      }
    }

    private def networkMessage(string: String, networkRef: Ref): TagMod = {
      UiDetail(
        <.div(
          string + " ",
          networkLink(networkRef),
          "."
        )
      )
    }

    private def routeLink(ref: Ref): VdomElement = {
      context.gotoRoute(ref.id, ref.name)
    }

    private def networkLink(ref: Ref): VdomElement = {
      context.gotoNetworkDetails(ref.id, ref.name)
    }
  }

}
