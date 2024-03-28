import { Signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { NetworkTypes } from '@app/kpn/common';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { OpendataVectorTileLayer } from '@app/ol/layers';
import { OpendataBitmapTileLayer } from '@app/ol/layers';
import { MapLayer } from '@app/ol/layers';
import { TileDebug512Layer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { BackgroundLayer } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { PoiTileLayerService } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { MainMapStyle } from '@app/ol/style';

@Injectable()
export class PlannerMapLayerService {
  private readonly poiTileLayerService = inject(PoiTileLayerService);

  registerLayers(
    networkType: NetworkType,
    urlLayerIds: string[],
    parameters: Signal<MainMapStyleParameters>
  ): MapLayerRegistry {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, BackgroundLayer.build(), true);
    registry.register(urlLayerIds, OsmLayer.build(), false);

    registry.registerAll(
      urlLayerIds,
      this.flandersOpenDataHikingLayers(),
      false,
      networkType === NetworkType.hiking
    );

    registry.registerAll(
      urlLayerIds,
      this.flandersOpenDataCyclingLayers(),
      false,
      networkType === NetworkType.cycling
    );

    registry.registerAll(
      urlLayerIds,
      this.netherlandsHikingOpenDataLayers(),
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
    registry.register(urlLayerIds, this.poiTileLayerService.buildLayer(), true, false);

    return registry;
  }

  private flandersOpenDataHikingLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.flanders-hiking:Toerisme Vlaanderen`;
    return [
      OpendataBitmapTileLayer.build(NetworkType.hiking, 'flanders-hiking', name, 'flanders/hiking'),
      OpendataVectorTileLayer.build(NetworkType.hiking, 'flanders-hiking', name, 'flanders/hiking'),
    ];
  }

  private flandersOpenDataCyclingLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.flanders-cycling:Toerisme Vlaanderen`;
    return [
      OpendataBitmapTileLayer.build(
        NetworkType.cycling,
        'flanders-cycling',
        name,
        'flanders/cycling'
      ),
      OpendataVectorTileLayer.build(
        NetworkType.cycling,
        'flanders-cycling',
        name,
        'flanders/cycling'
      ),
    ];
  }

  private netherlandsHikingOpenDataLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.netherlands-hiking:Netherlands routedatabank`;
    return [
      OpendataBitmapTileLayer.build(
        NetworkType.hiking,
        'netherlands-hiking',
        name,
        'netherlands/hiking'
      ),
      OpendataVectorTileLayer.build(
        NetworkType.hiking,
        'netherlands-hiking',
        name,
        'netherlands/hiking'
      ),
    ];
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
