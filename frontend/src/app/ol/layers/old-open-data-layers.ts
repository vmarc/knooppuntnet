import { RouteType } from '@api/common/route-type';
import { OldMapLayer } from './old-map-layer';
import { OldMapLayerRegistry } from './old-map-layer-registry';
import { OldOpendataBitmapTileLayer } from './old-opendata-bitmap-tile-layer';
import { OldOpendataVectorTileLayer } from './old-opendata-vector-tile-layer';

export class OldOpenDataLayers {
  private static readonly openDataFlanders = $localize`:@@map.layer.open-data-flanders:Toerisme Vlaanderen`;
  private static readonly openDataNetherlands = $localize`:@@map.layer.open-data-netherlands:NL routedatabank`;
  private static readonly openDataFrance = $localize`:@@map.layer.open-data-france:Parc du Vercors`;

  static register(
    registry: OldMapLayerRegistry,
    routeType: RouteType,
    urlLayerIds: string[]
  ): void {
    if (routeType == 'hiking') {
      registry.registerAll(urlLayerIds, OldOpenDataLayers.flandersHiking(), false);
      registry.registerAll(urlLayerIds, OldOpenDataLayers.netherlandsHiking(), false);
      registry.registerAll(urlLayerIds, OldOpenDataLayers.franceHiking(), false);
    } else if (routeType == 'cycling') {
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
      this.openDataFlanders,
      'flanders/hiking'
    );
  }

  private static flandersHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'flanders-hiking',
      this.openDataFlanders,
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
      this.openDataFlanders,
      'flanders/cycling'
    );
  }

  private static flandersCyclingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'cycling',
      'flanders-cycling',
      this.openDataFlanders,
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
      this.openDataNetherlands,
      'netherlands/hiking'
    );
  }

  private static netherlandsHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'netherlands-hiking',
      this.openDataNetherlands,
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
      this.openDataNetherlands,
      'netherlands/cycling'
    );
  }

  private static netherlandsCyclingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'cycling',
      'netherlands-cycling',
      this.openDataNetherlands,
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
      this.openDataFrance,
      'france/hiking'
    );
  }

  private static franceHikingVector(): OldMapLayer {
    return OldOpendataVectorTileLayer.build(
      'hiking',
      'france-hiking',
      this.openDataFrance,
      'france/hiking'
    );
  }
}
