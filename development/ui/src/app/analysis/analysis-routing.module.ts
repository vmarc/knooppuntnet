import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {ChangeSetPageComponent} from "./pages/changeset/_change-set-page.component";
import {ChangesPageComponent} from "./pages/changes/changes-page.component";
import {NetworkChangesPageComponent} from "./pages/network-changes/network-changes-page.component";
import {NetworkDetailsPageComponent} from "./pages/network-details/network-details-page.component";
import {NetworkFactsPageComponent} from "./pages/network-facts/network-facts-page.component";
import {NetworkMapPageComponent} from "./pages/network-map/network-map-page.component";
import {NetworkNodesPageComponent} from "./pages/network-nodes/network-nodes-page.component";
import {NetworkRoutesPageComponent} from "./pages/network-routes/network-routes-page.component";
import {NodePageComponent} from "./pages/node/node-page.component";
import {OverviewPageComponent} from "./pages/overview/overview-page.component";
import {RoutePageComponent} from "./pages/route/_route-page.component";
import {SubsetChangesPageComponent} from "./pages/subset-changes/subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./pages/subset-fact-details/subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./pages/subset-facts/subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./pages/subset-networks/subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./pages/subset-orphan-nodes/subset-orphan-nodes-page.component";
import {SubsetOrphanRoutesPageComponent} from "./pages/subset-orphan-routes/subset-orphan-routes-page.component";
import {AnalysisSidebarComponent} from "./analysis-sidebar.component";
import {AnalysisPageComponent} from "./pages/analysis/analysis-page.component";

const routes: Routes = [
  {
    path: '',
    component: AnalysisSidebarComponent,
    outlet: "sidebar"
  },
  {
    path: '',
    component: AnalysisPageComponent
  },
  {
    path: 'changeset/:changeSetId/:replicationNumber',
    component: ChangeSetPageComponent
  },
  {
    path: 'changes',
    component: ChangesPageComponent
  },
  {
    path: 'network-changes/:networkId',
    component: NetworkChangesPageComponent
  },
  {
    path: 'network-details/:networkId',
    component: NetworkDetailsPageComponent
  },
  {
    path: 'network-facts/:networkId',
    component: NetworkFactsPageComponent
  },
  {
    path: 'network-map/:networkId',
    component: NetworkMapPageComponent
  },
  {
    path: 'network-nodes/:networkId',
    component: NetworkNodesPageComponent
  },
  {
    path: 'network-routes/:networkId',
    component: NetworkRoutesPageComponent
  },
  {
    path: 'node/:nodeId',
    component: NodePageComponent
  },
  {
    path: 'overview',
    component: OverviewPageComponent
  },
  {
    path: 'route/:routeId',
    component: RoutePageComponent
  },
  {
    path: 'changes/:country/:networkType',
    component: SubsetChangesPageComponent
  },
  {
    path: 'facts/:country/:networkType',
    component: SubsetFactsPageComponent
  },
  {
    path: 'networks/:country/:networkType',
    component: SubsetNetworksPageComponent
  },
  {
    path: 'orphan-nodes/:country/:networkType',
    component: SubsetOrphanNodesPageComponent
  },
  {
    path: 'orphan-routes/:country/:networkType',
    component: SubsetOrphanRoutesPageComponent
  },
  {
    path: ':fact/:country/:networkType',
    component: SubsetFactDetailsPageComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AnalysisRoutingModule {
}
