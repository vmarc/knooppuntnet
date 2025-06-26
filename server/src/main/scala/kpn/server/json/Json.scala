package kpn.server.json

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import kpn.api.base.ObjectId
import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.ElementChangeType
import kpn.api.common.Fact
import kpn.api.common.FeatureLayer
import kpn.api.common.Language
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.MemberType
import kpn.api.common.diff.TagDiffType
import kpn.api.common.location.BooleanParameter
import kpn.api.common.location.LastUpdatedParameter
import kpn.api.common.location.SurveyParameter
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.planner.PlanCoordinate
import kpn.api.common.route.LinkDirection
import kpn.api.common.route.WayDirection
import kpn.api.common.search.ConditionGroupOperator
import kpn.api.common.search.ConditionOperator
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.api.custom.Timestamp2
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.enumeratum.AnalysisStrategyJsonDeserializer
import kpn.server.json.enumeratum.AnalysisStrategyJsonSerializer
import kpn.server.json.enumeratum.BooleanParameterJsonDeserializer
import kpn.server.json.enumeratum.BooleanParameterJsonSerializer
import kpn.server.json.enumeratum.ChangeTypeJsonDeserializer
import kpn.server.json.enumeratum.ChangeTypeJsonSerializer
import kpn.server.json.enumeratum.ConditionGroupOperatorJsonDeserializer
import kpn.server.json.enumeratum.ConditionGroupOperatorJsonSerializer
import kpn.server.json.enumeratum.ConditionOperatorJsonDeserializer
import kpn.server.json.enumeratum.ConditionOperatorJsonSerializer
import kpn.server.json.enumeratum.CountryJsonDeserializer
import kpn.server.json.enumeratum.CountryJsonSerializer
import kpn.server.json.enumeratum.ElementChangeTypeJsonDeserializer
import kpn.server.json.enumeratum.ElementChangeTypeJsonSerializer
import kpn.server.json.enumeratum.FactJsonDeserializer
import kpn.server.json.enumeratum.FactJsonSerializer
import kpn.server.json.enumeratum.FeatureLayerJsonDeserializer
import kpn.server.json.enumeratum.FeatureLayerJsonSerializer
import kpn.server.json.enumeratum.LanguageJsonDeserializer
import kpn.server.json.enumeratum.LanguageJsonSerializer
import kpn.server.json.enumeratum.LastUpdatedParameterJsonDeserializer
import kpn.server.json.enumeratum.LastUpdatedParameterJsonSerializer
import kpn.server.json.enumeratum.LinkDirectionJsonDeserializer
import kpn.server.json.enumeratum.LinkDirectionJsonSerializer
import kpn.server.json.enumeratum.MemberTypeJsonDeserializer
import kpn.server.json.enumeratum.MemberTypeJsonSerializer
import kpn.server.json.enumeratum.MonitorReferenceTypeJsonDeserializer
import kpn.server.json.enumeratum.MonitorReferenceTypeJsonSerializer
import kpn.server.json.enumeratum.RouteScopeJsonDeserializer
import kpn.server.json.enumeratum.RouteScopeJsonSerializer
import kpn.server.json.enumeratum.RouteTypeJsonDeserializer
import kpn.server.json.enumeratum.RouteTypeJsonSerializer
import kpn.server.json.enumeratum.SurveyParameterJsonDeserializer
import kpn.server.json.enumeratum.SurveyParameterJsonSerializer
import kpn.server.json.enumeratum.TagDiffTypeJsonDeserializer
import kpn.server.json.enumeratum.TagDiffTypeJsonSerializer
import kpn.server.json.enumeratum.WayDirectionJsonDeserializer
import kpn.server.json.enumeratum.WayDirectionJsonSerializer
import org.locationtech.jts.geom.Geometry
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

object Json {

  val objectMapper: ObjectMapper = buildObjectMapper(false)
  val mongoObjectMapper: ObjectMapper = buildObjectMapper(true)

  private val prettyJsonWriter = {
    val printer = new DefaultPrettyPrinter().withArrayIndenter(new DefaultIndenter())
    Json.objectMapper.writer(printer)
  }

  def value[T](string: String, valueType: Class[T]): T = {
    objectMapper.readValue(string, valueType)
  }

  def string(o: Object): String = {
    objectMapper.writeValueAsString(o)
  }

  def pretty(o: Object): String = {
    prettyJsonWriter.writeValueAsString(o)
  }

