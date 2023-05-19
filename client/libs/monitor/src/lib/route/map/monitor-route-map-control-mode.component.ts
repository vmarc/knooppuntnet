import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-control-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="service.osmSegments().length > 1">
      <mat-radio-group
        [value]="service.mode()"
        (change)="service.modeChanged($event.value)"
      >
        <mat-radio-button value="comparison">
          <span i18n="@@monitor.route.map.mode.comparison">
            GPX / OSM comparison
          </span>
        </mat-radio-button>
        <mat-radio-button value="osm-segments">
          <span i18n="@@monitor.route.map.mode.osm-segments">OSM segments</span>
          <span class="kpn-brackets">{{ service.osmSegments().length }}</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>
  `,
  standalone: true,
  imports: [NgIf, MatRadioModule],
})
export class MonitorRouteMapControlModeComponent {
  constructor(protected service: MonitorRouteMapStateService) {}
}
