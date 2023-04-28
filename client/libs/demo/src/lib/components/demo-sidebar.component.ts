import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { DemoVideoControlComponent } from './demo-video-control.component';

@Component({
  selector: 'kpn-demo-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1 class="header title" i18n="@@demo.title">Demo videos</h1>

    <div class="video-list">
      <kpn-demo-video-control
        name="plan-a-route"
        duration="55"
        i18n="@@demo.plan-a-route"
      >
        How to plan a walk (or bicycle ride)?
      </kpn-demo-video-control>

      <kpn-demo-video-control
        name="zoom-in"
        duration="12"
        i18n="@@demo.zoom-in"
      >
        How to zoom in to a selected area of the map?
      </kpn-demo-video-control>

      <kpn-demo-video-control
        name="current-location"
        duration="8"
        i18n="@@demo.current-location"
      >
        How to zoom in to your current location?
      </kpn-demo-video-control>

      <kpn-demo-video-control
        name="refine-plan"
        duration="86"
        i18n="@@demo.refine-plan"
      >
        How to refine your plan?
      </kpn-demo-video-control>

      <kpn-demo-video-control
        name="via-loop"
        duration="18"
        i18n="@@demo.via-loop"
      >
        How to include a loop in the route? (Dutch)
      </kpn-demo-video-control>
    </div>
  `,
  styles: [
    `
      .title {
        padding-left: 20px;
      }

      .video-list {
        border-top: 1px solid lightgrey;
      }
    `,
  ],
  standalone: true,
  imports: [DemoVideoControlComponent],
})
export class DemoSidebarComponent {}
