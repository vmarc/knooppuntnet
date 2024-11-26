import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteMapPage } from '@api/common/route';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { OldOpenDataLayers } from '@app/ol/layers';
import { OldBackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { OldMapLayerRegistry } from '@app/ol/layers';
import { OldOsmLayer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { MapClickService } from '@app/ol/services';
import { OpenlayersMapService } from '@app/ol/services';
import { NodeMapStyle } from '@app/ol/style';
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
      page.routeMapInfo.networkType,
      new NodeMapStyle().styleFunction()
    );
    // const routeLayers = new RouteLayers(page.map).build();
    const registry = new OldMapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);
    registry.register(urlLayerIds, networkVectorTileLayer, true);
    // routeLayers.forEach((mapLayer) => registry.register(urlLayerIds, mapLayer, true));
    OldOpenDataLayers.register(registry, page.routeMapInfo.networkType, urlLayerIds);
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
