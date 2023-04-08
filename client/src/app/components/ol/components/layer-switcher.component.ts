import { ChangeDetectionStrategy } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatMenuTrigger } from '@angular/material/menu';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { MAP_SERVICE_TOKEN } from '@app/components/ol/services/openlayers-map-service';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';

@Component({
  selector: 'kpn-layer-switcher',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-menu #mapMenu="matMenu" class="map-control-menu">
      <ng-template matMenuContent>
        <div
          *ngIf="layerStates$ | async as layerStates"
          (click)="$event.stopPropagation()"
        >
          <div
            *ngFor="let layerState of layerStates"
            [hidden]="!layerState.enabled"
          >
            <mat-checkbox
              (click)="$event.stopPropagation()"
              [checked]="layerState.visible"
              (change)="layerVisibleChanged(layerState, $event)"
            >
              {{ layerNameTranslation(layerState) }}
            </mat-checkbox>
            <mat-divider *ngIf="layerState.layerName === 'osm'"></mat-divider>
          </div>
          <ng-content></ng-content>
        </div>
      </ng-template>
    </mat-menu>

    <div class="map-control map-layers-control" (click)="openPopupMenu()">
      <button
        class="map-control-button"
        [matMenuTriggerFor]="mapMenu"
        title="select the layers displayed in the map"
        i18n-title="@@layer-switcher.title"
      >
        <mat-icon svgIcon="layers" />
      </button>
    </div>
  `,
  styles: [
    `
      .map-layers-control {
        top: 90px;
        right: 10px;
      }
    `,
  ],
})
export class LayerSwitcherComponent {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
  protected readonly layerStates$ = this.openlayersMapService.layerStates$;

  constructor(
    @Inject(MAP_SERVICE_TOKEN)
    private openlayersMapService: OpenlayersMapService,
    private mapLayerService: MapLayerService
  ) {}

  openPopupMenu(): void {
    this.trigger.openMenu();
  }

  layerVisibleChanged(
    layerState: MapLayerState,
    event: MatCheckboxChange
  ): void {
    const change: MapLayerState = {
      ...layerState,
      visible: event.checked,
    };
    this.openlayersMapService.layerStateChange(change);
  }

  layerNameTranslation(layerstate: MapLayerState): string {
    return this.mapLayerService.translation(layerstate.layerName);
  }
}
