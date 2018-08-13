import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {ChangeSetPageComponent} from "./pages/changeset/_page/change-set-page.component";
import {ChangesPageComponent} from "./pages/changes/_page/changes-page.component";
import {MapPageComponent} from "./pages/map/_page/map-page.component";
import {NetworkChangesPageComponent} from "./pages/network-changes/_page/network-changes-page.component";
import {NetworkDetailsPageComponent} from "./pages/network-details/_page/network-details-page.component";
import {NetworkFactsPageComponent} from "./pages/network-facts/_page/network-facts-page.component";
import {NetworkMapPageComponent} from "./pages/network-map/_page/network-map-page.component";
import {NetworkNodesPageComponent} from "./pages/network-nodes/_page/network-nodes-page.component";
import {NetworkRoutesPageComponent} from "./pages/network-routes/_page/network-routes-page.component";
import {NodePageComponent} from "./pages/node/_page/node-page.component";
import {OverviewPageComponent} from "./pages/overview/_page/overview-page.component";
import {RoutePageComponent} from "./pages/route/_page/route-page.component";
import {SubsetChangesPageComponent} from "./pages/subset-changes/_page/subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./pages/subset-fact-details/_page/subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./pages/subset-facts/_page/subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./pages/subset-networks/_page/subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./pages/subset-orphan-nodes/_page/subset-orphan-nodes-page.component";
import {SubsetOrphanRoutesPageComponent} from "./pages/subset-orphan-routes/_page/subset-orphan-routes-page.component";

const routes: Routes = [
  {
    path: 'changeset/:changesetId/:replicationId',
    component: ChangeSetPageComponent
  },
  {
    path: 'changes',
    component: ChangesPageComponent
  },
  {
    path: 'map/:networkType',
    component: MapPageComponent
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
