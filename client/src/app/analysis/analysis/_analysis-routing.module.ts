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
    loadChildren: "../node/_node.module#NodeModule"
  },
  {
    path: "route",
    loadChildren: "../route/_route.module#RouteModule"
  },
  {
    path: "network",
    loadChildren: "../network/_network.module#NetworkModule"
  },
  {
    path: "changeset",
    loadChildren: "../changeset/_change-set.module#ChangeSetModule"
  },
  {
    path: "changes",
    loadChildren: "../changes/_changes.module#ChangesModule"
  },
  {
    path: "facts",
    loadChildren: "../facts/_facts.module#FactsModule"
  },
  {
    path: "overview",
    loadChildren: "../overview/_overview.module#OverviewModule"
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
    loadChildren: "../subset/_subset.module#SubsetModule"
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
