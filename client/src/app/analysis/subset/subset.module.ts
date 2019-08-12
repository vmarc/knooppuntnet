import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatDividerModule, MatIconModule, MatPaginatorModule, MatSortModule, MatTableModule} from "@angular/material";
import {SharedModule} from "../../components/shared/shared.module";
import {SubsetChangesPageComponent} from "./changes/_subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./networks/_subset-networks-page.component";
import {SubsetNetworkHappyComponent} from "./networks/subset-network-happy.component";
import {SubsetNetworkListComponent} from "./networks/subset-network-list.component";
import {SubsetNetworkTableComponent} from "./networks/subset-network-table.component";
import {SubsetNetworkComponent} from "./networks/subset-network.component";
import {SubsetOrphanNodesPageComponent} from "./orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanNodeComponent} from "./orphan-nodes/subset-orphan-node.component";
import {SubsetOrphanNodesTableComponent} from "./orphan-nodes/subset-orphan-nodes-table.component";
import {SubsetOrphanRoutesPageComponent} from "./orphan-routes/_subset-orphan-routes-page.component";
import {SubsetOrphanRouteComponent} from "./orphan-routes/subset-orphan-route.component";
import {SubsetOrphanRoutesTableComponent} from "./orphan-routes/subset-orphan-routes-table.component";
import {SubsetRoutingModule} from "./subset-routing.module";
import {SubsetPageHeaderBlockComponent} from "./components/subset-page-header-block.component";
import {FactModule} from "../fact/fact.module";

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
    SubsetRoutingModule
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
    SubsetPageHeaderBlockComponent
  ]
})
export class SubsetModule {
}
