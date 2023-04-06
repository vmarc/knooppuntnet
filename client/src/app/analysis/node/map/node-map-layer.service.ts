import { Injectable } from '@angular/core';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { NodeMarkerLayer } from '@app/components/ol/layers/node-marker-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { NodeMapStyle } from '@app/components/ol/style/node-map-style';
import { NodeMapInfo } from '@api/common/node-map-info';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { NetworkType } from '@api/custom/network-type';

@Injectable()
export class NodeMapLayerService {
  private _layers$ = new BehaviorSubject<MapLayer[]>([]);
  public layers$: Observable<MapLayer[]> = this._layers$.asObservable();

  buildLayers(
    nodeMapInfo: NodeMapInfo,
    defaultNetworkType: NetworkType
  ): MapLayerState[] {
    const layers: MapLayer[] = [];
    const layerStates: MapLayerState[] = [];

    layers.push(BackgroundLayer.build());
    layerStates.push({ layerName: BackgroundLayer.id, visible: true });

    layers.push(OsmLayer.build());
    layerStates.push({ layerName: 'osm', visible: false });

    nodeMapInfo.networkTypes.forEach((networkType) => {
      const visible =
        nodeMapInfo.networkTypes.length > 1
          ? networkType == defaultNetworkType
          : true;
      layers.push(
        NetworkVectorTileLayer.build(
          networkType,
          new NodeMapStyle().styleFunction()
        )
      );
      layerStates.push({ layerName: networkType, visible });
    });

    layers.push(new NodeMarkerLayer().build(nodeMapInfo));
    layerStates.push({
      layerName: 'node-marker-layer',
      visible: true,
    });

    layers.push(new TileDebug256Layer().build());
    layerStates.push({ layerName: 'debug-256', visible: false });

    this._layers$.next(layers);
    this.updateLayerVisibility(layerStates);
    return layerStates;
  }

  updateLayerVisibility(layerStates: MapLayerState[]): void {
    this._layers$.value.forEach((mapLayer) => {
      const mapLayerState = layerStates.find(
        (layerState) => layerState.layerName === mapLayer.name
      );
      const visible = mapLayerState && mapLayerState.visible;
      if (
        visible &&
        mapLayer.id.includes('vector') &&
        mapLayer.layer.getVisible()
      ) {
        mapLayer.layer.changed();
      }
      mapLayer.layer.setVisible(visible);
    });
  }
}
