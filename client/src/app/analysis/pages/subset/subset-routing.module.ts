import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {SubsetChangesPageComponent} from "./changes/_subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./networks/_subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanRoutesPageComponent} from "./orphan-routes/_subset-orphan-routes-page.component";
import {AnalysisSidebarComponent} from "../../../components/shared/sidebar/analysis-sidebar.component";

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
        component: AnalysisSidebarComponent,
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
        component: AnalysisSidebarComponent,
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
        component: AnalysisSidebarComponent,
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
