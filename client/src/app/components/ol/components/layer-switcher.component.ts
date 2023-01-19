import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatMenuTrigger } from '@angular/material/menu';
import { List } from 'immutable';
import BaseLayer from 'ol/layer/Base';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';

@Component({
  selector: 'kpn-layer-switcher',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-menu #mapMenu="matMenu" class="map-control-menu">
      <ng-template matMenuContent>
        <div *ngIf="!!this.mapLayers" (mouseleave)="closePanel()">
          <div *ngFor="let layer of namedLayers()">
            <mat-checkbox
              (click)="$event.stopPropagation()"
              [checked]="isLayerVisible(layer)"
              (change)="layerVisibleChanged(layer, $event)"
            >
              {{ layerName(layer) }}
            </mat-checkbox>
          </div>
          <ng-content></ng-content>
        </div>
      </ng-template>
    </mat-menu>

    <div class="map-control map-layers-control" (mouseenter)="openPopupMenu()">
      <button class="map-control-button" [matMenuTriggerFor]="mapMenu">
        <mat-icon svgIcon="layers" />
      </button>
    </div>
  `,
  styles: [
    `
      .map-layers-control {
        top: 50px;
        right: 10px;
      }
    `,
  ],
})
export class LayerSwitcherComponent {
  @Input() mapLayers: MapLayers;

  open = false;
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;

  constructor(private mapLayerService: MapLayerService) {}

  openPopupMenu(): void {
    this.trigger.openMenu();
  }

  namedLayers(): List<BaseLayer> {
    return this.mapLayers.layers
      .map((ml) => ml.layer)
      .filter((layer) => layer.get('name'));
  }

  closePanel(): void {
    this.trigger.closeMenu();
  }

  isLayerVisible(layer: BaseLayer): boolean {
    return layer.getVisible();
  }

  layerVisibleChanged(layer: BaseLayer, event: MatCheckboxChange): void {
    layer.setVisible(event.checked);
    this.mapLayerService.storeMapLayerStates(this.mapLayers);
  }

  layerName(layer: BaseLayer): string {
    return layer.get('name');
  }
}
