package kpn.server.json

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class NodeNameJsonDeserializerTest extends UnitTest {

  test("deserializer") {
    val nodeName = Json.value("""{"networkType":"hiking","networkScope":"regional","name":"01"}""", classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.hiking, NetworkScope.regional, "01"))
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
        |        "name": "22"
        |      }
        |""".stripMargin
    val nodeName = Json.value(serialized, classOf[NodeName])
    nodeName should equal(NodeName(NetworkType.cycling, NetworkScope.regional, "22"))
  }
}