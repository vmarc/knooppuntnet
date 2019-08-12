import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {FactsPageComponent} from "./_facts-page.component";
import {AnalysisSidebarComponent} from "../../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: "facts",
    children: [
      {
        path: "",
        component: FactsPageComponent
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
export class FactsRoutingModule {
}
