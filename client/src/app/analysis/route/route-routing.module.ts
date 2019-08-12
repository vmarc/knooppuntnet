import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {RouteChangesPageComponent} from "./changes/_route-changes-page.component";
import {RoutePageComponent} from "./details/_route-page.component";
import {RouteMapPageComponent} from "./map/_route-map-page.component";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: ":routeId",
    children: [
      {
        path: "",
        component: RoutePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":routeId/map",
    children: [
      {
        path: "",
        component: RouteMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":routeId/changes",
    children: [
      {
        path: "",
        component: RouteChangesPageComponent
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
export class RouteRoutingModule {
}
