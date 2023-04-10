import { Injectable } from '@angular/core';
import { RouteMapInfo } from '@api/common/route/route-map-info';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { OldMapPositionService } from '@app/components/ol/services/old-map-position.service';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { Util } from '@app/components/shared/util';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { NetworkVectorTileLayer } from '../../../components/ol/layers/network-vector-tile-layer';
import { RouteLayers } from '../../../components/ol/layers/route-layers';
import { NodeMapStyle } from '../../../components/ol/style/node-map-style';
import { I18nService } from '../../../i18n/i18n.service';

@Injectable()
export class RouteMapService extends OpenlayersMapService {
  constructor(
    private mapClickService: MapClickService,
    private mapPositionService: OldMapPositionService,
    private i18nService: I18nService
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
    const networkVectorTileLayer = NetworkVectorTileLayer.build(
      routeMapInfo.networkType,
      new NodeMapStyle().styleFunction()
    );
    const routeLayers = new RouteLayers(
      this.i18nService,
      routeMapInfo.map
    ).build();
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
