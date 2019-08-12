import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisBePageComponent} from "./analysis-be-page.component";
import {AnalysisDePageComponent} from "./analysis-de-page.component";
import {AnalysisNlPageComponent} from "./analysis-nl-page.component";
import {AnalysisPageComponent} from "./analysis-page.component";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: "",
    children: [
      {
        path: "",
        component: AnalysisPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "nl",
    children: [
      {
        path: "",
        component: AnalysisNlPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "be",
    children: [
      {
        path: "",
        component: AnalysisBePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "de",
    children: [
      {
        path: "",
        component: AnalysisDePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
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
