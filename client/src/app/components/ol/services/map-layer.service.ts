import { Injectable } from '@angular/core';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteMap } from '@api/common/route/route-map';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerDefinition } from '@app/components/ol/services/map-layer-definition';
import { I18nService } from '@app/i18n/i18n.service';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { MapLayerState } from '../domain/map-layer-state';
import { MapLayerStates } from '../domain/map-layer-states';
import { MapLayer } from '../layers/map-layer';
import { OldMapLayers } from '../layers/old-map-layers';
import { NetworkBitmapTileLayer } from '../layers/network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from '../layers/network-vector-tile-layer';
import { RouteChangeLayers } from '../layers/route-change-layers';
import { RouteLayers } from '../layers/route-layers';
import { MapMode } from './map-mode';
import { MapService } from './map.service';
import { NodeMapStyle } from '@app/components/ol/style/node-map-style';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';

@Injectable()
export class MapLayerService {
  private mapLayerStateKey = 'map-layer-state';

  readonly mapLayerDefinitions: MapLayerDefinition[] = [
    { name: BackgroundLayer.id, translation: '@@map.layer.background' },
    { name: OsmLayer.id, translation: '@@map.layer.osm' },
    { name: 'cycling', translation: '@@network-type.cycling' },
    { name: 'hiking', translation: '@@network-type.hiking' },
    { name: 'horse-riding', translation: '@@network-type.horse-riding' },
    { name: 'motorboat', translation: '@@network-type.motorboat' },
    { name: 'canoe', translation: '@@network-type.canoe' },
    { name: 'inline-skating', translation: '@@network-type.inline-skating' },

    {
      name: 'network-nodes-cycling-layer',
      translation: '@@network-type.cycling',
    },
    {
      name: 'network-nodes-hiking-layer',
      translation: '@@network-type.hiking',
    },
    {
      name: 'network-nodes-horse-riding-layer',
      translation: '@@network-type.horse-riding',
    },
    {
      name: 'network-nodes-motorboat-layer',
      translation: '@@network-type.motorboat',
    },
    { name: 'network-nodes-canoe-layer', translation: '@@network-type.canoe' },
    {
      name: 'network-nodes-inline-skating-layer',
      translation: '@@network-type.inline-skating',
    },

    {
      name: 'netherlands-hiking',
      translation: '@@map.layer.netherlands-hiking', //$localize`:@@map.layer.opendata.netherlands:Routedatabank Nederland`
    },
    { name: 'flanders-hiking', translation: '@@map.layer.flanders-hiking' }, // $localize`:@@map.layer.opendata.flanders:Toerisme Vlaanderen`
    { name: 'debug-512', translation: '@@map.layer.tile-512-names' },
    { name: 'debug-256', translation: '@@map.layer.tile-256-names' },
    { name: 'location-boundary', translation: '@@map.layer.boundary' },
    { name: 'node-marker-layer', translation: '@@map.layer.node' },
    { name: 'poi-marker-layer', translation: '@@map.layer.poi' },
    { name: 'route-nodes-layer', translation: '@@map.layer.nodes' },
    { name: 'poi-areas-layer', translation: '@@map.layer.poi-areas' },
    { name: 'network-node-markers-layer', translation: '@@map.layer.nodes' },
    { name: 'network-marker-layer', translation: '@@map.layer.networks' },
    // { name: '', translation: '' },
  ];

  constructor(
    private i18nService: I18nService,
    private mapService: MapService,
    private store: Store,
    private browserStorageService: BrowserStorageService
  ) {}

  translation(layerName: string): string {
    const layerDefinition = this.mapLayerDefinitions.find(
      (definition) => definition.name === layerName
    );
    if (!!layerDefinition) {
      const translation = this.i18nService.translation(
        layerDefinition.translation
      );
      if (!!translation) {
        return translation;
      }
    }
    return layerName;
  }

  restoreMapLayerStates(mapLayers: OldMapLayers): void {
    const mapLayerStatesString = this.browserStorageService.get(
      this.mapLayerStateKey
    );
    if (mapLayerStatesString == null) {
      this.storeMapLayerStates(mapLayers);
    } else {
      const mapLayerStates: MapLayerStates = JSON.parse(mapLayerStatesString);
      mapLayers.layers.forEach((mapLayer) => {
        mapLayerStates.layerStates.forEach((mapLayerState) => {
          if (mapLayerState.layerName === mapLayer.name) {
            mapLayer.layer.setVisible(mapLayerState.visible);
          }
        });
      });
    }
  }

  storeMapLayerStates(mapLayers: OldMapLayers): void {
    const layerStates: MapLayerState[] = mapLayers.layers
      .toArray()
      .map((mapLayer) => {
        const mapLayerState: MapLayerState = {
          layerName: mapLayer.name,
          enabled: true,
          visible: mapLayer.layer.getVisible(),
        };
        return mapLayerState;
      });

    const mapLayerStates: MapLayerStates = { layerStates };

    this.browserStorageService.set(
      this.mapLayerStateKey,
      JSON.stringify(mapLayerStates)
    );
  }

  networkLayers(networkTypes: NetworkType[]): MapLayer[] {
    return networkTypes.map((networkType) =>
      this.networkVectorTileLayer(networkType)
    );
  }

  networkVectorTileLayer(networkType: NetworkType): MapLayer {
    return NetworkVectorTileLayer.build(
      networkType,
      new NodeMapStyle().styleFunction()
    );
  }

  networkBitmapTileLayer(networkType: NetworkType, mapMode: MapMode): MapLayer {
    return NetworkBitmapTileLayer.build(networkType, mapMode);
  }

  routeChangeLayers(geometryDiff: GeometryDiff): List<MapLayer> {
    return new RouteChangeLayers(this.i18nService).build(geometryDiff);
  }

  routeLayers(routeMap: RouteMap): List<MapLayer> {
    return new RouteLayers(this.i18nService, routeMap).build();
  }
}
