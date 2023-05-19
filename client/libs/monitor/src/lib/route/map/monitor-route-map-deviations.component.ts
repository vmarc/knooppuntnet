import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
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
    <div
      *ngIf="
        service.referenceAvailable();
        then showReferenceAvailable;
        else showNoReference
      "
    ></div>
    <ng-template #showNoReference>
      <p i18n="@@monitor.route.map-deviations.no-reference">
        No reference, so no analysis results.
      </p>
    </ng-template>
    <ng-template #showReferenceAvailable>
      <div
        *ngIf="
          service.osmRelationAvailable();
          then showOsmRelationAvailable;
          else showOsmRelationMissing
        "
      ></div>
      <ng-template #showOsmRelationMissing>
        <p i18n="@@monitor.route.map-deviations.no-relation">
          OSM relation missing in route definition, so no analysis results.
        </p>
      </ng-template>
      <ng-template #showOsmRelationAvailable>
        <p
          *ngIf="service.osmRelationEmpty()"
          i18n="@@monitor.route.map-deviations.relation-empty"
        >
          OSM relation empty, so no analysis results.
        </p>
        <div
          *ngIf="
            service.deviations().length > 0;
            then showDeviations;
            else showNoDeviations
          "
        ></div>
        <ng-template #showNoDeviations>
          <p class="kpn-spacer-above kpn-line">
            <kpn-icon-happy />
            <span i18n="@@monitor.route.map-deviations.no-deviations">
              No deviations
            </span>
          </p>
        </ng-template>

        <ng-template #showDeviations>
          <p class="segments-title">
            <span i18n="@@monitor.route.map-deviations.title">Deviations</span>
          </p>

          <div class="segment segment-header">
            <span class="segment-id">
              <kpn-legend-line color="red"></kpn-legend-line>
            </span>
            <span
              class="segment-deviation"
              i18n="@@monitor.route.map-deviations.deviation"
              >Deviation</span
            >
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
            [multiple]="false"
            (selectionChange)="selectionChanged($event)"
            [hideSingleSelectionIndicator]="true"
          >
            <mat-list-option
              *ngFor="let deviation of service.deviations()"
              [selected]="service.selectedDeviation()?.id === deviation.id"
              [value]="deviation"
            >
              <div class="segment">
                <span class="segment-id">{{ deviation.id }}</span>
                <span
                  *ngIf="deviation.distance === 2500"
                  class="segment-deviation"
                  >{{ longDistance }}</span
                >
                <span
                  *ngIf="deviation.distance !== 2500"
                  class="segment-deviation"
                  >{{ deviation.distance | distance }}</span
                >
                <span>{{ deviation.meters | distance }}</span>
                <button
                  mat-icon-button
                  [matMenuTriggerFor]="appMenu"
                  [matMenuTriggerData]="{ deviation: deviation }"
                  class="popup-menu"
                >
                  <mat-icon svgIcon="menu-dots"></mat-icon>
                </button>
              </div>
            </mat-list-option>
          </mat-selection-list>
        </ng-template>
      </ng-template>
    </ng-template>
  `,
  styles: [
    `
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
  ],
  standalone: true,
  imports: [
    DistancePipe,
    IconHappyComponent,
    LegendLineComponent,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatMenuModule,
    NgFor,
    NgIf,
  ],
})
export class MonitorRouteMapDeviationsComponent {
  readonly longDistance = '> 2.5 km';

  constructor(
    protected service: MonitorRouteMapStateService,
    private editService: EditService
  ) {}

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
}
