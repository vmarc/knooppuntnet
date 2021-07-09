package kpn.server.json

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class NodeNameJsonDeserializerTest extends UnitTest {

  test("deserializer") {
    val nodeName = Json.value("""{"networkType":"hiking","networkScope":"regional","name":"01","longName":"long name","proposed":true}""", classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.hiking, NetworkScope.regional, "01", Some("long name"), proposed = true))
  }

  test("deserializer - no long name") {
    val nodeName = Json.value("""{"networkType":"hiking","networkScope":"regional","name":"01","proposed":false}}""", classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.hiking, NetworkScope.regional, "01", None, proposed = false))
  }

  test("deserializer backward compatibility - proposed field missing") {
    val nodeName = Json.value("""{"networkType":"hiking","networkScope":"regional","name":"01","longName":"long name"}""", classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.hiking, NetworkScope.regional, "01", Some("long name"), proposed = false))
  }

  test("deserializer backward compatibility") {
    val serialized =
      """
        |      {
        |        "scopedNetworkType": {
        |          "networkScope": "regional",
        |          "networkType": "cycling",
        |          "key": "rcn"
        |        },
        |        "name": "22",
        |        "longName": "long name"
        |      }
        |""".stripMargin
    val nodeName = Json.value(serialized, classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.cycling, NetworkScope.regional, "22", Some("long name"), proposed = false))
  }
}
