import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Util} from "../components/shared/util";
import {StatusPageComponent} from "./status/status-page.component";
import {StatusSidebarComponent} from "./status/status-sidebar.component";

const routes: Routes = [
  Util.routePath("", StatusPageComponent, StatusSidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class StatusRoutingModule {
}
