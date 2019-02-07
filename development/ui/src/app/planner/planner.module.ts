import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PlannerSidebarComponent} from "./sidebar/_planner-sidebar.component";
import {SharedModule} from "../components/shared/shared.module";
import {MapPageComponent} from "./pages/map/map-page.component";
import {MapModule} from "../components/map/map.module";
import {MapDetailDefaultComponent} from "./pages/map/map-detail-default.component";
import {MapDetailNodeComponent} from "./pages/map/map-detail-node.component";
import {MapDetailRouteComponent} from "./pages/map/map-detail-route.component";
import {PlannerRoutingModule} from "./planner-routing.module";
import {PlannerPageComponent} from "./pages/planner/planner-page.component";
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

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule,
    PlannerRoutingModule,
    MatRadioModule,
    ReactiveFormsModule,
    SharedModule,
    MapModule
  ],
  declarations: [
    PlannerPageComponent,
    MapPageComponent,
    PlannerSidebarComponent,
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
    NetworkTypeSelectorComponent
  ],
  exports: [
    MapPageComponent
  ]
})
export class PlannerModule {
}
