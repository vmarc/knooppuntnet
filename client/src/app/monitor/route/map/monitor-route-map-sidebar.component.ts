import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-monitor-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-monitor-route-map-control/>
    </kpn-sidebar>
  `,
})
export class MonitorRouteMapSidebarComponent {}
