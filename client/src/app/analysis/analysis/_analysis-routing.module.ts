import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";
import {Util} from "../../components/shared/util";
import {AnalysisCanoePageComponent} from "./analysis-canoe-page.component";
import {AnalysisCyclingPageComponent} from "./analysis-cycling-page.component";
import {AnalysisHikingPageComponent} from "./analysis-hiking-page.component";
import {AnalysisHorseRidingPageComponent} from "./analysis-horse-riding-page.component";
import {AnalysisInlineSkatingPageComponent} from "./analysis-inline-skating-page.component";
import {AnalysisMotorboatPageComponent} from "./analysis-motorboat-page.component";
import {AnalysisPageComponent} from "./analysis-page.component";
import {LocationNodesPageComponent} from "./location-nodes-page.component";
import {LocationSelectionPageComponent} from "./location-selection-page.component";

const routes: Routes = [
  Util.routePath("", AnalysisPageComponent, AnalysisSidebarComponent),
  {
    path: "node",
    loadChildren: () => import("../node/_node.module").then(m => m.NodeModule)
  },
  {
    path: "route",
    loadChildren: () => import("../route/_route.module").then(m => m.RouteModule)
  },
  {
    path: "network",
    loadChildren: () => import("../network/_network.module").then(m => m.NetworkModule)
  },
  {
    path: "changeset",
    loadChildren: () => import("../changeset/_change-set.module").then(m => m.ChangeSetModule)
  },
  {
    path: "changes",
    loadChildren: () => import("../changes/_changes.module").then(m => m.ChangesModule)
  },
  {
    path: "facts",
    loadChildren: () => import("../facts/_facts.module").then(m => m.FactsModule)
  },
  {
    path: "overview",
    loadChildren: () => import("../overview/_overview.module").then(m => m.OverviewModule)
  },
  Util.routePath("cycling", AnalysisCyclingPageComponent, AnalysisSidebarComponent),
  Util.routePath("hiking", AnalysisHikingPageComponent, AnalysisSidebarComponent),
  Util.routePath("horse-riding", AnalysisHorseRidingPageComponent, AnalysisSidebarComponent),
  Util.routePath("motorboat", AnalysisMotorboatPageComponent, AnalysisSidebarComponent),
  Util.routePath("canoe", AnalysisCanoePageComponent, AnalysisSidebarComponent),
  Util.routePath("inline-skating", AnalysisInlineSkatingPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country", LocationSelectionPageComponent, AnalysisSidebarComponent),
  Util.routePath(":networkType/:country/:location/nodes", LocationNodesPageComponent, AnalysisSidebarComponent),
  {
    path: "",
    loadChildren: () => import("../subset/_subset.module").then(m => m.SubsetModule)
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
export class AnalysisRoutingModule {
}
