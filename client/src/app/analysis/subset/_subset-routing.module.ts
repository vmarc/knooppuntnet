import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";
import {SubsetChangesPageComponent} from "./changes/_subset-changes-page.component";
import {SubsetChangesSidebarComponent} from "./changes/subset-changes-sidebar.component";
import {SubsetFactDetailsPageComponent} from "./fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./networks/_subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanNodesSidebarComponent} from "./orphan-nodes/subset-orphan-nodes-sidebar.component";
import {SubsetOrphanRoutesPageComponent} from "./orphan-routes/_subset-orphan-routes-page.component";
import {SubsetOrphanRoutesSidebarComponent} from "./orphan-routes/subset-orphan-routes-sidebar.component";

const routes: Routes = [
  {
    path: ":country/:networkType/networks",
    children: [
      {
        path: "",
        component: SubsetNetworksPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/facts",
    children: [
      {
        path: "",
        component: SubsetFactsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/orphan-nodes",
    children: [
      {
        path: "",
        component: SubsetOrphanNodesPageComponent
      },
      {
        path: "",
        component: SubsetOrphanNodesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/orphan-routes",
    children: [
      {
        path: "",
        component: SubsetOrphanRoutesPageComponent
      },
      {
        path: "",
        component: SubsetOrphanRoutesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/changes",
    children: [
      {
        path: "",
        component: SubsetChangesPageComponent
      },
      {
        path: "",
        component: SubsetChangesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/:fact",
    children: [
      {
        path: "",
        component: SubsetFactDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class SubsetRoutingModule {
}
