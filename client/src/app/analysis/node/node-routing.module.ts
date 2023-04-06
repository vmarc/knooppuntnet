import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar/analysis-sidebar.component';
import { Util } from '@app/components/shared/util';
import { NodeChangesPageComponent } from './changes/_node-changes-page.component';
import { NodeChangesSidebarComponent } from './changes/node-changes-sidebar.component';
import { NodeDetailsPageComponent } from './details/_node-details-page.component';
import { NodeDetailsSidebarComponent } from './details/node-details-sidebar.component';
import { NodeMapPageComponent } from './map/_node-map-page.component';

const routes: Routes = [
  Util.routePath(':nodeId', NodeDetailsPageComponent, AnalysisSidebarComponent),
  Util.routePath(
    ':nodeId/map',
    NodeMapPageComponent,
    NodeDetailsSidebarComponent
  ),
  Util.routePath(
    ':nodeId/changes',
    NodeChangesPageComponent,
    NodeChangesSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NodeRoutingModule {}
