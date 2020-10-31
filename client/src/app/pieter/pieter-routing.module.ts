import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Util} from "../components/shared/util";
import {PieterComponent} from "./pieter.component";
import {SidebarComponent} from "../components/shared/sidebar/sidebar.component";

const routes: Routes = [
  Util.routePath("", PieterComponent, SidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class PieterRoutingModule {
}
