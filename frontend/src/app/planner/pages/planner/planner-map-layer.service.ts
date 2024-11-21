import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { NetworkTypes } from '@app/kpn/common';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { OpenDataLayers } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { MapLayer } from '@app/ol/layers';
import { TileDebug512Layer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { OldOsmLayer } from '@app/ol/layers';
import { OldBackgroundLayer } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { PoiTileLayerService } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { MainMapStyle } from '@app/ol/style';
import { PoiService } from '@app/services';

@Injectable()
export class PlannerMapLayerService {
  private readonly poiService = inject(PoiService);
  private readonly poiTileLayerService = inject(PoiTileLayerService);

  registerLayers(
    networkType: NetworkType,
    urlLayerIds: string[],
    parameters: Signal<MainMapStyleParameters>
  ): MapLayerRegistry {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);

    registry.registerAll(
      urlLayerIds,
      OpenDataLayers.flandersHiking(),
      false,
      networkType === NetworkType.hiking
    );

    registry.registerAll(
      urlLayerIds,
      OpenDataLayers.flandersCycling(),
      false,
      networkType === NetworkType.cycling
    );

    registry.registerAll(
      urlLayerIds,
      OpenDataLayers.netherlandsHiking(),
      false,
      networkType === NetworkType.hiking
    );

    registry.registerAll(
      urlLayerIds,
      OpenDataLayers.netherlandsCycling(),
      false,
      networkType === NetworkType.cycling
    );

    registry.registerAll(
      urlLayerIds,
      OpenDataLayers.franceHiking(),
      false,
      networkType === NetworkType.hiking
    );

    NetworkTypes.all.forEach((layerNetworkType) => {
      registry.registerAll(
        urlLayerIds,
        this.networkLayers(layerNetworkType, parameters),
        layerNetworkType === networkType,
        layerNetworkType === networkType
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
    networkType: NetworkType,
    parameters: Signal<MainMapStyleParameters>
  ): MapLayer[] {
    const networkVectorLayerStyle = new MainMapStyle(parameters);
    return [
      NetworkBitmapTileLayer.build(networkType, 'surface'),
      NetworkBitmapTileLayer.build(networkType, 'survey'),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
      NetworkVectorTileLayer.build(networkType, networkVectorLayerStyle.styleFunction()),
    ];
  }
}
