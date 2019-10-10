package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.NetworkType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Test
@RunWith(classOf[SpringRunner])
@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class NetworkTypeJsonDeserializerTest {

  @Autowired
  var objectMapper: ObjectMapper = _

  @Test
  def testDeserialization(): Unit = {
    val networkType = objectMapper.readValue(""""rcn"""", classOf[NetworkType])
    assertEquals(NetworkType.bicycle, networkType)
  }
}
