import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { AnalysisSidebarComponent } from '../../components/shared/sidebar/analysis-sidebar.component';
import { Util } from '../../components/shared/util';
import { LocationChangesPageComponent } from './changes/location-changes-page.component';
import { LocationEditPageComponent } from './edit/location-edit-page.component';
import { LocationFactsPageComponent } from './facts/location-facts-page.component';
import { LocationMapPageComponent } from './map/location-map-page.component';
import { LocationNodesPageComponent } from './nodes/location-nodes-page.component';
import { LocationNodesSidebarComponent } from './nodes/location-nodes-sidebar.component';
import { LocationRoutesPageComponent } from './routes/location-routes-page.component';
import { LocationSelectionPageComponent } from './selection/location-selection-page.component';

const routes: Routes = [
  Util.routePath(
    ':networkType/:country',
    LocationSelectionPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/nodes',
    LocationNodesPageComponent,
    LocationNodesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/routes',
    LocationRoutesPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/facts',
    LocationFactsPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/map',
    LocationMapPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/changes',
    LocationChangesPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/edit',
    LocationEditPageComponent,
    AnalysisSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LocationRoutingModule {}
