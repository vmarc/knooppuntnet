import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {RoutePageComponent} from './pages/route/_page/route-page.component';
import {NodePageComponent} from './pages/node/_page/node-page.component';
import {ChangesPageComponent} from './pages/changes/_page/changes-page.component';
import {SubsetOrphanNodesPageComponent} from './pages/subset-orphan-nodes/_page/subset-orphan-nodes-page.component';
import {SubsetOrphanRoutesPageComponent} from './pages/subset-orphan-routes/_page/subset-orphan-routes-page.component';
import {SubsetChangesPageComponent} from './pages/subset-changes/_page/subset-changes-page.component';
import {SubsetNetworksPageComponent} from './pages/subset-networks/_page/subset-networks-page.component';
import {SubsetFactsPageComponent} from './pages/subset-facts/_page/subset-facts-page.component';
import {SubsetFactDetailsPageComponent} from './pages/subset-fact-details/_page/subset-fact-details-page.component';
import {ChangeSetPageComponent} from './pages/changeset/_page/change-set-page.component';
import {NetworkNodesPageComponent} from './pages/network-nodes/_page/network-nodes-page.component';
import {NetworkRoutesPageComponent} from './pages/network-routes/_page/network-routes-page.component';
import {NetworkDetailsPageComponent} from './pages/network-details/_page/network-details-page.component';
import {NetworkChangesPageComponent} from './pages/network-changes/_page/network-changes-page.component';
import {NetworkMapPageComponent} from './pages/network-map/_page/network-map-page.component';
import {NetworkFactsPageComponent} from './pages/network-facts/_page/network-facts-page.component';
import {OverviewPageComponent} from './pages/overview/_page/overview-page.component';
import {KpnMaterialModule} from "../material/kpn-material.module";
import {MapPageComponent} from "./pages/map/_page/map-page.component";
import {AnalysisRoutingModule} from "./analysis-routing.module";
import {SharedModule} from "../shared/shared.module";
import {MapModule} from "../map/map.module";
import {NodeSummaryComponent} from "./pages/node/node-summary/node-summary.component";
import {NodeNetworksComponent} from "./pages/node/node-networks/node-networks.component";
import {NodeRoutesComponent} from "./pages/node/node-routes/node-routes.component";
import {NetworkNodeTableComponent} from "./pages/network-nodes/network-node-table/network-node-table.component";
import {NetworkNodeAnalysisComponent} from "./pages/network-nodes/network-node-analysis/network-node-analysis.component";
import {NetworkNodeRoutesComponent} from "./pages/network-nodes/network-node-routes/network-node-routes.component";
import {SubsetNetworkListComponent} from './pages/subset-networks/subset-network-list/subset-network-list.component';
import {SubsetNetworkTableComponent} from './pages/subset-networks/subset-network-table/subset-network-table.component';
import {SubsetNetworkComponent} from './pages/subset-networks/subset-network/subset-network.component';
import {SubsetNetworkHappyComponent} from './pages/subset-networks/subset-network-happy/subset-network-happy.component';
import {SubsetOrphanRoutesTableComponent} from './pages/subset-orphan-routes/subset-orphan-routes-table/subset-orphan-routes-table.component';
import {SubsetOrphanRouteComponent} from './pages/subset-orphan-routes/subset-orphan-route/subset-orphan-route.component';
import {SubsetOrphanNodeComponent} from './pages/subset-orphan-nodes/subset-orphan-node/subset-orphan-node.component';
import {SubsetOrphanNodesTableComponent} from './pages/subset-orphan-nodes/subset-orphan-nodes-table/subset-orphan-nodes-table.component';
import {ChangesTableComponent} from "./components/changes/changes-table/changes-table.component";
import { MapDetailComponent } from './pages/map/map-detail/map-detail.component';
import { MapDetailNodeComponent } from './pages/map/map-detail-node/map-detail-node.component';
import { MapDetailRouteComponent } from './pages/map/map-detail-route/map-detail-route.component';
import { MapDetailDefaultComponent } from './pages/map/map-detail-default/map-detail-default.component';

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule,
    SharedModule,
    MapModule,
    AnalysisRoutingModule
  ],
  declarations: [
    ChangeSetPageComponent,
    ChangesPageComponent,
    MapPageComponent,
    NetworkChangesPageComponent,
    NetworkDetailsPageComponent,
    NetworkFactsPageComponent,
    NetworkMapPageComponent,
    NetworkNodesPageComponent,
    NetworkNodeTableComponent,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    NetworkRoutesPageComponent,
    NodePageComponent,
    NodeSummaryComponent,
    NodeNetworksComponent,
    NodeRoutesComponent,
    OverviewPageComponent,
    RoutePageComponent,
    SubsetChangesPageComponent,
    SubsetFactDetailsPageComponent,
    SubsetFactsPageComponent,
    SubsetNetworksPageComponent,
    SubsetOrphanNodesPageComponent,
    SubsetOrphanRoutesPageComponent,
    SubsetNetworkListComponent,
    SubsetNetworkTableComponent,
    SubsetNetworkComponent,
    SubsetNetworkHappyComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetOrphanRouteComponent,
    SubsetOrphanNodeComponent,
    SubsetOrphanNodesTableComponent,
    ChangesTableComponent,
    MapDetailComponent,
    MapDetailNodeComponent,
    MapDetailRouteComponent,
    MapDetailDefaultComponent
  ]
})
export class AnalysisModule {
}
