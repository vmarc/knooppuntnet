import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { PoiDetail } from '@app/kpn/api/common/poi-detail';
import { List } from 'immutable';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';

@Component({
  selector: 'kpn-poi-detail-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <div id="poi-detail-map" class="poi-map"></div> `,
  styles: [
    `
      .poi-map {
        width: 512px;
        height: 320px;
      }
    `,
  ],
})
export class PoiDetailMapComponent implements AfterViewInit, OnDestroy {
  @Input() poiDetail: PoiDetail;

  layers: MapLayers;
  private map: Map;
  private readonly mapId = 'poi-detail-map';

  constructor(private mapLayerService: MapLayerService) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    const center = Util.toCoordinate(
      this.poiDetail.poi.latitude,
      this.poiDetail.poi.longitude
    );
    viewOptions = {
      ...viewOptions,
      center,
      zoom: 18,
    };

    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

    this.layers.applyMap(this.map);
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.setTarget(null);
    }
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(
      this.mapLayerService.backgroundLayer(this.mapId)
    );
    mapLayers = mapLayers.push(
      this.mapLayerService.poiMarkerLayer(this.poiDetail)
    );
    return new MapLayers(mapLayers);
  }
}
