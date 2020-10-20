import {Routes} from "@angular/router";
import {RouterModule} from "@angular/router";
import {NgModule} from "@angular/core";
import {DemoMenuComponent} from "./menu/demo-menu.component";
import {DemoZoomInComponent} from "./demo_2_zoom_in/demo-zoom-in.component";
import {DemoPlanRouteComponent} from "./demo_1_plan_route/demo-plan-route.component";

const routes: Routes = [
  {
    path: "plan-a-route",
    component: DemoPlanRouteComponent
  },
  {
    path: "zoom-in",
    component: DemoZoomInComponent
  },
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
