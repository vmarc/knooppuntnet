import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/analysis-components.module";
import {FactModule} from "../fact/fact.module";
import {NodeRoutingModule} from "./node-routing.module";
import {NodeChangesPageComponent} from "./changes/_node-changes-page.component";
import {NodeChangeComponent} from "./changes/node-change.component";
import {NodeChangesSidebarComponent} from "./changes/node-changes-sidebar.component";
import {NodeChangesService} from "./changes/node-changes.service";
import {NodePageHeaderComponent} from "./components/node-page-header.component";
import {NodeDetailsPageComponent} from "./details/_node-details-page.component";
import {NodeNetworkReferenceStatementComponent} from "./details/node-network-reference-statement.component";
import {NodeNetworkReferenceComponent} from "./details/node-network-reference.component";
import {NodeNetworkReferencesComponent} from "./details/node-network-references.component";
import {NodeOrphanRouteReferencesComponent} from "./details/node-orphan-route-references.component";
import {NodeSummaryComponent} from "./details/node-summary.component";
import {NodeMapPageComponent} from "./map/_node-map-page.component";

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    OlModule,
    SharedModule,
    AnalysisComponentsModule,
    FactModule,
    NodeRoutingModule,
    MatPaginatorModule
  ],
  declarations: [
    NodeDetailsPageComponent,
    NodeChangeComponent,
    NodeSummaryComponent,
    NodeNetworkReferencesComponent,
    NodeNetworkReferenceComponent,
    NodeNetworkReferenceStatementComponent,
    NodeOrphanRouteReferencesComponent,
    NodeChangesPageComponent,
    NodeMapPageComponent,
    NodePageHeaderComponent,
    NodeChangesSidebarComponent,
  ],
  providers: [
    NodeChangesService
  ]
})
export class NodeModule {
}
