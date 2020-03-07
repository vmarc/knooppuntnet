import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material/icon";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/analysis-components.module";
import {FactModule} from "../fact/fact.module";
import {RouteRoutingModule} from "./route-routing.module";
import {RouteChangesPageComponent} from "./changes/_route-changes-page.component";
import {RouteChangeComponent} from "./changes/route-change.component";
import {RouteChangesSidebarComponent} from "./changes/route-changes-sidebar.component";
import {RouteChangesService} from "./changes/route-changes.service";
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
    RouteChangeComponent,
    RouteChangesPageComponent,
    RouteEndNodesComponent,
    RouteMapPageComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RouteNodeComponent,
    RoutePageComponent,
    RoutePageHeaderComponent,
    RouteRedundantNodesComponent,
    RouteStartNodesComponent,
    RouteStructureComponent,
    RouteSummaryComponent,
    RouteChangesSidebarComponent
  ],
  providers: [
    RouteChangesService
  ]
})
export class RouteModule {
}
