import { ChangeDetectionStrategy } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatMenuTrigger } from '@angular/material/menu';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';

@Component({
  selector: 'kpn-layer-switcher',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-menu #mapMenu="matMenu" class="map-control-menu">
      <ng-template matMenuContent>
        <div *ngIf="!!this.layerStates" (mouseleave)="closePanel()">
          <div *ngFor="let layerState of layerStates">
            <mat-checkbox
              (click)="$event.stopPropagation()"
              [checked]="layerState.visible"
              (change)="layerVisibleChanged(layerState, $event)"
            >
              {{ layerNameTranslation(layerState)}}
            </mat-checkbox>
          </div>
          <ng-content></ng-content>
        </div>
      </ng-template>
    </mat-menu>

    <div class="map-control map-layers-control" (mouseenter)="openPopupMenu()">
      <button class="map-control-button" [matMenuTriggerFor]="mapMenu">
        <mat-icon svgIcon="layers"/>
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
  @Input() layerStates: MapLayerState[];
  @Output() layerStateChange = new EventEmitter<MapLayerState>();
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;

  constructor(private mapLayerService: MapLayerService) {}

  openPopupMenu(): void {
    this.trigger.openMenu();
  }

  closePanel(): void {
    this.trigger.closeMenu();
  }

  layerVisibleChanged(
    layerState: MapLayerState,
    event: MatCheckboxChange
  ): void {
    const change: MapLayerState = {
      ...layerState,
      visible: event.checked,
    };
    this.layerStateChange.emit(change);
  }

  layerNameTranslation(layerstate: MapLayerState): string {
    return this.mapLayerService.translation(layerstate.layerName);
  }
}
