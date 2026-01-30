import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteTypes } from '@app/shared/kpn/common/route-types';
import { NetworkBitmapTileLayer } from '@app/ol/layers/network-bitmap-tile-layer';
import { OldOldOpenDataLayers } from '@app/ol/layers/old-old-open-data-layers';
import { NetworkVectorTileLayer } from '@app/ol/layers/network-vector-tile-layer';
import { OldOldMapLayer } from '@app/ol/layers/old-old-map-layer';
import { TileDebug512Layer } from '@app/ol/layers/tile-debug-512-layer';
import { TileDebug256Layer } from '@app/ol/layers/tile-debug-256-layer';
import { OldOldMapLayerRegistry } from '@app/ol/layers/old-old-map-layer-registry';
import { OldPoiTileLayerService } from '@app/ol/services/old-poi-tile-layer.service';
import { MainMapStyleParameters } from '@app/ol/style/main-map-style-parameters';
import { MainMapStyle } from '@app/ol/style/main-map-style';
import { OldOldPoiService } from '@app/shared/services/old-old-poi.service';

@Injectable()
export class PlannerMapLayerService {
  private readonly poiService = inject(OldOldPoiService);
  private readonly poiTileLayerService = inject(OldPoiTileLayerService);

  registerLayers(
    routeType: RouteType,
    urlLayerIds: string[],
    parameters: Signal<MainMapStyleParameters>
  ): OldOldMapLayerRegistry {
    const registry = new OldOldMapLayerRegistry();

    registry.registerAll(
      urlLayerIds,
      OldOldOpenDataLayers.flandersHiking(),
      false,
      routeType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOldOpenDataLayers.flandersCycling(),
      false,
      routeType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOldOpenDataLayers.netherlandsHiking(),
      false,
      routeType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOldOpenDataLayers.netherlandsCycling(),
      false,
      routeType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOldOpenDataLayers.franceHiking(),
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
  ): OldOldMapLayer[] {
    const networkVectorLayerStyle = new MainMapStyle(parameters);
    return [
      NetworkBitmapTileLayer.build(routeType, 'surface'),
      NetworkBitmapTileLayer.build(routeType, 'survey'),
      NetworkBitmapTileLayer.build(routeType, 'analysis'),
      NetworkVectorTileLayer.build(routeType, networkVectorLayerStyle.styleFunction()),
    ];
  }
}
