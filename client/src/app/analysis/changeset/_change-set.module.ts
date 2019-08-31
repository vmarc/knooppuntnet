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
import {CsNcNodesAddedComponent} from "./network/cs-nc-nodes-added.component";
import {CsNcNodesRemovedComponent} from "./network/cs-nc-nodes-removed.component";
import {CsNcNodesUpdatedComponent} from "./network/cs-nc-nodes-updated.component";
import {CsNcOrphanNodesNewComponent} from "./network/cs-nc-orphan-nodes-new.component";
import {CsNcOrphanNodesOldComponent} from "./network/cs-nc-orphan-nodes-old.component";
import {CsNcOrphanRoutesNewComponent} from "./network/cs-nc-orphan-routes-new.component";
import {CsNcOrphanRoutesOldComponent} from "./network/cs-nc-orphan-routes-old.component";
import {CsNcRelationsAddedComponent} from "./network/cs-nc-relations-added.component";
import {CsNcRelationsRemovedComponent} from "./network/cs-nc-relations-removed.component";
import {CsNcRelationsUpdatedComponent} from "./network/cs-nc-relations-updated.component";
import {CsNcTypeComponent} from "./network/cs-nc-type.component";
import {CsNcWaysAddedComponent} from "./network/cs-nc-ways-added.component";
import {CsNcWaysRemovedComponent} from "./network/cs-nc-ways-removed.component";
import {CsNcWaysUpdatedComponent} from "./network/cs-nc-ways-updated.component";
import {CsNcComponent} from "./network/cs-nc.component";
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
