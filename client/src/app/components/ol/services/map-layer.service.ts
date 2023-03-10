import { Injectable } from '@angular/core';
import { RawNode } from '@api/common/data/raw/raw-node';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { NetworkMapNode } from '@api/common/network/network-map-node';
import { NodeMapInfo } from '@api/common/node-map-info';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteMap } from '@api/common/route/route-map';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { NetworkType } from '@api/custom/network-type';
import { PoiDetail } from '@app/kpn/api/common/poi-detail';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import VectorTileLayer from 'ol/layer/VectorTile';
import { I18nService } from '../../../i18n/i18n.service';
import { BrowserStorageService } from '../../../services/browser-storage.service';
import { MapLayerState } from '../domain/map-layer-state';
import { MapLayerStates } from '../domain/map-layer-states';
import { BackgroundLayer } from '../layers/background-layer';
import { GpxLayer } from '../layers/gpx-layer';
import { LocationBoundaryLayer } from '../layers/location-boundary-layer';
import { MainMapLayer } from '../layers/main-map-layer';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { NetworkBitmapTileLayer } from '../layers/network-bitmap-tile-layer';
import { NetworkMarkerLayer } from '../layers/network-marker-layer';
import { NetworkNodesMarkerLayer } from '../layers/network-nodes-marker-layer';
import { NetworkNodesTileLayer } from '../layers/network-nodes-tile-layer';
import { NetworkVectorTileLayer } from '../layers/network-vector-tile-layer';
import { NodeMarkerLayer } from '../layers/node-marker-layer';
import { NodeMovedLayer } from '../layers/node-moved-layer';
import { OsmLayer } from '../layers/osm-layer';
import { PoiAreasLayer } from '../layers/poi-areas-layer';
import { PoiMarkerLayer } from '../layers/poi-marker-layer';
import { PoiTileLayer } from '../layers/poi-tile-layer';
import { RouteChangeLayers } from '../layers/route-change-layers';
import { RouteLayers } from '../layers/route-layers';
import { RouteNodesLayer } from '../layers/route-nodes-layer';
import { TileDebug256Layer } from '../layers/tile-debug-256-layer';
import { TileDebug512Layer } from '../layers/tile-debug-512-layer';
import { MapMode } from './map-mode';
import { MapService } from './map.service';
import { FrisoLayer } from '@app/components/ol/layers/friso-layer';

@Injectable()
export class MapLayerService {
  private mapLayerStateKey = 'map-layer-state';

  constructor(
    private i18nService: I18nService,
    private mapService: MapService,
    private store: Store,
    private browserStorageService: BrowserStorageService
  ) {}

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

  osmLayer(): MapLayer {
    return new OsmLayer(this.i18nService).build();
  }

  backgroundLayer(mapElementId: string): MapLayer {
    return new BackgroundLayer(this.i18nService).build(mapElementId);
  }

  tile256NameLayer(): MapLayer {
    return new TileDebug256Layer(this.i18nService).build();
  }

  tile512NameLayer(): MapLayer {
    return new TileDebug512Layer(this.i18nService).build();
  }

  mainMapLayer(): MapLayer {
    return new MainMapLayer(
      this.mapService,
      this.i18nService,
      this.store
    ).build();
  }

  locationBoundaryLayer(geoJson: string): MapLayer {
    return new LocationBoundaryLayer(this.i18nService).build(geoJson);
  }

  nodeMarkerLayer(nodeMapInfo: NodeMapInfo): MapLayer {
    return new NodeMarkerLayer(this.i18nService).build(nodeMapInfo);
  }

  poiMarkerLayer(poiDetail: PoiDetail): MapLayer {
    return new PoiMarkerLayer(this.i18nService).build(poiDetail);
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
    const layer = NetworkVectorTileLayer.build(networkType);
    const layerName = this.i18nService.translation(
      '@@map.layer.' + networkType
    );
    layer.layer.set('name', layerName);
    return layer;
  }

  networkBitmapTileLayer(networkType: NetworkType, mapMode: MapMode): MapLayer {
    const layer = NetworkBitmapTileLayer.build(networkType, mapMode);
    const layerName = this.i18nService.translation(
      '@@map.layer.' + networkType
    );
    layer.layer.set('name', layerName);
    return layer;
  }

  routeNodeLayer(nodes: RawNode[]): MapLayer {
    return new RouteNodesLayer(this.i18nService).build(nodes);
  }

  routeChangeLayers(geometryDiff: GeometryDiff): List<MapLayer> {
    return new RouteChangeLayers(this.i18nService).build(geometryDiff);
  }

  routeLayers(routeMap: RouteMap): List<MapLayer> {
    return new RouteLayers(this.i18nService, routeMap).build();
  }

  networkMarkerLayer(networks: SubsetMapNetwork[]): MapLayer {
    return new NetworkMarkerLayer(this.i18nService).build(networks);
  }

  networkNodesMarkerLayer(nodes: NetworkMapNode[]): MapLayer {
    return new NetworkNodesMarkerLayer(this.i18nService).build(nodes);
  }

  networkNodesTileLayer(
    networkType: NetworkType,
    nodeIds: number[],
    routeIds: number[]
  ): MapLayer {
    const layer = NetworkNodesTileLayer.build(networkType, nodeIds, routeIds);
    const layerName = this.i18nService.translation(
      '@@map.layer.' + networkType
    );
    layer.layer.set('name', layerName);
    return layer;
  }

  poiTileLayer(): VectorTileLayer {
    return new PoiTileLayer().build();
  }

  gpxLayer(): MapLayer {
    return new GpxLayer(this.i18nService).build();
  }

  poiAreasLayer(geoJson: string): MapLayer {
    return new PoiAreasLayer(this.i18nService).build(geoJson);
  }

  frisoLayer(name: string, filename: string): MapLayer {
    return new FrisoLayer(name, `${name}.geojson`).build();
  }
}
