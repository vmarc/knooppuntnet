import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {SharedModule} from '../../components/shared/shared.module';
import {AnalysisComponentsModule} from '../components/analysis-components.module';
import {ChangeSetPageComponent} from './page/_change-set-page.component';
import {ChangeSetRoutingModule} from './change-set-routing.module';
import {ChangeSetAnalysisComponent} from './page/change-set-analysis.component';
import {ChangeSetHeaderComponent} from './page/change-set-header.component';
import {ChangeSetNetworkChangesComponent} from './page/change-set-network-changes.component';
import {ChangeSetOrphanNodeChangesComponent} from './page/change-set-orphan-node-changes.component';
import {ChangeSetOrphanRouteChangesComponent} from './page/change-set-orphan-route-changes.component';
import {CsNcNodesAddedComponent} from './page/network/cs-nc-nodes-added.component';
import {CsNcNodesRemovedComponent} from './page/network/cs-nc-nodes-removed.component';
import {CsNcNodesUpdatedComponent} from './page/network/cs-nc-nodes-updated.component';
import {CsNcOrphanNodesNewComponent} from './page/network/cs-nc-orphan-nodes-new.component';
import {CsNcOrphanNodesOldComponent} from './page/network/cs-nc-orphan-nodes-old.component';
import {CsNcOrphanRoutesNewComponent} from './page/network/cs-nc-orphan-routes-new.component';
import {CsNcOrphanRoutesOldComponent} from './page/network/cs-nc-orphan-routes-old.component';
import {CsNcRelationsAddedComponent} from './page/network/cs-nc-relations-added.component';
import {CsNcRelationsRemovedComponent} from './page/network/cs-nc-relations-removed.component';
import {CsNcRelationsUpdatedComponent} from './page/network/cs-nc-relations-updated.component';
import {CsNcTypeComponent} from './page/network/cs-nc-type.component';
import {CsNcWaysAddedComponent} from './page/network/cs-nc-ways-added.component';
import {CsNcWaysRemovedComponent} from './page/network/cs-nc-ways-removed.component';
import {CsNcWaysUpdatedComponent} from './page/network/cs-nc-ways-updated.component';
import {CsNcComponent} from './page/network/cs-nc.component';
import {NodeDiffsComponent} from './page/node-diffs/_node-diffs.component';
import {NodeDiffsAddedComponent} from './page/node-diffs/node-diffs-added.component';
import {NodeDiffsRemovedComponent} from './page/node-diffs/node-diffs-removed.component';
import {NodeDiffsUpdatedComponent} from './page/node-diffs/node-diffs-updated.component';
import {RouteDiffsComponent} from './page/route-diffs/_route-diffs.component';
import {RouteDiffsAddedComponent} from './page/route-diffs/route-diffs-added.component';
import {RouteDiffsRemovedComponent} from './page/route-diffs/route-diffs-removed.component';
import {RouteDiffsUpdatedComponent} from './page/route-diffs/route-diffs-updated.component';
import {VersionChangeComponent} from './page/version-change.component';

@NgModule({
  imports: [
    CommonModule,
    MatIconModule,
    SharedModule,
    ChangeSetRoutingModule,
    AnalysisComponentsModule
  ],
  declarations: [
    ChangeSetPageComponent,
    ChangeSetHeaderComponent,
    ChangeSetAnalysisComponent,
    ChangeSetNetworkChangesComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    NodeDiffsComponent,
    NodeDiffsRemovedComponent,
    NodeDiffsAddedComponent,
    NodeDiffsUpdatedComponent,
    RouteDiffsComponent,
    RouteDiffsAddedComponent,
    RouteDiffsRemovedComponent,
    RouteDiffsUpdatedComponent,
    VersionChangeComponent,
    CsNcTypeComponent,
    CsNcOrphanRoutesOldComponent,
    CsNcOrphanRoutesNewComponent,
    CsNcOrphanNodesOldComponent,
    CsNcOrphanNodesNewComponent,
    CsNcComponent,
    CsNcNodesAddedComponent,
    CsNcNodesRemovedComponent,
    CsNcNodesUpdatedComponent,
    CsNcWaysAddedComponent,
    CsNcWaysRemovedComponent,
    CsNcWaysUpdatedComponent,
    CsNcRelationsAddedComponent,
    CsNcRelationsUpdatedComponent,
    CsNcRelationsRemovedComponent
  ]
})
export class ChangeSetModule {
}
