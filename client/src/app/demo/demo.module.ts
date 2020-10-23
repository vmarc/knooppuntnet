import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../components/shared/shared.module";
import {DemoMenuComponent} from "./menu/demo-menu.component";
import {DemoRoutingModule} from "./demo-routing.module";
import {VideoComponent} from "./components/video.component";
import {MatIconModule} from "@angular/material/icon";
import {VideoCoverComponent} from "./components/video-cover.component";
import {DemoZoomInComponent} from "./demo_2_zoom_in/demo-zoom-in.component";
import {DemoPlanRouteComponent} from "./demo_1_plan_route/demo-plan-route.component";
import {DemoSidebarComponent} from "./components/demo-sidebar.component";
import {ViaLoopRouteComponent} from "./viaLoop/via-loop.component";
import {MatSliderModule} from "@angular/material/slider";
import {DemoVideoControlComponent} from "./components/demo-video-control.component";

@NgModule({
  imports: [
    CommonModule,
    DemoRoutingModule,
    SharedModule,
    MatIconModule,
    MatSliderModule,
  ],
  declarations: [
    DemoPlanRouteComponent,
    DemoZoomInComponent,
    DemoMenuComponent,
    VideoComponent,
    VideoCoverComponent,
    DemoSidebarComponent,
    ViaLoopRouteComponent,
    DemoVideoControlComponent
  ]
})
export class DemoModule {
}
