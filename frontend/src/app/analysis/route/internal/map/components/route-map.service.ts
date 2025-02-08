import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { MapPosition } from '@app/ol/domain/map-position';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { OldOpenDataLayers } from '@app/ol/layers/old-open-data-layers';
import { OldBackgroundLayer } from '@app/ol/layers/old-background-layer';
import { MapControls } from '@app/ol/layers/map-controls';
import { OldMapLayerRegistry } from '@app/ol/layers/old-map-layer-registry';
import { OldOsmLayer } from '@app/ol/layers/old-osm-layer';
import { TileDebug256Layer } from '@app/ol/layers/tile-debug-256-layer';
import { NetworkVectorTileLayer } from '@app/ol/layers/network-vector-tile-layer';
import { MapClickService } from '@app/ol/services/map-click.service';
import { OpenlayersMapService } from '@app/ol/services/openlayers-map-service';
import { NodeMapStyle } from '@app/ol/style/node-map-style';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import Map from 'ol/Map';
import { fromLonLat } from 'ol/proj';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';

@Injectable()
export class RouteMapService extends OpenlayersMapService {
  private readonly mapClickService = inject(MapClickService);

  init(routeMapPage: RouteMapPage, mapPositionFromUrl: MapPosition, urlLayerIds: string[]): void {
    this.registerLayers(routeMapPage, urlLayerIds);

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
      view.fit(this.buildExtent(routeMapPage));
    }

    this.finalizeSetup(true);
  }

  private registerLayers(page: RouteMapPage, urlLayerIds: string[]): void {
    const networkVectorTileLayer = NetworkVectorTileLayer.build(
      page.routeMapInfo.routeType,
      new NodeMapStyle().styleFunction()
    );
    // const routeLayers = new RouteLayers(page.map).build();
    const registry = new OldMapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);
    registry.register(urlLayerIds, networkVectorTileLayer, true);
    // routeLayers.forEach((mapLayer) => registry.register(urlLayerIds, mapLayer, true));
    OldOpenDataLayers.register(registry, page.routeMapInfo.routeType, urlLayerIds);
    registry.register(urlLayerIds, TileDebug256Layer.build(), false);

    this.register(registry);
  }

  private buildExtent(page: RouteMapPage): Extent {
    const bounds = page.bounds;
    const latExtra = (bounds.maxLat - bounds.minLat) / 5;
    const lonExtra = (bounds.maxLon - bounds.minLon) / 5;
    const latMinAdjusted = bounds.minLat - latExtra;
    const latMaxAdjusted = bounds.maxLat + latExtra;
    const lonMinAdjusted = bounds.minLon - lonExtra;
    const lonMaxAdjusted = bounds.maxLon + lonExtra;
    const min = fromLonLat([lonMinAdjusted, latMinAdjusted]);
    const max = fromLonLat([lonMaxAdjusted, latMaxAdjusted]);
    return [min[0], min[1], max[0], max[1]];
  }
}
