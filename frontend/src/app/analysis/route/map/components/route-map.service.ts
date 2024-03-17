import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteMapInfo } from '@api/common/route';
import { OlUtil } from '@app/ol';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { RouteLayers } from '@app/ol/layers';
import { MapClickService } from '@app/ol/services';
import { OpenlayersMapService } from '@app/ol/services';
import { NodeMapStyle } from '@app/ol/style';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';

@Injectable()
export class RouteMapService extends OpenlayersMapService {
  private readonly mapClickService = inject(MapClickService);

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

    this.finalizeSetup(true);
  }

  private registerLayers(routeMapInfo: RouteMapInfo): void {
    const networkVectorTileLayer = NetworkVectorTileLayer.build(
      routeMapInfo.networkType,
      new NodeMapStyle().styleFunction()
    );
    const routeLayers = new RouteLayers(routeMapInfo.map).build();
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
    const min = OlUtil.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = OlUtil.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }
}
