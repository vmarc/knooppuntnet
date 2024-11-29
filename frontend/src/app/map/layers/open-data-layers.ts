import { NetworkType } from '@api/custom';
import { OldMapLayer } from './old-map-layer';
import { OpendataBitmapTileLayer } from './opendata-bitmap-tile-layer';
import { OpendataVectorTileLayer } from './opendata-vector-tile-layer';

export class OpenDataLayers {
  private static readonly flandersHikingName = $localize`:@@map.layer.flanders-hiking:Toerisme Vlaanderen (hiking)`;
  private static readonly flandersCyclingName = $localize`:@@map.layer.flanders-cycling:Toerisme Vlaanderen (cycling)`;
  private static readonly netherlandsHikingName = $localize`:@@map.layer.netherlands-hiking:NL routedatabank (hiking)`;
  private static readonly netherlandsCyclingName = $localize`:@@map.layer.netherlands-cycling:NL routedatabank (cycling)`;
  private static readonly franceHikingName = $localize`:@@map.layer.france-hiking:Parc du Vercors`;

  // static register(
  //   registry: MapLayerRegistry,
  //   networkType: NetworkType,
  //   urlLayerIds: string[]
  // ): void {
  //   if (networkType == NetworkType.hiking) {
  //     registry.registerAll(urlLayerIds, OpenDataLayers.flandersHiking(), false);
  //     registry.registerAll(urlLayerIds, OpenDataLayers.netherlandsHiking(), false);
  //     registry.registerAll(urlLayerIds, OpenDataLayers.franceHiking(), false);
  //   } else if (networkType == NetworkType.cycling) {
  //     registry.registerAll(urlLayerIds, OpenDataLayers.flandersCycling(), false);
  //     registry.registerAll(urlLayerIds, OpenDataLayers.netherlandsCycling(), false);
  //   }
  // }

  static flandersHiking(): OldMapLayer[] {
    return [this.flandersHikingBitmap(), this.flandersHikingVector()];
  }

  private static flandersHikingBitmap(): OldMapLayer {
    return OpendataBitmapTileLayer.build(
      NetworkType.hiking,
      'flanders-hiking',
      this.flandersHikingName,
      'flanders/hiking'
    );
  }

  private static flandersHikingVector(): OldMapLayer {
    return OpendataVectorTileLayer.build(
      NetworkType.hiking,
      'flanders-hiking',
      this.flandersHikingName,
      'flanders/hiking'
    );
  }

  static flandersCycling(): OldMapLayer[] {
    return [this.flandersCyclingBitmap(), this.flandersCyclingVector()];
  }

  private static flandersCyclingBitmap(): OldMapLayer {
    return OpendataBitmapTileLayer.build(
      NetworkType.cycling,
      'flanders-cycling',
      this.flandersCyclingName,
      'flanders/cycling'
    );
  }

  private static flandersCyclingVector(): OldMapLayer {
    return OpendataVectorTileLayer.build(
      NetworkType.cycling,
      'flanders-cycling',
      this.flandersCyclingName,
      'flanders/cycling'
    );
  }

  static netherlandsHiking(): OldMapLayer[] {
    return [this.netherlandsHikingBitmap(), this.netherlandsHikingVector()];
  }

  private static netherlandsHikingBitmap(): OldMapLayer {
    return OpendataBitmapTileLayer.build(
      NetworkType.hiking,
      'netherlands-hiking',
      this.netherlandsHikingName,
      'netherlands/hiking'
    );
  }

  private static netherlandsHikingVector(): OldMapLayer {
    return OpendataVectorTileLayer.build(
      NetworkType.hiking,
      'netherlands-hiking',
      this.netherlandsHikingName,
      'netherlands/hiking'
    );
  }

  static netherlandsCycling(): OldMapLayer[] {
    return [this.netherlandsCyclingBitmap(), this.netherlandsCyclingVector()];
  }

  private static netherlandsCyclingBitmap(): OldMapLayer {
    return OpendataBitmapTileLayer.build(
      NetworkType.cycling,
      'netherlands-cycling',
      this.netherlandsCyclingName,
      'netherlands/cycling'
    );
  }

  private static netherlandsCyclingVector(): OldMapLayer {
    return OpendataVectorTileLayer.build(
      NetworkType.cycling,
      'netherlands-cycling',
      this.netherlandsCyclingName,
      'netherlands/cycling'
    );
  }

  static franceHiking(): OldMapLayer[] {
    return [this.franceHikingBitmap(), this.franceHikingVector()];
  }

  private static franceHikingBitmap(): OldMapLayer {
    return OpendataBitmapTileLayer.build(
      NetworkType.hiking,
      'france-hiking',
      this.franceHikingName,
      'france/hiking'
    );
  }

  private static franceHikingVector(): OldMapLayer {
    return OpendataVectorTileLayer.build(
      NetworkType.hiking,
      'france-hiking',
      this.franceHikingName,
      'france/hiking'
    );
  }
}
