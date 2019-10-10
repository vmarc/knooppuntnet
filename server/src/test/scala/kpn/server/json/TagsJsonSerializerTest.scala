package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.data.Tags
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Test
@RunWith(classOf[SpringRunner])
@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class TagsJsonSerializerTest {

  @Autowired
  var objectMapper: ObjectMapper = _

  @Test
  def testSerialization(): Unit = {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = objectMapper.writeValueAsString(tags)
    assertEquals("""[["key1","value1"],["key2","value2"],["key3","value3"]]""", json)
  }
}
