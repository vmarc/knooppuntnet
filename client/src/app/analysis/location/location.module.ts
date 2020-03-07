import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatDividerModule} from "@angular/material/divider";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {SharedModule} from "../../components/shared/shared.module";
import {LocationRoutingModule} from "./location-routing.module";
import {LocationChangesPageComponent} from "./changes/location-changes-page.component";
import {LocationChangesComponent} from "./changes/location-changes.component";
import {LocationPageBreadcrumbComponent} from "./components/location-page-breadcrumb.component";
import {LocationPageHeaderComponent} from "./components/location-page-header.component";
import {LocationResponseComponent} from "./components/location-response.component";
import {LocationFactsPageComponent} from "./facts/location-facts-page.component";
import {LocationFactsComponent} from "./facts/location-facts.component";
import {LocationService} from "./location.service";
import {LocationMapPageComponent} from "./map/location-map-page.component";
import {LocationMapComponent} from "./map/location-map.component";
import {LocationNodeRoutesComponent} from "./nodes/location-node-routes.component";
import {LocationNodeTableComponent} from "./nodes/location-node-table.component";
import {LocationNodesPageComponent} from "./nodes/location-nodes-page.component";
import {LocationNodesComponent} from "./nodes/location-nodes.component";
import {LocationRouteTableComponent} from "./routes/location-route-table.component";
import {LocationRoutesPageComponent} from "./routes/location-routes-page.component";
import {LocationRoutesComponent} from "./routes/location-routes.component";

@NgModule({
  imports: [
    LocationRoutingModule,
    CommonModule,
    SharedModule,
    MatDividerModule,
    MatTableModule,
    MatSortModule
  ],
  declarations: [
    LocationPageHeaderComponent,
    LocationPageBreadcrumbComponent,
    LocationNodesPageComponent,
    LocationFactsPageComponent,
    LocationRoutesPageComponent,
    LocationMapPageComponent,
    LocationChangesPageComponent,
    LocationNodeTableComponent,
    LocationNodeRoutesComponent,
    LocationRouteTableComponent,
    LocationRoutesComponent,
    LocationNodesComponent,
    LocationResponseComponent,
    LocationChangesComponent,
    LocationFactsComponent,
    LocationMapComponent
  ],
  providers: [
    LocationService
  ]
})
export class LocationModule {
}
