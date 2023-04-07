import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { List } from 'immutable';
import View from 'ol/View';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { OldMapLayers } from '../layers/old-map-layers';
import { MapLayerService } from '../services/map-layer.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { PoiAreasLayer } from '@app/components/ol/layers/poi-areas-layer';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-poi-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="poi-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="layers" />
    </div>
  `,
})
export class PoiMapComponent implements AfterViewInit, OnDestroy {
  @Input() geoJson: string;

  protected layers: OldMapLayers;
  private map: OpenLayersMap;
  private readonly mapId = 'poi-map';

  constructor(
    private newMapService: NewMapService,
    private mapLayerService: MapLayerService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    const center = Util.toCoordinate('49.153', '2.4609');
    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        center,
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 8,
      }),
    });
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  private buildLayers(): OldMapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(
      this.mapLayerService.networkBitmapTileLayer(
        NetworkType.cycling,
        'analysis'
      )
    );
    mapLayers = mapLayers.concat(new PoiAreasLayer().build(this.geoJson));
    mapLayers = mapLayers.push(TileDebug256Layer.build());
    return new OldMapLayers(mapLayers);
  }
}
