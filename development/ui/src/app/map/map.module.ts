import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MapSidebarComponent} from "./sidebar/_map-sidebar.component";
import {SharedModule} from "../components/shared/shared.module";
import {MapPageComponent} from "./pages/map/_map-page.component";
import {MapMainPageComponent} from "./pages/map/map-main-page.component";
import {OlModule} from "../components/ol/ol.module";
import {MapDetailDefaultComponent} from "./pages/map/map-detail-default.component";
import {MapDetailNodeComponent} from "./pages/map/map-detail-node.component";
import {MapDetailRouteComponent} from "./pages/map/map-detail-route.component";
import {MapRoutingModule} from "./map-routing.module";
import {KpnMaterialModule} from "../material/kpn-material.module";
import {MapPoiConfigComponent} from "./pages/map/poi/map-poi-config.component";
import {PoiConfigComponent} from "./pages/map/poi/poi-config.component";
import {ReactiveFormsModule} from "@angular/forms";
import {MatRadioModule} from "@angular/material";
import {PoiGroupAmenityComponent} from "./pages/map/poi/poi-group-amenity.component";
import {PoiGroupFoodShopsComponent} from "./pages/map/poi/poi-group-food-shops.component";
import {PoiGroupPlacesToStayComponent} from "./pages/map/poi/poi-group-places-to-stay.component";
import {PoiGroupRestaurantsComponent} from "./pages/map/poi/poi-group-restaurants.component";
import {PoiGroupShopsComponent} from "./pages/map/poi/poi-group-shops.component";
import {PoiGroupSportsComponent} from "./pages/map/poi/poi-group-sports.component";
import {PoiGroupTourismComponent} from "./pages/map/poi/poi-group-tourism.component";
import {PoiGroupVariousComponent} from "./pages/map/poi/poi-group-various.component";
import {PoiGroupComponent} from "./pages/map/poi/poi-group.component";
import {NetworkTypeSelectorComponent} from "./sidebar/network-type-selector.component";
import {PoiDetailComponent} from "./sidebar/poi-detail.component";
import {MapSidebarPlannerComponent} from "./sidebar/map-side-bar-planner.component";
import {MapSidebarAnalysisComponent} from "./sidebar/map-side-bar-analysis.component";
import {MapSidebarLegendComponent} from "./sidebar/map-side-bar-legend.component";
import {MapSidebarAppearanceComponent} from "./sidebar/map-side-bar-appearance.component";
import {MapSidebarPoiConfigurationComponent} from "./sidebar/map-side-bar-poi-configuration.component";
import {PoiNameComponent} from "./pages/map/poi/poi-name.component";
import {PoiNamesComponent} from "./pages/map/poi/poi-names.component";

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule,
    MapRoutingModule,
    MatRadioModule,
    ReactiveFormsModule,
    SharedModule,
    OlModule
  ],
  declarations: [
    MapPageComponent,
    MapMainPageComponent,
    MapSidebarComponent,
    MapSidebarPlannerComponent,
    MapSidebarAnalysisComponent,
    MapSidebarLegendComponent,
    MapSidebarAppearanceComponent,
    MapSidebarPoiConfigurationComponent,
    MapDetailDefaultComponent,
    MapDetailNodeComponent,
    MapDetailRouteComponent,
    MapPoiConfigComponent,
    PoiGroupComponent,
    PoiGroupAmenityComponent,
    PoiGroupFoodShopsComponent,
    PoiGroupPlacesToStayComponent,
    PoiGroupRestaurantsComponent,
    PoiGroupShopsComponent,
    PoiGroupSportsComponent,
    PoiGroupTourismComponent,
    PoiGroupVariousComponent,
    PoiConfigComponent,
    PoiNameComponent,
    PoiNamesComponent,
    NetworkTypeSelectorComponent,
    PoiDetailComponent
  ],
  exports: [
    MapPageComponent
  ]
})
export class MapModule {
}
