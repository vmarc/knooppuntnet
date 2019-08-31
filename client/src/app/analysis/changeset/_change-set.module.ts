import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";
import {ChangeSetPageComponent} from "./_change-set-page.component";
import {ChangeSetRoutingModule} from "./_change-set-routing.module";
import {ChangeSetAnalysisComponent} from "./change-set-analysis.component";
import {ChangeSetHeaderComponent} from "./change-set-header.component";
import {ChangeSetNetworkChangesComponent} from "./change-set-network-changes.component";
import {ChangeSetOrphanNodeChangesComponent} from "./change-set-orphan-node-changes.component";
import {ChangeSetOrphanRouteChangesComponent} from "./change-set-orphan-route-changes.component";
import {ChangeSetNetworkChangeNodesAddedComponent} from "./network/change-set-network-change-nodes-added.component";
import {ChangeSetNetworkChangeNodesRemovedComponent} from "./network/change-set-network-change-nodes-removed.component";
import {ChangeSetNetworkChangeNodesUpdatedComponent} from "./network/change-set-network-change-nodes-updated.component";
import {ChangeSetNetworkChangeOrphanNodesNewComponent} from "./network/change-set-network-change-orphan-nodes-new.component";
import {ChangeSetNetworkChangeOrphanNodesOldComponent} from "./network/change-set-network-change-orphan-nodes-old.component";
import {ChangeSetNetworkChangeOrphanRoutesNewComponent} from "./network/change-set-network-change-orphan-routes-new.component";
import {ChangeSetNetworkChangeOrphanRoutesOldComponent} from "./network/change-set-network-change-orphan-routes-old.component";
import {ChangeSetNetworkChangeRelationsAddedComponent} from "./network/change-set-network-change-relations-added.component";
import {ChangeSetNetworkChangeRelationsRemovedComponent} from "./network/change-set-network-change-relations-removed.component";
import {ChangeSetNetworkChangeRelationsUpdatedComponent} from "./network/change-set-network-change-relations-updated.component";
import {ChangeSetNetworkChangeTypeComponent} from "./network/change-set-network-change-type.component";
import {ChangeSetNetworkChangeWaysAddedComponent} from "./network/change-set-network-change-ways-added.component";
import {ChangeSetNetworkChangeWaysRemovedComponent} from "./network/change-set-network-change-ways-removed.component";
import {ChangeSetNetworkChangeWaysUpdatedComponent} from "./network/change-set-network-change-ways-updated.component";
import {ChangeSetNetworkChangeComponent} from "./network/change-set-network-change.component";
import {NodeDiffsComponent} from "./node-diffs/_node-diffs.component";
import {NodeDiffsAddedComponent} from "./node-diffs/node-diffs-added.component";
import {NodeDiffsRemovedComponent} from "./node-diffs/node-diffs-removed.component";
import {NodeDiffsUpdatedComponent} from "./node-diffs/node-diffs-updated.component";
import {RouteDiffsComponent} from "./route-diffs/_route-diffs.component";
import {RouteDiffsAddedComponent} from "./route-diffs/route-diffs-added.component";
import {RouteDiffsRemovedComponent} from "./route-diffs/route-diffs-removed.component";
import {RouteDiffsUpdatedComponent} from "./route-diffs/route-diffs-updated.component";
import {VersionChangeComponent} from "./version-change.component";

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
    ChangeSetNetworkChangeTypeComponent,
    ChangeSetNetworkChangeOrphanRoutesOldComponent,
    ChangeSetNetworkChangeOrphanRoutesNewComponent,
    ChangeSetNetworkChangeOrphanNodesOldComponent,
    ChangeSetNetworkChangeOrphanNodesNewComponent,
    ChangeSetNetworkChangeComponent,
    ChangeSetNetworkChangeNodesAddedComponent,
    ChangeSetNetworkChangeNodesRemovedComponent,
    ChangeSetNetworkChangeNodesUpdatedComponent,
    ChangeSetNetworkChangeWaysAddedComponent,
    ChangeSetNetworkChangeWaysRemovedComponent,
    ChangeSetNetworkChangeWaysUpdatedComponent,
    ChangeSetNetworkChangeRelationsAddedComponent,
    ChangeSetNetworkChangeRelationsUpdatedComponent,
    ChangeSetNetworkChangeRelationsRemovedComponent
  ]
})
export class ChangeSetModule {
}
