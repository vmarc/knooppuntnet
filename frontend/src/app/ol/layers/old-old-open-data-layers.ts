import { RouteType } from '@api/common/route-type';
import { OldOldMapLayer } from './old-old-map-layer';
import { OldOldMapLayerRegistry } from './old-old-map-layer-registry';
import { OldOldOpendataBitmapTileLayer } from './old-old-opendata-bitmap-tile-layer';
import { OldOldOpendataVectorTileLayer } from './old-old-opendata-vector-tile-layer';

export class OldOldOpenDataLayers {
  private static readonly openDataFlanders = $localize`:@@map.layer.open-data-flanders:Toerisme Vlaanderen`;
  private static readonly openDataNetherlands = $localize`:@@map.layer.open-data-netherlands:NL routedatabank`;
  private static readonly openDataFrance = $localize`:@@map.layer.open-data-france:Parc du Vercors`;

  static register(
    registry: OldOldMapLayerRegistry,
    routeType: RouteType,
    urlLayerIds: string[]
  ): void {
    if (routeType == 'hiking') {
      registry.registerAll(urlLayerIds, OldOldOpenDataLayers.flandersHiking(), false);
      registry.registerAll(urlLayerIds, OldOldOpenDataLayers.netherlandsHiking(), false);
      registry.registerAll(urlLayerIds, OldOldOpenDataLayers.franceHiking(), false);
    } else if (routeType == 'cycling') {
      registry.registerAll(urlLayerIds, OldOldOpenDataLayers.flandersCycling(), false);
      registry.registerAll(urlLayerIds, OldOldOpenDataLayers.netherlandsCycling(), false);
    }
  }

  static flandersHiking(): OldOldMapLayer[] {
    return [this.flandersHikingBitmap(), this.flandersHikingVector()];
  }

  private static flandersHikingBitmap(): OldOldMapLayer {
    return OldOldOpendataBitmapTileLayer.build(
      'hiking',
      'flanders-hiking',
      this.openDataFlanders,
      'flanders/hiking'
    );
  }

  private static flandersHikingVector(): OldOldMapLayer {
    return OldOldOpendataVectorTileLayer.build(
      'hiking',
      'flanders-hiking',
      this.openDataFlanders,
      'flanders/hiking'
    );
  }

  static flandersCycling(): OldOldMapLayer[] {
    return [this.flandersCyclingBitmap(), this.flandersCyclingVector()];
  }

  private static flandersCyclingBitmap(): OldOldMapLayer {
    return OldOldOpendataBitmapTileLayer.build(
      'cycling',
      'flanders-cycling',
      this.openDataFlanders,
      'flanders/cycling'
    );
  }

  private static flandersCyclingVector(): OldOldMapLayer {
    return OldOldOpendataVectorTileLayer.build(
      'cycling',
      'flanders-cycling',
      this.openDataFlanders,
      'flanders/cycling'
    );
  }

  static netherlandsHiking(): OldOldMapLayer[] {
    return [this.netherlandsHikingBitmap(), this.netherlandsHikingVector()];
  }

  private static netherlandsHikingBitmap(): OldOldMapLayer {
    return OldOldOpendataBitmapTileLayer.build(
      'hiking',
      'netherlands-hiking',
      this.openDataNetherlands,
      'netherlands/hiking'
    );
  }

  private static netherlandsHikingVector(): OldOldMapLayer {
    return OldOldOpendataVectorTileLayer.build(
      'hiking',
      'netherlands-hiking',
      this.openDataNetherlands,
      'netherlands/hiking'
    );
  }

  static netherlandsCycling(): OldOldMapLayer[] {
    return [this.netherlandsCyclingBitmap(), this.netherlandsCyclingVector()];
  }

  private static netherlandsCyclingBitmap(): OldOldMapLayer {
    return OldOldOpendataBitmapTileLayer.build(
      'cycling',
      'netherlands-cycling',
      this.openDataNetherlands,
      'netherlands/cycling'
    );
  }

  private static netherlandsCyclingVector(): OldOldMapLayer {
    return OldOldOpendataVectorTileLayer.build(
      'cycling',
      'netherlands-cycling',
      this.openDataNetherlands,
      'netherlands/cycling'
    );
  }

  static franceHiking(): OldOldMapLayer[] {
    return [this.franceHikingBitmap(), this.franceHikingVector()];
  }

  private static franceHikingBitmap(): OldOldMapLayer {
    return OldOldOpendataBitmapTileLayer.build(
      'hiking',
      'france-hiking',
      this.openDataFrance,
      'france/hiking'
    );
  }

  private static franceHikingVector(): OldOldMapLayer {
    return OldOldOpendataVectorTileLayer.build(
      'hiking',
      'france-hiking',
      this.openDataFrance,
      'france/hiking'
    );
  }
}
