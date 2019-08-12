import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {ChangeSetPageComponent} from "./_change-set-page.component";
import {ChangeSetAnalysisComponent} from "./change-set-analysis.component";
import {ChangeSetHeaderComponent} from "./change-set-header.component";
import {ChangeSetNetworkDiffDetailsComponent} from "./change-set-network-diff-details.component";
import {ChangeSetOrphanNodeChangesComponent} from "./change-set-orphan-node-changes.component";
import {ChangeSetOrphanRouteChangesComponent} from "./change-set-orphan-route-changes.component";
import {NodeDiffsComponent} from "./node-diffs/_node-diffs.component";
import {NodeDiffsAddedComponent} from "./node-diffs/node-diffs-added.component";
import {NodeDiffsRemovedComponent} from "./node-diffs/node-diffs-removed.component";
import {NodeDiffsUpdatedComponent} from "./node-diffs/node-diffs-updated.component";
import {SharedModule} from "../../components/shared/shared.module";
import {ChangeSetRoutingModule} from "./change-set-routing.module";

@NgModule({
  imports: [
    CommonModule,
    MatIconModule,
    SharedModule,
    ChangeSetRoutingModule
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
    NodeDiffsUpdatedComponent
  ]
})
export class ChangeSetModule {
}
