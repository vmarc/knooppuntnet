import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-control-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="service.osmSegments().length > 1">
      <mat-radio-group [value]="mode" (change)="modeChanged($event)">
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
  @Input() mode: MonitorMapMode;

  constructor(protected service: MonitorRouteMapService) {}

  modeChanged(event: MatRadioChange): void {
    this.service.mapModeChanged(event.value);
  }
}
