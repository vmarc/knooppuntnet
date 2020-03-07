import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";
import {Util} from "../../components/shared/util";
import {LocationChangesPageComponent} from "./changes/location-changes-page.component";
import {LocationFactsPageComponent} from "./facts/location-facts-page.component";
import {LocationMapPageComponent} from "./map/location-map-page.component";
import {LocationNodesPageComponent} from "./nodes/location-nodes-page.component";
import {LocationRoutesPageComponent} from "./routes/location-routes-page.component";

const routes: Routes = [
  Util.routePath(":networkType/:country/:location/nodes", LocationNodesPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/:location/routes", LocationRoutesPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/:location/facts", LocationFactsPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/:location/map", LocationMapPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/:location/changes", LocationChangesPageComponent, AnalysisSidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class LocationRoutingModule {
}
