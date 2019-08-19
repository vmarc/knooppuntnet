import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Util} from "../../components/shared/util";
import {ChangesPageComponent} from "./changes-page.component";
import {AnalysisSidebarComponent} from "../../components/shared/sidebar/analysis-sidebar.component";

const routes: Routes = [
  Util.routePath("", ChangesPageComponent, AnalysisSidebarComponent)
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
