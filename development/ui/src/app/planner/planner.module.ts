import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PlannerSidebarComponent} from "./planner-sidebar.component";
import {SharedModule} from "../shared/shared.module";
import {MapPageComponent} from "./pages/map/map-page.component";
import {MapModule} from "../map/map.module";
import {MapDetailDefaultComponent} from "./pages/map/map-detail-default.component";
import {MapDetailNodeComponent} from "./pages/map/map-detail-node.component";
import {MapDetailRouteComponent} from "./pages/map/map-detail-route.component";
import {RouterModule} from "@angular/router";
import {PlannerRoutingModule} from "./planner-routing.module";

@NgModule({
  imports: [
    CommonModule,
    PlannerRoutingModule,
    SharedModule,
    MapModule
  ],
  declarations: [
    MapPageComponent,
    PlannerSidebarComponent,
    MapDetailDefaultComponent,
    MapDetailNodeComponent,
    MapDetailRouteComponent
  ],
  exports: [
    MapPageComponent
  ]
})
export class PlannerModule {
}
