import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AnalysisSidebarComponent} from "./analysis-sidebar.component";
import {AnalysisBePageComponent} from "./pages/analysis/analysis-be-page.component";
import {AnalysisDePageComponent} from "./pages/analysis/analysis-de-page.component";
import {AnalysisNlPageComponent} from "./pages/analysis/analysis-nl-page.component";
import {AnalysisPageComponent} from "./pages/analysis/analysis-page.component";
import {ChangesPageComponent} from "./pages/changes/changes-page.component";
import {ChangeSetPageComponent} from "./pages/changeset/_change-set-page.component";
import {FactsPageComponent} from "./pages/facts/_facts-page.component";
import {NetworkChangesPageComponent} from "./pages/network/changes/_network-changes-page.component";
import {NetworkDetailsPageComponent} from "./pages/network/details/_network-details-page.component";
import {NetworkFactsPageComponent} from "./pages/network/facts/_network-facts-page.component";
import {NetworkMapPageComponent} from "./pages/network/map/_network-map-page.component";
import {NetworkNodesPageComponent} from "./pages/network/nodes/_network-nodes-page.component";
import {NetworkRoutesPageComponent} from "./pages/network/routes/_network-routes-page.component";
import {NodeChangesPageComponent} from "./pages/node/changes/_node-changes-page.components";
import {NodePageComponent} from "./pages/node/details/_node-page.component";
import {OverviewPageComponent} from "./pages/overview/_overview-page.component";
import {RouteChangesPageComponent} from "./pages/route/changes/_route-changes-page.component";
import {RoutePageComponent} from "./pages/route/details/_route-page.component";
import {SubsetChangesPageComponent} from "./pages/subset/changes/_subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./pages/subset/fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./pages/subset/facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./pages/subset/networks/_subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./pages/subset/orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanRoutesPageComponent} from "./pages/subset/orphan-routes/_subset-orphan-routes-page.component";

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
    path: 'nl',
    component: AnalysisNlPageComponent
  },
  {
    path: 'be',
    component: AnalysisBePageComponent
  },
  {
    path: 'de',
    component: AnalysisDePageComponent
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
    path: 'node-changes/:nodeId',
    component: NodeChangesPageComponent
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
    path: 'route-changes/:routeId',
    component: RouteChangesPageComponent
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
  },
  {
    path: 'facts',
    component: FactsPageComponent
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
