import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-about',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>About</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-monitor-page-menu pageName="about"></kpn-monitor-page-menu>

    <div class="kpn-comment">
      <p>
        The route monitoring function allows you to compare a route geometry in
        OpenStreetMap against a given reference geometry.
      </p>
    </div>
  `
})
export class MonitorAboutPageComponent {
}
