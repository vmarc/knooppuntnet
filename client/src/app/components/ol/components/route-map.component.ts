import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { RouteMapInfo } from '@api/common/route/route-map-info';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { Util } from '../../shared/util';
import { MapPosition } from '../domain/map-position';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapClickService } from '../services/map-click.service';
import { MapLayerService } from '../services/map-layer.service';
import { MapService } from '../services/map.service';
import { OldMapPositionService } from '../services/old-map-position.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-route-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="route-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="layers" />
      <kpn-map-link-menu [openLayersMap]="map" />
    </div>
  `,
})
export class RouteMapComponent implements AfterViewInit, OnDestroy {
  @Input() routeMapInfo: RouteMapInfo;
  @Input() mapPositionFromUrl: MapPosition;

  protected map: OpenLayersMap;
  protected layers: MapLayers;
  private networkVectorTileLayer: MapLayer;
  private networkVectorTileLayerActive = true;

  private readonly mapId = 'route-map';

  constructor(
    private newMapService: NewMapService,
    private mapService: MapService,
    private mapClickService: MapClickService,
    private mapLayerService: MapLayerService,
    private mapPositionService: OldMapPositionService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.minZoom,
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
    }

    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View(viewOptions),
    });

    this.mapClickService.installOn(this.map.map);

    const view = this.map.map.getView();

    if (!this.mapPositionFromUrl) {
      view.fit(this.buildExtent());
    }

    view.on('change:resolution', () => {
      if (view.getZoom() < ZoomLevel.vectorTileMinZoom) {
        if (this.networkVectorTileLayerActive) {
          this.map.map.removeLayer(this.networkVectorTileLayer.layer);
          this.networkVectorTileLayerActive = false;
        }
      } else {
        if (this.networkVectorTileLayerActive === false) {
          this.map.map
            .getLayers()
            .insertAt(1, this.networkVectorTileLayer.layer);
          this.networkVectorTileLayerActive = true;
        }
      }
    });
    this.mapPositionService.install(this.map.map.getView());
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  private buildExtent(): Extent {
    const bounds = this.routeMapInfo.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }

  private buildLayers(): MapLayers {
    this.networkVectorTileLayer = this.mapLayerService.networkVectorTileLayer(
      this.routeMapInfo.networkType
    );
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(this.networkVectorTileLayer);
    mapLayers = mapLayers.concat(
      this.mapLayerService.routeLayers(this.routeMapInfo.map)
    );
    mapLayers = mapLayers.push(new TileDebug256Layer().build());
    return new MapLayers(mapLayers);
  }
}
