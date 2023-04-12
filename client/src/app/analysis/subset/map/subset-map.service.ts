import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { SubsetMapNetwork } from '@api/common/subset';
import { ZoomLevel } from '@app/components/ol/domain';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { BackgroundLayer } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { NetworkMarkerLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { Store } from '@ngrx/store';
import { MapBrowserEvent } from 'ol';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import View from 'ol/View';
import { actionSubsetMapPageNetworkClicked } from '../store/subset.actions';

@Injectable()
export class SubsetMapService extends OpenlayersMapService {
  constructor(private store: Store) {
    super();
  }

  init(networks: SubsetMapNetwork[], bounds: Bounds): void {
    this.registerLayers(networks);
    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.maxZoom,
        }),
      })
    );

    this.map.getView().fit(Util.toExtent(bounds, 0.1));

    this.map.addInteraction(this.buildInteraction());

    this.finalizeSetup();
  }

  private registerLayers(networks: SubsetMapNetwork[]): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.register([], new NetworkMarkerLayer().build(networks), true);
    this.register(registry);
  }

  private buildInteraction(): Interaction {
    return new Interaction({
      handleEvent: (event: MapBrowserEvent<MouseEvent>) => {
        if (MapBrowserEventType.SINGLECLICK === event.type) {
          return this.handleSingleClickEvent(event);
        }
        if (MapBrowserEventType.POINTERMOVE === event.type) {
          return this.handleMoveEvent(event);
        }
        return true; // propagate event
      },
    });
  }

  private handleSingleClickEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features) {
      const index = features.findIndex(
        (feature) =>
          NetworkMarkerLayer.networkMarker ===
          feature.get(NetworkMarkerLayer.layer)
      );
      if (index >= 0) {
        const networkId = +features[index].get(NetworkMarkerLayer.networkId);
        this.store.dispatch(actionSubsetMapPageNetworkClicked({ networkId }));
        return false; // do not propagate event
      }
    }
    return true; // propagate event
  }

  private handleMoveEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features) {
      const index = features.findIndex(
        (feature) =>
          NetworkMarkerLayer.networkMarker ===
          feature.get(NetworkMarkerLayer.layer)
      );
      evt.map.getTargetElement().style.cursor =
        index >= 0 ? 'pointer' : 'default';
    }
    return true; // propagate event
  }
}
