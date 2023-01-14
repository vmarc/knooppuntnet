import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-monitor-about',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li i18n="@@breadcrumb.monitor.about">About</li>
    </ul>

    <h1 i18n="@@monitor.about.title">Monitor</h1>

    <kpn-monitor-page-menu pageName="about"/>

    <div class="kpn-comment">
      <p i18n="@@monitor.about.text">
        The route monitoring function allows you to compare a route geometry in
        OpenStreetMap against a given reference geometry.
      </p>
    </div>
  `,
})
export class MonitorAboutPageComponent {}
