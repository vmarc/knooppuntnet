import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../../components/ol/ol.module";
import {SharedModule} from "../../../components/shared/shared.module";
import {NodeChangesPageComponent} from "./changes/_node-changes-page.component";
import {NodeChangeDetailComponent} from "./changes/node-change-detail.component";
import {NodeChangeComponent} from "./changes/node-change.component";
import {NodePageHeaderComponent} from "./components/node-page-header.component";
import {NodeDetailsPageComponent} from "./details/_node-details-page.component";
import {NodeNetworkReferencesComponent} from "./details/node-network-references.component";
import {NodeOrphanRouteReferencesComponent} from "./details/node-orphan-route-references.component";
import {NodeSummaryComponent} from "./details/node-summary.component";
import {NodeMapPageComponent} from "./map/_node-map-page.component";
import {NodeNetworkReferenceComponent} from "./details/node-network-reference.component";
import {NodeNetworkReferenceStatementComponent} from "./details/node-network-reference-statement.component";
import {NodeChangeMovedComponent} from "./changes/node-change-moved.component";
import {NodeRoutingModule} from "./node-routing.module";
import {AnalysisComponentsModule} from "../../components/analysis-components.module";
import {FactModule} from "../../fact/fact.module";

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    OlModule,
    SharedModule,
    AnalysisComponentsModule,
    FactModule,
    NodeRoutingModule
  ],
  declarations: [
    NodeDetailsPageComponent,
    NodeChangeComponent,
    NodeChangeDetailComponent,
    NodeChangeMovedComponent,
    NodeSummaryComponent,
    NodeNetworkReferencesComponent,
    NodeNetworkReferenceComponent,
    NodeNetworkReferenceStatementComponent,
    NodeOrphanRouteReferencesComponent,
    NodeChangesPageComponent,
    NodeMapPageComponent,
    NodePageHeaderComponent,
  ]
})
export class NodeModule {
}
