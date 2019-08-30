import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";
import {ChangeSetPageComponent} from "./_change-set-page.component";
import {ChangeSetRoutingModule} from "./_change-set-routing.module";
import {ChangeSetAnalysisComponent} from "./change-set-analysis.component";
import {ChangeSetHeaderComponent} from "./change-set-header.component";
import {ChangeSetNetworkDiffDetailsComponent} from "./change-set-network-diff-details.component";
import {ChangeSetOrphanNodeChangesComponent} from "./change-set-orphan-node-changes.component";
import {ChangeSetOrphanRouteChangesComponent} from "./change-set-orphan-route-changes.component";
import {NodeDiffsComponent} from "./node-diffs/_node-diffs.component";
import {NodeDiffsAddedComponent} from "./node-diffs/node-diffs-added.component";
import {NodeDiffsRemovedComponent} from "./node-diffs/node-diffs-removed.component";
import {NodeDiffsUpdatedComponent} from "./node-diffs/node-diffs-updated.component";
import {RouteDiffsAddedComponent} from "./route-diffs/route-diffs-added.component";
import {RouteDiffsRemovedComponent} from "./route-diffs/route-diffs-removed.component";
import {RouteDiffsUpdatedComponent} from "./route-diffs/route-diffs-updated.component";
import {RouteDiffsComponent} from "./route-diffs/_route-diffs.component";
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
    ChangeSetNetworkDiffDetailsComponent,
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
    VersionChangeComponent
  ]
})
export class ChangeSetModule {
}
