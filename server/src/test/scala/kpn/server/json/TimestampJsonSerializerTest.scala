package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.Timestamp
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Test
@RunWith(classOf[SpringRunner])
@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class TimestampJsonSerializerTest {

  @Autowired
  var objectMapper: ObjectMapper = _

  @Test
  def testSerialization(): Unit = {
    val timestamp = Timestamp(2018, 8, 11, 12, 34, 56)
    val json = objectMapper.writeValueAsString(timestamp)
    assertEquals(""""2018-08-11T12:34:56Z"""", json)
  }
}
