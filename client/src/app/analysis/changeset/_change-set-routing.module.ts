import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ChangeSetPageComponent} from "./_change-set-page.component";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: ":changeSetId/:replicationNumber",
    children: [
      {
        path: "",
        component: ChangeSetPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class ChangeSetRoutingModule {
}
