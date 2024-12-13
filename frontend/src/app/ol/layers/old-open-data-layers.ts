import { NetworkType } from '@api/common';
import { OldMapLayer } from './old-map-layer';
import { OldMapLayerRegistry } from './old-map-layer-registry';
import { OldOpendataBitmapTileLayer } from './old-opendata-bitmap-tile-layer';
import { OldOpendataVectorTileLayer } from './old-opendata-vector-tile-layer';

export class OldOpenDataLayers {
  private static readonly flandersHikingName = $localize`:@@map.layer.flanders-hiking:Toerisme Vlaanderen (hiking)`;
  private static readonly flandersCyclingName = $localize`:@@map.layer.flanders-cycling:Toerisme Vlaanderen (cycling)`;
  private static readonly netherlandsHikingName = $localize`:@@map.layer.netherlands-hiking:NL routedatabank (hiking)`;
  private static readonly netherlandsCyclingName = $localize`:@@map.layer.netherlands-cycling:NL routedatabank (cycling)`;
  private static readonly franceHikingName = $localize`:@@map.layer.france-hiking:Parc du Vercors`;

  static register(
    registry: OldMapLayerRegistry,
    networkType: NetworkType,
    urlLayerIds: string[]
  ): void {
    if (networkType == 'hiking') {
      registry.registerAll(urlLayerIds, OldOpenDataLayers.flandersHiking(), false);
      registry.registerAll(urlLayerIds, OldOpenDataLayers.netherlandsHiking(), false);
      registry.registerAll(urlLayerIds, OldOpenDataLayers.franceHiking(), false);
    } else if (networkType == 'cycling') {
      registry.registerAll(urlLayerIds, OldOpenDataLayers.flandersCycling(), false);
      registry.registerAll(urlLayerIds, OldOpenDataLayers.netherlandsCycling(), false);
    }
  }

  static flandersHiking(): OldMapLayer[] {
    return [this.flandersHikingBitmap(), this.flandersHikingVector()];
  }

  private static flandersHikingBitmap(): OldMapLayer {
    return OldOpendataBitmapTileLayer.build(
      'hiking',
      'flanders-hiking',
      this.flandersHikingName,
      'flanders/hiking'
    );
  }

  private static flandersHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'flanders-hiking',
      this.flandersHikingName,
      'flanders/hiking'
    );
  }

  static flandersCycling(): OldMapLayer[] {
    return [this.flandersCyclingBitmap(), this.flandersCyclingVector()];
  }

  private static flandersCyclingBitmap(): OldMapLayer {
    return OldOpendataBitmapTileLayer.build(
      'cycling',
      'flanders-cycling',
      this.flandersCyclingName,
      'flanders/cycling'
    );
  }

  private static flandersCyclingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'cycling',
      'flanders-cycling',
      this.flandersCyclingName,
      'flanders/cycling'
    );
  }

  static netherlandsHiking(): OldMapLayer[] {
    return [this.netherlandsHikingBitmap(), this.netherlandsHikingVector()];
  }

  private static netherlandsHikingBitmap(): OldMapLayer {
    return OldOpendataBitmapTileLayer.build(
      'hiking',
      'netherlands-hiking',
      this.netherlandsHikingName,
      'netherlands/hiking'
    );
  }

  private static netherlandsHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'netherlands-hiking',
      this.netherlandsHikingName,
      'netherlands/hiking'
    );
  }

  static netherlandsCycling(): OldMapLayer[] {
    return [this.netherlandsCyclingBitmap(), this.netherlandsCyclingVector()];
  }

  private static netherlandsCyclingBitmap(): OldMapLayer {
    return OldOpendataBitmapTileLayer.build(
      'cycling',
      'netherlands-cycling',
      this.netherlandsCyclingName,
      'netherlands/cycling'
    );
  }

  private static netherlandsCyclingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'cycling',
      'netherlands-cycling',
      this.netherlandsCyclingName,
      'netherlands/cycling'
    );
  }

  static franceHiking(): OldMapLayer[] {
    return [this.franceHikingBitmap(), this.franceHikingVector()];
  }

  private static franceHikingBitmap(): OldMapLayer {
    return OldOpendataBitmapTileLayer.build(
      'hiking',
      'france-hiking',
      this.franceHikingName,
      'france/hiking'
    );
  }

  private static franceHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'france-hiking',
      this.franceHikingName,
      'france/hiking'
    );
  }
}
