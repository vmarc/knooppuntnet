import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {NetworkChangesPageComponent} from "./changes/_network-changes-page.component";
import {NetworkDetailsPageComponent} from "./details/_network-details-page.component";
import {NetworkFactsPageComponent} from "./facts/_network-facts-page.component";
import {NetworkMapPageComponent} from "./map/_network-map-page.component";
import {NetworkNodesPageComponent} from "./nodes/_network-nodes-page.component";
import {NetworkRoutesPageComponent} from "./routes/_network-routes-page.component";
import {NetworkRoutesSidebarComponent} from "./routes/network-routes-sidebar.component";
import {NetworkNodesSidebarComponent} from "./nodes/network-nodes-sidebar.component";
import {AnalysisSidebarComponent} from "../../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: ":networkId",
    children: [
      {
        path: "",
        component: NetworkDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":networkId/facts",
    children: [
      {
        path: "",
        component: NetworkFactsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":networkId/nodes",
    children: [
      {
        path: "",
        component: NetworkNodesPageComponent
      },
      {
        path: "",
        component: NetworkNodesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":networkId/routes",
    children: [
      {
        path: "",
        component: NetworkRoutesPageComponent
      },
      {
        path: "",
        component: NetworkRoutesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":networkId/map",
    children: [
      {
        path: "",
        component: NetworkMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":networkId/changes",
    children: [
      {
        path: "",
        component: NetworkChangesPageComponent
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
export class NetworkRoutingModule {
}
