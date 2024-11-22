import { NgClass } from '@angular/common';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DividerComponent } from '@app/components/shared';
import { MapService } from '../map.service';

@Component({
  selector: 'kpn-map-route-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="route-popup" [ngClass]="{ hidden: hide() }">
      @for (route of routes(); track route.routeId) {
        <div>{{ route.scope }} {{ route.name }} ({{ route.routeId }})</div>
      }
      <kpn-divider />
      <div class="hint">Click for further details</div>
    </div>
  `,
  styles: `
    .route-popup {
      padding: 1em;
      background-color: white;
      min-width: 20em;
      border-radius: 0.5em;
      border: 1px solid lightgray;
    }

    .hint {
      font-size: 0.8em;
    }
  `,
  standalone: true,
  imports: [DividerComponent, NgClass],
})
export class MapRoutePopupComponent {
  private readonly mapService = inject(MapService);

  readonly routes = computed(() => {
    const state = this.mapService.routePopupState();
    if (state) {
      return state.routes;
    }
    return [];
  });

  readonly hide = computed(() => this.routes().length === 0);
}
