import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../components/shared/shared.module";
import {DemoMenuComponent} from "./menu/demo-menu.component";
import {DemoRoutingModule} from "./demo-routing.module";
import {VideoComponent} from "./components/video.component";
import {MatIconModule} from "@angular/material/icon";
import {VideoCoverComponent} from "./components/video-cover.component";
import {MatButtonModule} from "@angular/material/button";
import {DemoZoomInComponent} from "./demo_2_zoom_in/demo-zoom-in.component";
import {DemoPlanRouteComponent} from "./demo_1_plan_route/demo-plan-route.component";

@NgModule({
  declarations: [
    DemoPlanRouteComponent,
    DemoZoomInComponent,
    DemoMenuComponent,
    VideoComponent,
    VideoCoverComponent,
  ],
  imports: [
    CommonModule,
    DemoRoutingModule,
    SharedModule,
    MatIconModule,
    MatButtonModule
  ]
})
export class DemoModule {
}
