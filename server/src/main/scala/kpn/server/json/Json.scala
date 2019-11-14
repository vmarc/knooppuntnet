package kpn.server.json

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import kpn.api.common.route.WayDirection
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.database.doc.ChangeSetDatas
import org.locationtech.jts.geom.Geometry
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

object Json {

  val objectMapper: ObjectMapper = {

    val b = Jackson2ObjectMapperBuilder.json()
    b.featuresToEnable(DeserializationFeature.USE_LONG_FOR_INTS)
    b.serializationInclusion(NON_ABSENT)
    b.annotationIntrospector(new JacksonAnnotationIntrospector)

    b.deserializerByType(classOf[Country], new CountryJsonDeserializer())
    b.serializerByType(classOf[Country], new CountryJsonSerializer())

    b.deserializerByType(classOf[Fact], new FactJsonDeserializer())
    b.serializerByType(classOf[Fact], new FactJsonSerializer())

    b.deserializerByType(classOf[NetworkType], new NetworkTypeJsonDeserializer())
    b.serializerByType(classOf[NetworkType], new NetworkTypeJsonSerializer())

    b.deserializerByType(classOf[NetworkScope], new NetworkScopeJsonDeserializer())
    b.serializerByType(classOf[NetworkScope], new NetworkScopeJsonSerializer())

    b.deserializerByType(classOf[Subset], new SubsetJsonDeserializer())
    b.serializerByType(classOf[Subset], new SubsetJsonSerializer())

    b.deserializerByType(classOf[Tags], new TagsJsonDeserializer())
    b.serializerByType(classOf[Tags], new TagsJsonSerializer())

    b.deserializerByType(classOf[Timestamp], new TimestampJsonDeserializer())
    b.serializerByType(classOf[Timestamp], new TimestampJsonSerializer())

    b.deserializerByType(classOf[WayDirection], new WayDirectionJsonDeserializer())
    b.serializerByType(classOf[WayDirection], new WayDirectionJsonSerializer())

    b.deserializerByType(classOf[ChangeSetDatas], new ChangeSetDatasJsonDeserializer())
    b.deserializerByType(classOf[Geometry], new GeometryJsonDeserializer())

    val om: ObjectMapper = b.build()
    om.registerModule(DefaultScalaModule)
    om
  }

  def string(o: Object): String = {
    objectMapper.writeValueAsString(o)
  }

  def value[T](string: String, valueType: Class[T]): T = {
    objectMapper.readValue(string, valueType)

  }
}
