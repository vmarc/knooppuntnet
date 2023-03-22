import { Injectable } from '@angular/core';
import { RawNode } from '@api/common/data/raw/raw-node';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { NetworkMapNode } from '@api/common/network/network-map-node';
import { NodeMapInfo } from '@api/common/node-map-info';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteMap } from '@api/common/route/route-map';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { NetworkType } from '@api/custom/network-type';
import { FrisoLayer } from '@app/components/ol/layers/friso-layer';
import { MapLayerDefinition } from '@app/components/ol/services/map-layer-definition';
import { I18nService } from '@app/i18n/i18n.service';
import { PoiDetail } from '@app/kpn/api/common/poi-detail';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import VectorTileLayer from 'ol/layer/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { MapLayerState } from '../domain/map-layer-state';
import { MapLayerStates } from '../domain/map-layer-states';
import { BackgroundLayer } from '../layers/background-layer';
import { LocationBoundaryLayer } from '../layers/location-boundary-layer';
import { MainMapLayer } from '../layers/main-map-layer';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { NetworkBitmapTileLayer } from '../layers/network-bitmap-tile-layer';
import { NetworkMarkerLayer } from '../layers/network-marker-layer';
import { NetworkNodesMarkerLayer } from '../layers/network-nodes-marker-layer';
import { NetworkNodesVectorTileLayer } from '../layers/network-nodes-vector-tile-layer';
import { NetworkVectorTileLayer } from '../layers/network-vector-tile-layer';
import { NodeMarkerLayer } from '../layers/node-marker-layer';
import { NodeMovedLayer } from '../layers/node-moved-layer';
import { PoiAreasLayer } from '../layers/poi-areas-layer';
import { PoiMarkerLayer } from '../layers/poi-marker-layer';
import { PoiTileLayer } from '../layers/poi-tile-layer';
import { RouteChangeLayers } from '../layers/route-change-layers';
import { RouteLayers } from '../layers/route-layers';
import { RouteNodesLayer } from '../layers/route-nodes-layer';
import { TileDebug256Layer } from '../layers/tile-debug-256-layer';
import { MapMode } from './map-mode';
import { MapService } from './map.service';

@Injectable()
export class MapLayerService {
  private mapLayerStateKey = 'map-layer-state';

  readonly mapLayerDefinitions: MapLayerDefinition[] = [
    { name: 'osm', translation: '@@map.layer.osm' },
    { name: 'background', translation: '@@map.layer.background' },
    { name: 'cycling', translation: '@@network-type.cycling' },
    { name: 'hiking', translation: '@@network-type.hiking' },
    { name: 'horse-riding', translation: '@@network-type.horse-riding' },
    { name: 'motorboat', translation: '@@network-type.motorboat' },
    { name: 'canoe', translation: '@@network-type.canoe' },
    { name: 'inline-skating', translation: '@@network-type.inline-skating' },
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

  restoreMapLayerStates(mapLayers: MapLayers): void {
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

  storeMapLayerStates(mapLayers: MapLayers): void {
    const layerStates: MapLayerState[] = mapLayers.layers
      .toArray()
      .map((mapLayer) => {
        const mapLayerState: MapLayerState = {
          layerName: mapLayer.name,
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

  backgroundLayer(mapElementId: string): MapLayer {
    return new BackgroundLayer().build(mapElementId);
  }

  tile256NameLayer(): MapLayer {
    return new TileDebug256Layer().build();
  }

  mainMapVectorLayer(
    networkType: NetworkType,
    styleFunction: StyleFunction
  ): MapLayer {
    return new MainMapLayer(this.mapService, this.store).buildVectorLayer(
      networkType,
      styleFunction
    );
  }

  mainMapBitmapLayer(networkType: NetworkType): MapLayer {
    return new MainMapLayer(this.mapService, this.store).buildBitmapLayer(
      networkType
    );
  }

  locationBoundaryLayer(geoJson: string): MapLayer {
    return new LocationBoundaryLayer().build(geoJson);
  }

  nodeMarkerLayer(nodeMapInfo: NodeMapInfo): MapLayer {
    return new NodeMarkerLayer().build(nodeMapInfo);
  }

  poiMarkerLayer(poiDetail: PoiDetail): MapLayer {
    return new PoiMarkerLayer().build(poiDetail);
  }

  nodeMovedLayer(nodeMoved: NodeMoved): MapLayer {
    return NodeMovedLayer.build(nodeMoved);
  }

  networkLayers(networkTypes: NetworkType[]): MapLayer[] {
    return networkTypes.map((networkType) =>
      this.networkVectorTileLayer(networkType)
    );
  }

  networkVectorTileLayer(networkType: NetworkType): MapLayer {
    throw new Error('add style function');
    return NetworkVectorTileLayer.build(networkType, null);
  }

  networkBitmapTileLayer(networkType: NetworkType, mapMode: MapMode): MapLayer {
    return NetworkBitmapTileLayer.build(networkType, mapMode);
  }

  routeNodeLayer(nodes: RawNode[]): MapLayer {
    return new RouteNodesLayer().build(nodes);
  }

  routeChangeLayers(geometryDiff: GeometryDiff): List<MapLayer> {
    return new RouteChangeLayers(this.i18nService).build(geometryDiff);
  }

  routeLayers(routeMap: RouteMap): List<MapLayer> {
    return new RouteLayers(this.i18nService, routeMap).build();
  }

  networkMarkerLayer(networks: SubsetMapNetwork[]): MapLayer {
    return new NetworkMarkerLayer().build(networks);
  }

  networkNodesMarkerLayer(nodes: NetworkMapNode[]): MapLayer {
    return new NetworkNodesMarkerLayer().build(nodes);
  }

  networkNodesTileLayer(
    networkType: NetworkType,
    nodeIds: number[],
    routeIds: number[]
  ): MapLayer {
    return NetworkNodesVectorTileLayer.build(networkType, nodeIds, routeIds);
  }

  poiTileLayer(): VectorTileLayer {
    return new PoiTileLayer().build();
  }

  poiAreasLayer(geoJson: string): MapLayer {
    return new PoiAreasLayer().build(geoJson);
  }

  frisoLayer(name: string): MapLayer {
    return new FrisoLayer(name, `${name}.geojson`).build();
  }
}
