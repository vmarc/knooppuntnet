import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-monitor-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li>Routes</li>
    </ul>

    <h1>
      Routes
    </h1>

    <kpn-monitor-routes-table></kpn-monitor-routes-table>

  `,
  styles: [`
  `]
})
export class MonitorRoutesComponent {
}
