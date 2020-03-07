import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";
import {Util} from "../../components/shared/util";
import {SubsetChangesPageComponent} from "./changes/_subset-changes-page.component";
import {SubsetChangesSidebarComponent} from "./changes/subset-changes-sidebar.component";
import {SubsetFactDetailsPageComponent} from "./fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./facts/_subset-facts-page.component";
import {SubsetMapPageComponent} from "./map/_subset-map-page.component";
import {SubsetNetworksPageComponent} from "./networks/_subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanNodesSidebarComponent} from "./orphan-nodes/subset-orphan-nodes-sidebar.component";
import {SubsetOrphanRoutesPageComponent} from "./orphan-routes/_subset-orphan-routes-page.component";
import {SubsetOrphanRoutesSidebarComponent} from "./orphan-routes/subset-orphan-routes-sidebar.component";

const routes: Routes = [
  Util.routePath(":networkType/:country/networks", SubsetNetworksPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/facts", SubsetFactsPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/orphan-nodes", SubsetOrphanNodesPageComponent, SubsetOrphanNodesSidebarComponent),
  Util.routePath(":networkType/:country/orphan-routes", SubsetOrphanRoutesPageComponent, SubsetOrphanRoutesSidebarComponent),
  Util.routePath(":networkType/:country/map", SubsetMapPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/changes", SubsetChangesPageComponent, SubsetChangesSidebarComponent),
  Util.routePath(":networkType/:country/:fact", SubsetFactDetailsPageComponent, AnalysisSidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class SubsetRoutingModule {
}
