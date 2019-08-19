import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule, MatPaginatorModule, MatSortModule, MatTableModule} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {AnalysisComponentsModule} from "../components/_analysis-components.module";
import {FactModule} from "../fact/_fact.module";
import {SubsetRoutingModule} from "./_subset-routing.module";
import {SubsetChangesPageComponent} from "./changes/_subset-changes-page.component";
import {SubsetChangesSidebarComponent} from "./changes/subset-changes-sidebar.component";
import {SubsetChangesService} from "./changes/subset-changes.service";
import {SubsetPageHeaderBlockComponent} from "./components/subset-page-header-block.component";
import {SubsetFactDetailsPageComponent} from "./fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./facts/_subset-facts-page.component";
import {SubsetMapPageComponent} from "./map/_subset-map-page.component";
import {SubsetMapNetworkDialogComponent} from "./map/subset-map-network-dialog.component";
import {SubsetNetworksPageComponent} from "./networks/_subset-networks-page.component";
import {SubsetNetworkHappyComponent} from "./networks/subset-network-happy.component";
import {SubsetNetworkListComponent} from "./networks/subset-network-list.component";
import {SubsetNetworkTableComponent} from "./networks/subset-network-table.component";
import {SubsetNetworkComponent} from "./networks/subset-network.component";
import {SubsetOrphanNodesPageComponent} from "./orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanNodeComponent} from "./orphan-nodes/subset-orphan-node.component";
import {SubsetOrphanNodesSidebarComponent} from "./orphan-nodes/subset-orphan-nodes-sidebar.component";
import {SubsetOrphanNodesTableComponent} from "./orphan-nodes/subset-orphan-nodes-table.component";
import {SubsetOrphanNodesService} from "./orphan-nodes/subset-orphan-nodes.service";
import {SubsetOrphanRoutesPageComponent} from "./orphan-routes/_subset-orphan-routes-page.component";
import {SubsetOrphanRouteComponent} from "./orphan-routes/subset-orphan-route.component";
import {SubsetOrphanRoutesSidebarComponent} from "./orphan-routes/subset-orphan-routes-sidebar.component";
import {SubsetOrphanRoutesTableComponent} from "./orphan-routes/subset-orphan-routes-table.component";
import {SubsetOrphanRoutesService} from "./orphan-routes/subset-orphan-routes.service";

@NgModule({
  imports: [
    CommonModule,
    MatDividerModule,
    MatIconModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    SharedModule,
    FactModule,
    SubsetRoutingModule,
    AnalysisComponentsModule,
    OlModule,
    MarkdownModule,
    MatDialogModule,
    MatButtonModule
  ],
  declarations: [
    SubsetChangesPageComponent,
    SubsetFactDetailsPageComponent,
    SubsetFactsPageComponent,
    SubsetNetworksPageComponent,
    SubsetOrphanNodesPageComponent,
    SubsetOrphanRoutesPageComponent,
    SubsetNetworkListComponent,
    SubsetNetworkTableComponent,
    SubsetNetworkComponent,
    SubsetNetworkHappyComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetOrphanRouteComponent,
    SubsetOrphanNodeComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanRoutesSidebarComponent,
    SubsetChangesSidebarComponent,
    SubsetMapPageComponent,
    SubsetMapNetworkDialogComponent
  ],
  providers: [
    SubsetOrphanNodesService,
    SubsetOrphanRoutesService,
    SubsetChangesService
  ],
  entryComponents: [
    SubsetMapNetworkDialogComponent
  ]
})
export class SubsetModule {
}
