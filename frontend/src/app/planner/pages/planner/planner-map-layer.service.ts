import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteTypes } from '@app/kpn/common';
import { NetworkBitmapTileLayer } from '@app/ol/layers/network-bitmap-tile-layer';
import { OldOpenDataLayers } from '@app/ol/layers/old-open-data-layers';
import { NetworkVectorTileLayer } from '@app/ol/layers/network-vector-tile-layer';
import { OldMapLayer } from '@app/ol/layers/old-map-layer';
import { TileDebug512Layer } from '@app/ol/layers/tile-debug-512-layer';
import { TileDebug256Layer } from '@app/ol/layers/tile-debug-256-layer';
import { OldOsmLayer } from '@app/ol/layers/old-osm-layer';
import { OldBackgroundLayer } from '@app/ol/layers/old-background-layer';
import { OldMapLayerRegistry } from '@app/ol/layers/old-map-layer-registry';
import { OldPoiTileLayerService } from '@app/ol/services/old-poi-tile-layer.service';
import { MainMapStyleParameters } from '@app/ol/style/main-map-style-parameters';
import { MainMapStyle } from '@app/ol/style/main-map-style';
import { OldPoiService } from '@app/services';

@Injectable()
export class PlannerMapLayerService {
  private readonly poiService = inject(OldPoiService);
  private readonly poiTileLayerService = inject(OldPoiTileLayerService);

  registerLayers(
    routeType: RouteType,
    urlLayerIds: string[],
    parameters: Signal<MainMapStyleParameters>
  ): OldMapLayerRegistry {
    const registry = new OldMapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.flandersHiking(),
      false,
      routeType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.flandersCycling(),
      false,
      routeType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.netherlandsHiking(),
      false,
      routeType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.netherlandsCycling(),
      false,
      routeType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.franceHiking(),
      false,
      routeType === 'hiking'
    );

    RouteTypes.all.forEach((layerRouteType) => {
      registry.registerAll(
        urlLayerIds,
        this.networkLayers(layerRouteType, parameters),
        layerRouteType === routeType,
        layerRouteType === routeType
      );
    });

    registry.register(urlLayerIds, TileDebug256Layer.build(), false);
    registry.register(urlLayerIds, TileDebug512Layer.build(), false);

    let poiDefaultVisible = false;
    if (urlLayerIds.length === 0) {
      poiDefaultVisible = this.poiService.isEnabled();
    }
    registry.register(urlLayerIds, this.poiTileLayerService.buildLayer(), poiDefaultVisible, true);

    return registry;
  }

  private networkLayers(
    routeType: RouteType,
    parameters: Signal<MainMapStyleParameters>
  ): OldMapLayer[] {
    const networkVectorLayerStyle = new MainMapStyle(parameters);
    return [
      NetworkBitmapTileLayer.build(routeType, 'surface'),
      NetworkBitmapTileLayer.build(routeType, 'survey'),
      NetworkBitmapTileLayer.build(routeType, 'analysis'),
      NetworkVectorTileLayer.build(routeType, networkVectorLayerStyle.styleFunction()),
    ];
  }
}
