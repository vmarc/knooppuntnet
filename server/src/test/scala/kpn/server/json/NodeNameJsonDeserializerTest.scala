package kpn.server.json

import kpn.api.common.RouteScope
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class NodeNameJsonDeserializerTest extends UnitTest {

  test("deserializer") {
    val nodeName = Json.value("""{"routeType":"hiking","routeScope":"regional","name":"01","longName":"long name","proposed":true}""", classOf[NodeName])
    nodeName should equal(NodeName(RouteType.hiking, RouteScope.regional, "01", Some("long name"), proposed = true))
  }

  test("deserializer - no long name") {
    val nodeName = Json.value("""{"routeType":"hiking","routeScope":"regional","name":"01","proposed":false}}""", classOf[NodeName])
    nodeName should equal(NodeName(RouteType.hiking, RouteScope.regional, "01", None, proposed = false))
  }

  test("deserializer backward compatibility - proposed field missing") {
    val nodeName = Json.value("""{"routeType":"hiking","routeScope":"regional","name":"01","longName":"long name"}""", classOf[NodeName])
    nodeName should equal(NodeName(RouteType.hiking, RouteScope.regional, "01", Some("long name"), proposed = false))
  }

  test("deserializer backward compatibility") {
    val serialized =
      """
        |      {
        |        "scopedRouteType": {
        |          "routeScope": "regional",
        |          "routeType": "cycling",
        |          "key": "rcn"
        |        },
        |        "name": "22",
        |        "longName": "long name"
        |      }
        |""".stripMargin
    val nodeName = Json.value(serialized, classOf[NodeName])
    nodeName should equal(NodeName(RouteType.cycling, RouteScope.regional, "22", Some("long name"), proposed = false))
  }
}
