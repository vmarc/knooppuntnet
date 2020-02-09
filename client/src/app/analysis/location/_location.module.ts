import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {SharedModule} from "../../components/shared/shared.module";
import {LocationRoutingModule} from "./_location-routing.module";
import {LocationChangesPageComponent} from "./changes/location-changes-page.component";
import {LocationPageBreadcrumbComponent} from "./components/location-page-breadcrumb.component";
import {LocationPageHeaderComponent} from "./components/location-page-header.component";
import {LocationFactsPageComponent} from "./facts/location-facts-page.component";
import {LocationMapPageComponent} from "./map/location-map-page.component";
import {LocationNodesPageComponent} from "./nodes/location-nodes-page.component";
import {LocationRoutesPageComponent} from "./routes/location-routes-page.component";

@NgModule({
  imports: [
    LocationRoutingModule,
    CommonModule,
    SharedModule
  ],
  declarations: [
    LocationPageHeaderComponent,
    LocationPageBreadcrumbComponent,
    LocationNodesPageComponent,
    LocationFactsPageComponent,
    LocationRoutesPageComponent,
    LocationMapPageComponent,
    LocationChangesPageComponent
  ]
})
export class LocationModule {
}
