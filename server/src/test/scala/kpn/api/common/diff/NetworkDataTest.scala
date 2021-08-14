package kpn.api.common.diff

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MetaData
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.server.json.Json

// TODO MONGO remove after migration
class NetworkDataTest extends UnitTest with SharedTestObjects {

  test("backward compatibility for json deserialization") {

    val networkData = NetworkData(
      MetaData(
        version = 4,
        timestamp = Timestamp(2020, 8, 11, 1, 2, 3),
        changeSetId = 567L
      ),
      "network-name"
    )

    println(Json.objectMapper.writeValueAsString(networkData))

    Json.objectMapper.readValue(oldJson, classOf[NetworkData]) should equal(
      networkData
    )
  }

  private def oldJson: String = {
    """
      {
        "relation": {
          "id": 123,
          "version": 4,
          "timestamp": "2020-08-11T01:02:03Z",
          "changeSetId": 567,
          "members": [
            {
              "memberType": "node",
              "ref": 101,
              "node": true,
              "way": false,
              "relation": false
            },
            {
              "memberType": "way",
              "ref": 11,
              "node": false,
              "way": true,
              "relation": false
            }
          ],
          "tags": {
            "tags": [
              {
                "key": "a",
                "value": "b"
              }
            ]
          },
          "node": false,
          "way": false,
          "relation": true
        },
        "name": "network-name"
      }
    """
  }
}
