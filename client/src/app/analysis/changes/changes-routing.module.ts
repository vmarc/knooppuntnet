import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ChangesPageComponent} from "./changes-page.component";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  {
    path: "",
    children: [
      {
        path: "",
        component: ChangesPageComponent
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
export class ChangesRoutingModule {
}
