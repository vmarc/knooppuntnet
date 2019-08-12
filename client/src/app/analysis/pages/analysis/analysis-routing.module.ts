import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisBePageComponent} from "./analysis-be-page.component";
import {AnalysisDePageComponent} from "./analysis-de-page.component";
import {AnalysisNlPageComponent} from "./analysis-nl-page.component";
import {AnalysisPageComponent} from "./analysis-page.component";
import {AnalysisSidebarComponent} from "../../../components/shared/sidebar/analysis-sidebar.component";

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
    loadChildren: "../node/node.module#NodeModule"
  },
  {
    path: "route",
    loadChildren: "../route/route.module#RouteModule"
  },
  {
    path: "network",
    loadChildren: "../network/network.module#NetworkModule"
  },
  {
    path: "changeset",
    loadChildren: "../changeset/change-set.module#ChangeSetModule"
  },
  {
    path: "changes",
    loadChildren: "../changes/changes.module#ChangesModule"
  },
  {
    path: "facts",
    loadChildren: "../facts/facts.module#FactsModule"
  },
  {
    path: "overview",
    loadChildren: "../overview/overview.module#OverviewModule"
  },
  {
    path: "",
    loadChildren: "../subset/subset.module#SubsetModule"
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
