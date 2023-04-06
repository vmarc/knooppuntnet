import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Util } from '@app/components/shared/util';
import { SubsetChangesPageComponent } from './changes/_subset-changes-page.component';
import { SubsetChangesSidebarComponent } from './changes/subset-changes-sidebar.component';
import { SubsetFactDetailsPageComponent } from './fact-details/_subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/_subset-facts-page.component';
import { SubsetMapPageComponent } from './map/_subset-map-page.component';
import { SubsetNetworksPageComponent } from './networks/_subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/_subset-orphan-nodes-page.component';
import { SubsetOrphanNodesSidebarComponent } from './orphan-nodes/subset-orphan-nodes-sidebar.component';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/_subset-orphan-routes-page.component';
import { SubsetOrphanRoutesSidebarComponent } from './orphan-routes/subset-orphan-routes-sidebar.component';
import { SubsetSidebarComponent } from './subset-sidebar.component';

const routes: Routes = [
  Util.routePath(
    ':networkType/:country/networks',
    SubsetNetworksPageComponent,
    SubsetSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/facts',
    SubsetFactsPageComponent,
    SubsetSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/orphan-nodes',
    SubsetOrphanNodesPageComponent,
    SubsetOrphanNodesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/orphan-routes',
    SubsetOrphanRoutesPageComponent,
    SubsetOrphanRoutesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/map',
    SubsetMapPageComponent,
    SubsetSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/changes',
    SubsetChangesPageComponent,
    SubsetChangesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/facts/:fact',
    SubsetFactDetailsPageComponent,
    SubsetSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SubsetRoutingModule {}
