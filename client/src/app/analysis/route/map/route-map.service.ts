import { Injectable } from '@angular/core';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { RouteMapInfo } from '@api/common/route/route-map-info';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import Map from 'ol/Map';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Coordinate } from 'ol/coordinate';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { Extent } from 'ol/extent';
import { Util } from '@app/components/shared/util';
import { OldMapPositionService } from '@app/components/ol/services/old-map-position.service';

@Injectable()
export class RouteMapService extends OpenlayersMapService {
  constructor(
    private mapLayerService: MapLayerService,
    private mapClickService: MapClickService,
    private mapPositionService: OldMapPositionService
  ) {
    super();
  }

  init(routeMapInfo: RouteMapInfo, mapPositionFromUrl: MapPosition): void {
    this.registerLayers(routeMapInfo);

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.minZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    if (mapPositionFromUrl) {
      const center: Coordinate = [mapPositionFromUrl.x, mapPositionFromUrl.y];
      const zoom = mapPositionFromUrl.zoom;
      viewOptions = {
        ...viewOptions,
        center,
        zoom,
      };
    }

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View(viewOptions),
      })
    );

    this.mapClickService.installOn(this.map);

    const view = this.map.getView();

    if (!mapPositionFromUrl) {
      view.fit(this.buildExtent(routeMapInfo));
    }

    this.mapPositionService.install(this.map.getView());

    this.finalizeSetup();
  }

  private registerLayers(routeMapInfo: RouteMapInfo): void {
    const networkVectorTileLayer = this.mapLayerService.networkVectorTileLayer(
      routeMapInfo.networkType
    );
    const routeLayers = this.mapLayerService.routeLayers(routeMapInfo.map);
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.register([], networkVectorTileLayer, true);
    routeLayers.forEach((mapLayer) => registry.register([], mapLayer, true));
    registry.register([], TileDebug256Layer.build(), false);

    this.register(registry);
  }

  private buildExtent(routeMapInfo: RouteMapInfo): Extent {
    const bounds = routeMapInfo.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }
}
