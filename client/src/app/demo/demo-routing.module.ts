import {Routes} from "@angular/router";
import {RouterModule} from "@angular/router";
import {NgModule} from "@angular/core";
import {DemoMenuComponent} from "./menu/demo-menu.component";
import {DemoZoomInComponent} from "./demo_2_zoom_in/demo-zoom-in.component";
import {DemoPlanRouteComponent} from "./demo_1_plan_route/demo-plan-route.component";
import {Util} from "../components/shared/util";
import {DemoSidebarComponent} from "./components/demo-sidebar.component";
import {ViaLoopRouteComponent} from "./viaLoop/via-loop.component";

const routes: Routes = [
  Util.routePath("plan-a-route", DemoPlanRouteComponent, DemoSidebarComponent),
  Util.routePath("zoom-in", DemoZoomInComponent, DemoSidebarComponent),
  Util.routePath("via-loop", ViaLoopRouteComponent, DemoSidebarComponent),
  {
    path: "",
    component: DemoMenuComponent
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
export class DemoRoutingModule {
}