  private def buildObjectMapper(mongo: Boolean): ObjectMapper = {

    val b = Jackson2ObjectMapperBuilder.json()
    b.featuresToEnable(DeserializationFeature.USE_LONG_FOR_INTS)
    b.serializationInclusion(NON_ABSENT)
    b.annotationIntrospector(new JacksonAnnotationIntrospector)

    b.deserializerByType(classOf[Country], new CountryJsonDeserializer())
    b.serializerByType(classOf[Country], new CountryJsonSerializer())

    b.deserializerByType(classOf[Fact], new FactJsonDeserializer())
    b.serializerByType(classOf[Fact], new FactJsonSerializer())

    b.deserializerByType(classOf[Language], new LanguageJsonDeserializer())
    b.serializerByType(classOf[Language], new LanguageJsonSerializer())

    b.deserializerByType(classOf[FeatureLayer], new FeatureLayerJsonDeserializer())
    b.serializerByType(classOf[FeatureLayer], new FeatureLayerJsonSerializer())

    b.deserializerByType(classOf[LinkDirection], new LinkDirectionJsonDeserializer())
    b.serializerByType(classOf[LinkDirection], new LinkDirectionJsonSerializer())

    b.deserializerByType(classOf[RouteType], new RouteTypeJsonDeserializer())
    b.serializerByType(classOf[RouteType], new RouteTypeJsonSerializer())

    b.deserializerByType(classOf[ScopedRouteType], new ScopedRouteTypeJsonDeserializer())
    b.serializerByType(classOf[ScopedRouteType], new ScopedRouteTypeJsonSerializer())

    b.deserializerByType(classOf[RouteScope], new RouteScopeJsonDeserializer())
    b.serializerByType(classOf[RouteScope], new RouteScopeJsonSerializer())

    b.deserializerByType(classOf[MemberType], new MemberTypeJsonDeserializer())
    b.serializerByType(classOf[MemberType], new MemberTypeJsonSerializer())

    b.deserializerByType(classOf[ConditionGroupOperator], new ConditionGroupOperatorJsonDeserializer())
    b.serializerByType(classOf[ConditionGroupOperator], new ConditionGroupOperatorJsonSerializer())

    b.deserializerByType(classOf[ConditionOperator], new ConditionOperatorJsonDeserializer())
    b.serializerByType(classOf[ConditionOperator], new ConditionOperatorJsonSerializer())

    b.deserializerByType(classOf[ChangeType], new ChangeTypeJsonDeserializer())
    b.serializerByType(classOf[ChangeType], new ChangeTypeJsonSerializer())

    b.deserializerByType(classOf[ChangeType], new ChangeTypeJsonDeserializer())
    b.serializerByType(classOf[ChangeType], new ChangeTypeJsonSerializer())

    b.deserializerByType(classOf[Subset], new SubsetJsonDeserializer())

    b.deserializerByType(classOf[Timestamp], new TimestampJsonDeserializer(mongo))
    b.serializerByType(classOf[Timestamp], new TimestampJsonSerializer())

    b.serializerByType(classOf[Timestamp2], new Timestamp2JsonSerializer())

    b.deserializerByType(classOf[ObjectId], new ObjectIdJsonDeserializer())
    b.serializerByType(classOf[ObjectId], new ObjectIdJsonSerializer())

    b.deserializerByType(classOf[Day], new DayJsonDeserializer())
    b.serializerByType(classOf[Day], new DayJsonSerializer())

    b.deserializerByType(classOf[WayDirection], new WayDirectionJsonDeserializer())
    b.serializerByType(classOf[WayDirection], new WayDirectionJsonSerializer())

    b.deserializerByType(classOf[Geometry], new GeometryJsonDeserializer())
    b.serializerByType(classOf[Geometry], new GeometryJsonSerializer())

    b.serializerByType(classOf[PlanCoordinate], new PlanCoordinateJsonSerializer())

    b.deserializerByType(classOf[Relation], new RelationJsonDeserializer())

    b.deserializerByType(classOf[SurveyParameter], new SurveyParameterJsonDeserializer())
    b.serializerByType(classOf[SurveyParameter], new SurveyParameterJsonSerializer())

    b.deserializerByType(classOf[BooleanParameter], new BooleanParameterJsonDeserializer())
    b.serializerByType(classOf[BooleanParameter], new BooleanParameterJsonSerializer())

    b.deserializerByType(classOf[LastUpdatedParameter], new LastUpdatedParameterJsonDeserializer())
    b.serializerByType(classOf[LastUpdatedParameter], new LastUpdatedParameterJsonSerializer())

    b.deserializerByType(classOf[ElementChangeType], new ElementChangeTypeJsonDeserializer())
    b.serializerByType(classOf[ElementChangeType], new ElementChangeTypeJsonSerializer())

    b.deserializerByType(classOf[AnalysisStrategy], new AnalysisStrategyJsonDeserializer())
    b.serializerByType(classOf[AnalysisStrategy], new AnalysisStrategyJsonSerializer())

    b.deserializerByType(classOf[TagDiffType], new TagDiffTypeJsonDeserializer())
    b.serializerByType(classOf[TagDiffType], new TagDiffTypeJsonSerializer())

    b.deserializerByType(classOf[MonitorReferenceType], new MonitorReferenceTypeJsonDeserializer())
    b.serializerByType(classOf[MonitorReferenceType], new MonitorReferenceTypeJsonSerializer())

    b.deserializerByType(classOf[CoordinateArray], new CoordinateArrayJsonDeserializer())

    val om: ObjectMapper = b.build()
    om.registerModule(DefaultScalaModule)
    om.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
    om
  }
}
