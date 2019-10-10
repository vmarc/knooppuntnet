package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Test
@RunWith(classOf[SpringRunner])
@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class JsonConfigurationTest {

  @Autowired
  var objectMapper: ObjectMapper = _

  @Test
  def testCaseClassJson(): Unit = {
    val example = JsonExample("John Doe", 123)
    val json = objectMapper.writeValueAsString(example)
    assertEquals("""{"name":"John Doe","age":123}""", json)
    assertEquals(example, objectMapper.readValue(json, classOf[JsonExample]))
  }
}
