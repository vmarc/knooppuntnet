import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Changes</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-monitor-page-menu pageName="changes"></kpn-monitor-page-menu>

  `
})
export class MonitorChangesPageComponent {
}
