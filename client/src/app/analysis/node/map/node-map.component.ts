import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NodeMapInfo } from '@api/common/node-map-info';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { Util } from '@app/components/shared/util';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { OldMapPositionService } from '@app/components/ol/services/old-map-position.service';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { actionNodeMapLayerVisible } from '@app/analysis/node/store/node.actions';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';

@Component({
  selector: 'kpn-node-map',
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
export class NodeMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMapInfo: NodeMapInfo;
  @Input() mapPositionFromUrl: MapPosition;
  @Input() layerStates: MapLayerState[];
  @Input() layers: MapLayer[];

  protected map: OpenLayersMap;
  protected readonly mapId = 'node-map';

  constructor(
    private newMapService: NewMapService,
    private mapClickService: MapClickService,
    private mapPositionService: OldMapPositionService,
    private store: Store
  ) {}

  ngAfterViewInit(): void {
    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    if (this.mapPositionFromUrl) {
      const center: Coordinate = [
        this.mapPositionFromUrl.x,
        this.mapPositionFromUrl.y,
      ];
      const zoom = this.mapPositionFromUrl.zoom;
      viewOptions = {
        ...viewOptions,
        center,
        zoom,
      };
    } else {
      const center = Util.toCoordinate(
        this.nodeMapInfo.latitude,
        this.nodeMapInfo.longitude
      );
      viewOptions = {
        ...viewOptions,
        center,
        zoom: 18,
      };
    }

    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.map((mapLayer) => mapLayer.layer),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

    this.mapClickService.installOn(this.map.map);

    this.mapPositionService.install(this.map.map.getView());
  }

  layerStateChange(change: MapLayerState): void {
    this.store.dispatch(actionNodeMapLayerVisible(change));
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }
}
