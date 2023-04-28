import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorRouteMapControlComponent } from './monitor-route-map-control.component';

@Component({
  selector: 'kpn-monitor-route-map-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-monitor-route-map-control />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, MonitorRouteMapControlComponent],
})
export class MonitorRouteMapSidebarComponent {}
