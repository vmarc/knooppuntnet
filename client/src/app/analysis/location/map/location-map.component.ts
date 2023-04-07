import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import View from 'ol/View';
import { Util } from '@app/components/shared/util';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { MapService } from '@app/components/ol/services/map.service';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { Coordinate } from 'ol/coordinate';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { Store } from '@ngrx/store';
import { actionLocationMapPosition } from '@app/analysis/location/store/location.actions';
import { actionLocationMapLayerVisible } from '@app/analysis/location/store/location.actions';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';

@Component({
  selector: 'kpn-location-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-map">
      <kpn-temp-layer-switcher
        [layerStates]="layerStates"
        (layerStateChange)="layerStateChange($event)"
      />
      <kpn-old-map-link-menu [map]="map" />
    </div>
  `,
})
export class LocationMapComponent implements AfterViewInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() bounds: Bounds;
  @Input() geoJson: string;
  @Input() layerStates: MapLayerState[];
  @Input() layers: MapLayer[];

  protected map: OpenLayersMap;
  protected readonly mapId = 'location-map';
  private view: View;
  private readonly updatePositionHandler = () => this.updateMapPosition();

  constructor(
    private newMapService: NewMapService,
    private mapService: MapService,
    private mapClickService: MapClickService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    this.mapService.nextNetworkType(this.networkType);
    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.map((mapLayer) => mapLayer.layer),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom,
      }),
    });

    this.view = this.map.map.getView();
    this.view.on('change:resolution', () => this.updatePositionHandler);
    this.view.on('change:center', this.updatePositionHandler);

    this.view.fit(Util.toExtent(this.bounds, 0.05));

    this.mapClickService.installOn(this.map.map);

    this.updateMapPosition();
  }

  layerStateChange(change: MapLayerState): void {
    this.store.dispatch(actionLocationMapLayerVisible(change));
  }

  ngOnDestroy(): void {
    if (this.view) {
      this.view.un('change:resolution', this.updatePositionHandler);
      this.view.un('change:center', this.updatePositionHandler);
      this.view = null;
    }
    this.map.destroy();
  }

  private updateMapPosition(): void {
    const center: Coordinate = this.view.getCenter();
    if (center) {
      const zoom = this.view.getZoom();
      const z = Math.round(zoom);
      const mapPosition = new MapPosition(z, center[0], center[1], 0);
      this.store.dispatch(actionLocationMapPosition({ mapPosition }));
    }
  }
}
