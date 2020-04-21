package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.node.ArrayNode
import kpn.api.custom.Tags
import org.apache.commons.text.StringEscapeUtils

class TagsJsonDeserializer extends JsonDeserializer[Tags] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Tags = {
    val treeNode: TreeNode = jsonParser.getCodec.readTree(jsonParser)
    val pairs: Seq[(String, String)] = treeNode match {
      case arrayNode: ArrayNode =>
        import scala.jdk.CollectionConverters._
        arrayNode.elements().asScala.map {
          case pairArrayNode: ArrayNode if pairArrayNode.size() == 2 =>
            val key = StringEscapeUtils.unescapeJson(pairArrayNode.get(0).textValue)
            val value = StringEscapeUtils.unescapeJson(pairArrayNode.get(1).textValue)
            key -> value
          case _ =>
            throw JsonMappingException.from(
              jsonParser,
              "Cannot deserialize tags"
            )
        }.toSeq

      case _ =>
        throw JsonMappingException.from(
          jsonParser,
          "Cannot deserialize tags"
        )
    }
    Tags.from(pairs: _*)
  }
}
