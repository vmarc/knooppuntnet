import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { SharedModule } from '../components/shared/shared.module';
import { DemoDisabledComponent } from './components/demo-disabled.component';
import { DemoSidebarComponent } from './components/demo-sidebar.component';
import { DemoVideoControlComponent } from './components/demo-video-control.component';
import { DemoVideoPlayButton } from './components/demo-video-play-button';
import { DemoVideoProgressComponent } from './components/demo-video-progress.component';
import { DemoVideoComponent } from './components/demo-video.component';
import { VideoCoverComponent } from './components/video-cover.component';
import { DemoRoutingModule } from './demo-routing.module';
import { DemoService } from './demo.service';
import { DemoMenuComponent } from './menu/demo-menu.component';
import { DemoEffects } from './store/demo.effects';
import { demoReducer } from './store/demo.reducer';
import { demoFeatureKey } from './store/demo.state';

@NgModule({
  imports: [
    DemoRoutingModule,
    CommonModule,
    StoreModule.forFeature(demoFeatureKey, demoReducer),
    EffectsModule.forFeature([DemoEffects]),
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
    DemoVideoPlayButton,
  ],
  providers: [DemoService],
})
export class DemoModule {}
