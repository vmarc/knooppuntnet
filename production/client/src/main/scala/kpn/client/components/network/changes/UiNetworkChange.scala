// TODO migrate to Angular
package kpn.client.components.network.changes

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChangeInfo

object UiNetworkChange {

  private case class Props(context: Context, networkChangeInfo: NetworkChangeInfo)

  private val component = ScalaComponent.builder[Props]("network-update-summary")
    .render_P { props =>
      new Renderer(props.networkChangeInfo)(props.context).render()
    }
    .build

  def apply(networkChangeInfo: NetworkChangeInfo)(implicit context: Context): VdomElement = {
    component(Props(context, networkChangeInfo))
  }

  private class Renderer(networkChangeInfo: NetworkChangeInfo)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        changeType,
        networkNodesAdded,
        routesAdded,
        nodesAdded,
        waysAdded,
        relationsAdded,
        networkDataUpdate,
        networkNodesUpdated,
        routesUpdated,
        nodesUpdated,
        waysUpdated,
        relationsUpdated,
        networkNodesRemoved,
        routesRemoved,
        nodesRemoved,
        waysRemoved,
        relationsRemoved
      )
    }

    private def changeType: TagMod = {
      <.div(
        <.b(
          networkChangeInfo.changeType match {
            case ChangeType.Create =>
              nls(
                "Network created",
                "Netwerk nieuw aangemaakt"
              )

            case ChangeType.Delete =>
              nls(
                "Network deleted",
                "Netwerk verwijderd"
              )

            case _ =>
              ""
          }
        )
      )
    }

    private def networkNodesAdded: TagMod = {
      TagMod.when(networkChangeInfo.networkNodes.added.nonEmpty) {
        <.div(
          nls(
            "Added node(s)",
            "Toegevoegde knooppunten"
          ),
          ": " + networkChangeInfo.networkNodes.added.map(_.name).mkString(", ")
        )
      }
    }

    private def routesAdded: TagMod = {
      TagMod.when(networkChangeInfo.routes.added.nonEmpty) {
        <.div(
          nls(
            "Added route(s)",
            "Toegevoegde route(s)"
          ),
          ": ",
          networkChangeInfo.routes.added.map(_.name).mkString(", ")
        )
      }
    }

    private def nodesAdded: TagMod = {
      TagMod.when(networkChangeInfo.nodes.added.nonEmpty) {
        <.div(
          nls(
            "Added non-network node member(s) in network relation",
            "Niet-network-knooppunt(en) toegevoegd in netwerk relatie"
          )
        )
      }
    }

    private def waysAdded: TagMod = {
      TagMod.when(networkChangeInfo.ways.added.nonEmpty) {
        <.div(
          nls(
            "Added way member(s) in network relation",
            "Weg(en) toegevoegd in netwerk relatie"
          )
        )
      }
    }

    private def relationsAdded: TagMod = {
      TagMod.when(networkChangeInfo.relations.added.nonEmpty) {
        <.div(
          nls(
            "Added non-route relation(s) in network relation",
            "Niet-route relatie(s) toegevoegd in netwerk relatie"
          )
        )
      }
    }

    private def networkDataUpdate: TagMod = {
      if (networkChangeInfo.networkDataUpdated) {
        <.div(
          nls(
            "Updated network relation",
            "Netwerk relatie aangepast"
          )
        )
      }
      else {
        TagMod()
      }
    }

    private def networkNodesUpdated: TagMod = {
      TagMod.when(networkChangeInfo.networkNodes.updated.nonEmpty) {
        <.div(
          nls(
            "Updated network node(s)",
            "Knooppunt(en) aangepast"
          ),
          ": ",
          networkChangeInfo.networkNodes.updated.map(_.name).mkString(", "))
      }
    }

    private def routesUpdated: TagMod = {
      TagMod.when(networkChangeInfo.routes.updated.nonEmpty) {
        <.div(
          nls(
            "Updated route(s)",
            "Route(s) aangepast"
          ),
          ": ",
          networkChangeInfo.routes.updated.map(_.name).mkString(", ")
        )
      }
    }

    private def nodesUpdated: TagMod = {
      TagMod.when(networkChangeInfo.nodes.updated.nonEmpty) {
        <.div(
          nls(
            "Updated non-network node(s)",
            "Niet-netwerk-knooppunt(en) aangepast"
          )
        )
      }
    }

    private def waysUpdated: TagMod = {
      TagMod.when(networkChangeInfo.ways.updated.nonEmpty) {
        <.div(
          nls(
            "Updated way member(s)",
            "Weg aangepast"
          )
        )
      }
    }

    private def relationsUpdated: TagMod = {
      TagMod.when(networkChangeInfo.relations.updated.nonEmpty) {
        <.div(
          nls(
            "Updated non-route relation(s)",
            "Niet-route relatie aangepast"
          )
        )
      }
    }

    private def networkNodesRemoved: TagMod = {
      TagMod.when(networkChangeInfo.networkNodes.removed.nonEmpty) {
        <.div(
          nls(
            "Removed network node(s)",
            "Network knooppunt(en) verwijderd"
          ),
          ": ",
          networkChangeInfo.networkNodes.removed.map(_.name).mkString(", ")
        )
      }
    }

    private def routesRemoved: TagMod = {
      TagMod.when(networkChangeInfo.routes.removed.nonEmpty) {
        <.div(
          nls(
            "Removed route(s): ",
            "Verwijderde route(s)"
          ),
          networkChangeInfo.routes.removed.map(_.name).mkString(", ")
        )
      }
    }

    private def nodesRemoved: TagMod = {
      TagMod.when(networkChangeInfo.nodes.removed.nonEmpty) {
        <.div(
          nls(
            "Removed non-network node member(s) from network relation",
            "Knooppunt dat geen networkknooppunt is verwijderd uit netwerk relatie"
          )
        )
      }
    }

    private def waysRemoved: TagMod = {
      TagMod.when(networkChangeInfo.ways.removed.nonEmpty) {
        <.div(
          nls(
            "Removed way member(s) from network relation",
            "Weg(en) verwijderd uit netwerk relatie"
          )
        )
      }
    }

    private def relationsRemoved: TagMod = {
      TagMod.when(networkChangeInfo.relations.removed.nonEmpty) {
        <.div(
          nls(
            "Removed non-route relation(s) from network relation",
            "Niet-route relatie(s) verwijderd uit netwerk relatie"
          )
        )
      }
    }
  }

}
