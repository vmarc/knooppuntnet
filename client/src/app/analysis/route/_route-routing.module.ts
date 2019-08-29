import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";
import {Util} from "../../components/shared/util";
import {RouteChangesPageComponent} from "./changes/_route-changes-page.component";
import {RouteChangesSidebarComponent} from "./changes/route-changes-sidebar.component";
import {RoutePageComponent} from "./details/_route-page.component";
import {RouteMapPageComponent} from "./map/_route-map-page.component";

const routes: Routes = [
  Util.routePath(":routeId", RoutePageComponent, AnalysisSidebarComponent),
  Util.routePath(":routeId/map", RouteMapPageComponent, AnalysisSidebarComponent),
  Util.routePath(":routeId/changes", RouteChangesPageComponent, RouteChangesSidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class RouteRoutingModule {
}
