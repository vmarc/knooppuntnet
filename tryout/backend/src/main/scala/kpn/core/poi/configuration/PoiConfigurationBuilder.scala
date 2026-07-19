package kpn.core.poi.configuration

object PoiConfigurationBuilder {

  val groupBuilders: Seq[PoiGroupBuilder] = Seq(
    PoiGroupHikingBiking.builder,
    PoiGroupLandmarks.builder,
    PoiGroupRestaurants.builder,
    PoiGroupPlacesToStay.builder,
    PoiGroupTourism.builder,
    PoiGroupAmenity.builder,
    PoiGroupShops.builder,
    PoiGroupFoodshops.builder,
    PoiGroupSports.builder
  )
}
