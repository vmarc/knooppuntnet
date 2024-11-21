import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { DividerComponent } from '@app/components/shared';
import { MapService } from '../../map/map.service';

@Component({
  selector: 'kpn-menu-test-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(8464197)">LAW11</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(16068584)">Wandelpad Calmeyn</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(16075296)">Het Stroperspad</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(null)">None</button>
    </div>
    <kpn-divider />
    <div class="action">zoom: {{ zoom() }}</div>
    <div class="action">center: {{ center() }}</div>
  `,
  styles: [
    `
      .action {
        margin: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [MatButton, DividerComponent],
})
export class MenuTestActionsComponent {
  private readonly mapService = inject(MapService);
  readonly zoom = this.mapService.zoom;
  readonly center = computed(() => {
    const coordinate = this.mapService.center();
    if (coordinate) {
      return `${coordinate[0]},${coordinate[1]}`;
    }
    return '';
  });

  selectRoute(routeId: number) {
    console.log('selectedRoute: ' + routeId);
    this.mapService.selectRoute(routeId);
  }
}
