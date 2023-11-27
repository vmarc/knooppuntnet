import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-control-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (osmSegmentCount() > 1) {
      <div>
        <mat-radio-group [value]="mode()" (change)="modeChanged($event.value)">
          <mat-radio-button value="comparison">
            <span i18n="@@monitor.route.map.mode.comparison"> GPX / OSM comparison </span>
          </mat-radio-button>
          <mat-radio-button [value]="MonitorMapMode.osmSegments">
            <span i18n="@@monitor.route.map.mode.osm-segments">OSM segments</span>
            <span class="kpn-brackets">{{ osmSegmentCount() }}</span>
          </mat-radio-button>
        </mat-radio-group>
      </div>
    }
  `,
  standalone: true,
  imports: [MatRadioModule],
})
export class MonitorRouteMapControlModeComponent {
  private readonly service = inject(MonitorRouteMapStateService);

  protected readonly osmSegmentCount = computed(() => {
    return this.service.page()?.osmSegments.length ?? 0;
  });
  protected readonly mode = this.service.mode;
  protected readonly MonitorMapMode = MonitorMapMode;

  modeChanged(value: MonitorMapMode): void {
    this.service.modeChanged(value);
  }
}
