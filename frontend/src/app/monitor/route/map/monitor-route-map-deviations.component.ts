import { ViewChild } from '@angular/core';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectionList } from '@angular/material/list';
import { MatListModule, MatSelectionListChange } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { EditService } from '@app/components/shared';
import { DistancePipe } from '@app/components/shared/format';
import { IconHappyComponent } from '@app/components/shared/icon';
import { LegendLineComponent } from './legend-line';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-deviations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!referenceAvailable()) {
      <p i18n="@@monitor.route.map-deviations.no-reference">
        No reference, so no analysis results.
      </p>
    } @else {
      @if (!osmRelationAvailable()) {
        <p i18n="@@monitor.route.map-deviations.no-relation">
          OSM relation missing in route definition, so no analysis results.
        </p>
      } @else {
        @if (osmRelationEmpty()) {
          <p i18n="@@monitor.route.map-deviations.relation-empty">
            OSM relation empty, so no analysis results.
          </p>
        }

        @if (deviations().length === 0) {
          <p class="kpn-spacer-above kpn-line">
            <kpn-icon-happy />
            <span i18n="@@monitor.route.map-deviations.no-deviations"> No deviations </span>
          </p>
        } @else {
          <p class="segments-title">
            <span i18n="@@monitor.route.map-deviations.title">Deviations</span>
          </p>

          <div class="segment segment-header">
            <span class="segment-id">
              <kpn-legend-line color="red"></kpn-legend-line>
            </span>
            <span class="segment-deviation" i18n="@@monitor.route.map-deviations.deviation">
              Deviation
            </span>
            <span i18n="@@monitor.route.map-deviations.length">Length</span>
          </div>

          <mat-menu #appMenu="matMenu">
            <ng-template matMenuContent let-deviation="deviation">
              <button
                mat-menu-item
                (click)="josmZoomToSelectedDeviation(deviation)"
                i18n="@@monitor.route.map-deviations.zoom-josm"
              >
                Go here in JOSM
              </button>
              <button
                mat-menu-item
                (click)="zoomToDeviationInOpenstreetMap(deviation)"
                i18n="@@monitor.route.map-deviations.zoom-openstreetmap"
              >
                Go here in openstreetmap.org
              </button>
            </ng-template>
          </mat-menu>

          <mat-selection-list
            #deviationSelectionList
            [multiple]="false"
            (selectionChange)="selectionChanged($event)"
            [hideSingleSelectionIndicator]="true"
          >
            @for (deviation of deviations(); track $index) {
              <mat-list-option [selected]="isDeviationSelected(deviation)" [value]="deviation">
                <div class="segment">
                  <span class="segment-id">{{ deviation.id }}</span>

                  @if (deviation.distance === 2500) {
                    <span class="segment-deviation">{{ longDistance }}</span>
                  } @else {
                    <span class="segment-deviation">{{ deviation.distance | distance }}</span>
                  }

                  <span>{{ deviation.meters | distance }}</span>
                  <button
                    mat-icon-button
                    [matMenuTriggerFor]="appMenu"
                    [matMenuTriggerData]="{ deviation: deviation }"
                    class="popup-menu"
                  >
                    <mat-icon svgIcon="menu-dots" />
                  </button>
                </div>
              </mat-list-option>
            }
          </mat-selection-list>
        }
      }
    }
  `,
  styles: `
    .segments-title {
      padding-top: 1em;
    }

    .segment {
      display: flex;
      align-items: center;
    }

    .segment-header {
      padding-left: 1em;
    }

    .segment-id {
      width: 3em;
    }

    .segment-deviation {
      width: 5em;
    }

    .popup-menu {
      margin-left: auto;
    }
  `,
  standalone: true,
  imports: [
    DistancePipe,
    IconHappyComponent,
    LegendLineComponent,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
  ],
})
export class MonitorRouteMapDeviationsComponent {
  private readonly service = inject(MonitorRouteMapStateService);
  private readonly editService = inject(EditService);

  readonly referenceAvailable = this.service.referenceAvailable;
  readonly selectedDeviation = this.service.selectedDeviation;

  readonly longDistance = '> 2.5 km';
  readonly osmRelationAvailable = computed(() => {
    return !!this.service.page().relationId;
  });
  readonly osmRelationEmpty = computed(() => {
    const page = this.service.page();
    return page.osmSegments.length === 0 && !!page.relationId;
  });
  readonly deviations = computed(() => {
    return this.service.page()?.deviations ?? [];
  });

  @ViewChild('deviationSelectionList', { static: false }) selectionList: MatSelectionList;

  constructor() {
    effect(() => {
      this.deviations();
      this.selectionList?.deselectAll();
    });
  }

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const deviation = event.options[0].value;
      this.service.selectedDeviationChanged(deviation);
    }
  }

  josmZoomToSelectedDeviation(deviation: MonitorRouteDeviation): void {
    this.editService.edit({
      bounds: deviation.bounds,
    });
  }

  zoomToDeviationInOpenstreetMap(deviation: MonitorRouteDeviation): void {
    const bounds = deviation.bounds;
    const url = `https://www.openstreetmap.org/?bbox=${bounds.minLon},${bounds.minLat},${bounds.maxLon},${bounds.maxLat}`;
    window.open(url, 'openstreetmap');
  }

  isDeviationSelected(deviation: MonitorRouteDeviation | null): boolean {
    return this.selectedDeviation()?.id === deviation.id;
  }
}
