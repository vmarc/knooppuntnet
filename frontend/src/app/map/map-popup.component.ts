import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapService } from './map.service';

@Component({
  selector: 'kpn-map-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="popup" class="ol-popup" [class.ol-popup-offset]="offset">
      @for (route of routes(); track route.routeId) {
        <div>{{ route.scope }} {{ route.name }} ({{ route.routeId }})</div>
      }
    </div>
  `,
  styles: `
    .ol-popup {
      position: absolute;
      background-color: white;
      -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
      filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
      padding: 15px;
      border-radius: 10px;
      border: 1px solid #cccccc;
      min-width: 280px;
    }

    .ol-popup-offset {
      bottom: 0;
      left: -50px;
    }

    .ol-popup:after,
    .ol-popup:before {
      top: 100%;
      border: solid transparent;
      content: ' ';
      height: 0;
      width: 0;
      position: absolute;
      pointer-events: none;
    }

    .ol-popup:after {
      border-top-color: white;
      border-width: 10px;
      left: 48px;
      margin-left: -10px;
    }

    .ol-popup:before {
      border-top-color: #cccccc;
      border-width: 11px;
      left: 48px;
      margin-left: -11px;
    }
  `,
  standalone: true,
  imports: [],
})
export class MapPopupComponent implements AfterViewInit {
  private readonly mapService = inject(MapService);
  protected offset = false;

  readonly routes = computed(() => {
    const state = this.mapService.hooverState();
    if (state) {
      return state.routes;
    }
    return null;
  });

  ngAfterViewInit(): void {
    setTimeout(() => (this.offset = true), 500);
  }
}
