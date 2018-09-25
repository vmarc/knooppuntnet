package kpn.client.components.network.nodes.indicators

import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiIndicator

object NetworkIndicator {

  def apply(definedInRelation: Boolean, connection: Boolean, roleConnection: Boolean)(implicit context: Context): VdomElement = {
    if (definedInRelation) {
      if (connection && !roleConnection) {
        UiIndicator(
          "N",
          UiIndicator.Color.ORANGE,
          nls(
            "Unexpected - Defined in network relation",
            "Overwacht - Opgenomen in netwerk relatie"
          ),
          nls(
            """This node is included as a member in the network relation. We did not expect this,
              | because all routes to this node have role "connection". This would mean that the
              | node is part of another network. We expect that the node is not included in the
              |  network relation, unless it receives the role "connection".""".stripMargin,
            """Dit knooppunt is opgenomen als lid in een netwerk relatie. Dit is onverwacht omdat
              | alle routes naar deze node de rol "connection" hebben. Dit zou betekenen dat dit
              | knooppunt deel uitmaakt van een ander netwerk. We verwachten dat het knooppunt niet
              | opgenomen wordt in de netwerk relatie, behalve wanneer het ook
              | de rol "connection" meekrijgt.""".stripMargin
          )
        )
      }
      else {
        UiIndicator(
          "N",
          UiIndicator.Color.GREEN,
          nls(
            "OK - Defined in network relation",
            "OK - Opgenomen in netwerk relatie"
          ),
          nls(
            "This node is included as a member in the network relation. This is what we expect.",
            "Dit knooppunt is opgenomen als lid in een netwerk relatie. Dit is wat we verwachten."
          )
        )
      }
    }
    else {
      if (connection) {
        UiIndicator(
          "N",
          UiIndicator.Color.GRAY,
          nls(
            "OK - Not defined in network relation",
            "OK - Niet opgenomen in netwerk relatie"
          ),
          nls(
            """This node is not included as a member in the network relation. This is OK. This node
              | must belong to a different network, because all routes to this node within this network
              | have the role "connection" in the network relation.""".stripMargin,
            """Dit knooppunt is niet opgenomen als lid in de netwerk relatie. Dit is OK. Dit knooppunt
              | moet behoren tot een ander netwerk, want alle routes naar dit knooppunt binnen dit
              | netwerk hebben de rol "connection" in de netwerk relatie.""".stripMargin
          )
        )
      }
      else {
        UiIndicator(
          "N",
          UiIndicator.Color.RED,
          nls(
            "NOK - Not defined in network relation",
            "NOK - Niet opgenomen in netwerk relatie"
          ),
          nls(
            """This node is not included as a member in the network relation. This is not OK. The
              | convention is to include each node in the network relation, except when the node
              | belongs to another network (all routes to this node have role "connection" in
              | the network relation).""".stripMargin,
            """Dit knooppunt is niet opgenomen als lid in een netwerk relatie. Dit is niet OK. De
              | conventie is om elk knooppunt op te nemen in een netwerk relatie, behalve wanneer
              | wanneer het knooppunt tot een ander netwerk behoort (alle routes naar dit knooppunt
              | hebben de rol "connection" in de netwerk relatie).""".stripMargin
          )
        )
      }
    }
  }
}
