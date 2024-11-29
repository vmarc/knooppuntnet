import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { NetworkTypes } from '@app/kpn/common';
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
    networkType: NetworkType,
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
      networkType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.flandersCycling(),
      false,
      networkType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.netherlandsHiking(),
      false,
      networkType === 'hiking'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.netherlandsCycling(),
      false,
      networkType === 'cycling'
    );

    registry.registerAll(
      urlLayerIds,
      OldOpenDataLayers.franceHiking(),
      false,
      networkType === 'hiking'
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
  ): OldMapLayer[] {
    const networkVectorLayerStyle = new MainMapStyle(parameters);
    return [
      NetworkBitmapTileLayer.build(networkType, 'surface'),
      NetworkBitmapTileLayer.build(networkType, 'survey'),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
      NetworkVectorTileLayer.build(networkType, networkVectorLayerStyle.styleFunction()),
    ];
  }
}
