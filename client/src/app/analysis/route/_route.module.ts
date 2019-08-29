import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";
import {FactModule} from "../fact/_fact.module";
import {RouteRoutingModule} from "./_route-routing.module";
import {RouteChangesPageComponent} from "./changes/_route-changes-page.component";
import {NodeListComponent} from "./changes/node-list.component";
import {RouteChangeDetailComponent} from "./changes/route-change-detail.component";
import {RouteChangeWayAddedComponent} from "./changes/route-change-way-added.component";
import {RouteChangeWayRemovedComponent} from "./changes/route-change-way-removed.component";
import {RouteChangeWayUpdatedComponent} from "./changes/route-change-way-updated.component";
import {RouteChangeComponent} from "./changes/route-change.component";
import {RouteDiffComponent} from "./changes/route-diff.component";
import {RouteNodeDiffComponent} from "./changes/route-node-diff.component";
import {RoutePageHeaderComponent} from "./components/route-page-header.component";
import {RoutePageComponent} from "./details/_route-page.component";
import {RouteEndNodesComponent} from "./details/route-end-nodes.component";
import {RouteMembersComponent} from "./details/route-members.component";
import {RouteNetworkReferencesComponent} from "./details/route-network-references.component";
import {RouteNodeComponent} from "./details/route-node.component";
import {RouteRedundantNodesComponent} from "./details/route-redundant-nodes.component";
import {RouteStartNodesComponent} from "./details/route-start-nodes.component";
import {RouteStructureComponent} from "./details/route-structure.component";
import {RouteSummaryComponent} from "./details/route-summary.component";
import {RouteMapPageComponent} from "./map/_route-map-page.component";

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    MatIconModule,
    SharedModule,
    FactModule,
    AnalysisComponentsModule,
    RouteRoutingModule,
    OlModule
  ],
  declarations: [
    NodeListComponent,
    RouteChangeComponent,
    RouteChangeDetailComponent,
    RouteChangesPageComponent,
    RouteChangeWayAddedComponent,
    RouteChangeWayRemovedComponent,
    RouteChangeWayUpdatedComponent,
    RouteDiffComponent,
    RouteEndNodesComponent,
    RouteMapPageComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RouteNodeComponent,
    RouteNodeDiffComponent,
    RoutePageComponent,
    RoutePageHeaderComponent,
    RouteRedundantNodesComponent,
    RouteStartNodesComponent,
    RouteStructureComponent,
    RouteSummaryComponent
  ]
})
export class RouteModule {
}
