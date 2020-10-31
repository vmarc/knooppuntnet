import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../components/shared/shared.module";
import {DemoMenuComponent} from "./menu/demo-menu.component";
import {DemoRoutingModule} from "./demo-routing.module";
import {MatIconModule} from "@angular/material/icon";
import {VideoCoverComponent} from "./components/video-cover.component";
import {DemoSidebarComponent} from "./components/demo-sidebar.component";
import {MatSliderModule} from "@angular/material/slider";
import {DemoVideoControlComponent} from "./components/demo-video-control.component";
import {DemoVideoPlayButton} from "./components/demo-video-play-button";
import {DemoVideoComponent} from "./components/demo-video.component";
import {DemoVideoProgressComponent} from "./components/demo-video-progress.component";
import {DemoDisabledComponent} from "./components/demo-disabled.component";

@NgModule({
  imports: [
    CommonModule,
    DemoRoutingModule,
    SharedModule,
    MatIconModule,
    MatSliderModule,
  ],
  declarations: [
    DemoMenuComponent,
    DemoVideoComponent,
    DemoDisabledComponent,
    DemoVideoProgressComponent,
    VideoCoverComponent,
    DemoSidebarComponent,
    DemoVideoControlComponent,
    DemoVideoPlayButton
  ]
})
export class DemoModule {
}
