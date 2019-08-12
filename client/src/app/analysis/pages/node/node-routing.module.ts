import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {NodeChangesPageComponent} from "./changes/_node-changes-page.component";
import {NodeDetailsPageComponent} from "./details/_node-details-page.component";
import {NodeMapPageComponent} from "./map/_node-map-page.component";
import {AnalysisSidebarComponent} from "../../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: ":nodeId",
    children: [
      {
        path: "",
        component: NodeDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":nodeId/map",
    children: [
      {
        path: "",
        component: NodeMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":nodeId/changes",
    children: [
      {
        path: "",
        component: NodeChangesPageComponent
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
export class NodeRoutingModule {
}
