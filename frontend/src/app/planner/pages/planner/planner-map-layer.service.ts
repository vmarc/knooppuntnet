import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteTypes } from '@app/kpn/common';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { OldOpenDataLayers } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { OldMapLayer } from '@app/ol/layers';
import { TileDebug512Layer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { OldOsmLayer } from '@app/ol/layers';
import { OldBackgroundLayer } from '@app/ol/layers';
import { OldMapLayerRegistry } from '@app/ol/layers';
import { OldPoiTileLayerService } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { MainMapStyle } from '@app/ol/style';
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
