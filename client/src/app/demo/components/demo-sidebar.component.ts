import {Component} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";

@Component({
  selector: "kpn-demo-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h1 class="header title" i18n="demo.title">
      Demo videos
    </h1>

    <div class="video-list">

      <kpn-demo-video-control name="plan-a-route" i18n="demo.plan-a-route">
        How to plan a route?
      </kpn-demo-video-control>

      <kpn-demo-video-control name="zoom-in" i18n="demo.zoom-in">
        How to zoom in to a selected area of the map?
      </kpn-demo-video-control>

      <kpn-demo-video-control name="via-loop" i18n="demo.via-loop">
        How to include a loop in the route?
      </kpn-demo-video-control>

    </div>
  `,
  styles: [`
    .title {
      padding-left: 20px;
    }

    .video-list {
      border-top: 1px solid lightgrey;
    }
  `]
})
export class DemoSidebarComponent {
}
