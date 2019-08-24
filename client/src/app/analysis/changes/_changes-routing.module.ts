import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Util} from "../../components/shared/util";
import {ChangesSidebarComponent} from "../components/changes/filter/changes-sidebar.component";
import {ChangesPageComponent} from "./_changes-page.component";

const routes: Routes = [
  Util.routePath("", ChangesPageComponent, ChangesSidebarComponent)
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
