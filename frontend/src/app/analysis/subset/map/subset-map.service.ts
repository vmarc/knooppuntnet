import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Bounds } from '@api/common';
import { SubsetMapNetwork } from '@api/common/subset';
import { Util } from '@app/components/shared';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { MapLayerRegistry } from '@app/ol/layers';
import { OldBackgroundLayer } from '@app/ol/layers';
import { OldOsmLayer } from '@app/ol/layers';
import { NetworkMarkerLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { MapBrowserEvent } from 'ol';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { SubsetMapNetworkDialogComponent } from './components/subset-map-network-dialog.component';

@Injectable()
export class SubsetMapService extends OpenlayersMapService {
  private readonly dialog = inject(MatDialog);
  private networks: SubsetMapNetwork[] = [];

  init(
    networks: SubsetMapNetwork[],
    bounds: Bounds,
    mapPositionFromUrl: MapPosition,
    urlLayerIds: string[]
  ): void {
    this.networks = networks;
    this.registerLayers(networks, urlLayerIds);
    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.minZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    if (mapPositionFromUrl) {
      const center: Coordinate = [mapPositionFromUrl.x, mapPositionFromUrl.y];
      const zoom = mapPositionFromUrl.zoom;
      viewOptions = {
        ...viewOptions,
        center,
        zoom,
      };
    }
    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View(viewOptions),
      })
    );

    if (!mapPositionFromUrl) {
      this.map.getView().fit(Util.toExtent(bounds, 0.1));
    }

    this.map.addInteraction(this.buildInteraction());

    this.finalizeSetup(true);
  }

  private registerLayers(networks: SubsetMapNetwork[], urlLayerIds: string[]): void {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);
    registry.register(urlLayerIds, new NetworkMarkerLayer().build(networks), true);
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
        (feature) => NetworkMarkerLayer.networkMarker === feature.get(NetworkMarkerLayer.layer)
      );
      if (index >= 0) {
        const networkId = +features[index].get(NetworkMarkerLayer.networkId);
        const network = this.networks.find((n) => n.id === networkId);
        if (network) {
          this.dialog.open(SubsetMapNetworkDialogComponent, {
            data: network,
            autoFocus: false,
            maxWidth: 600,
          });
        }

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
        (feature) => NetworkMarkerLayer.networkMarker === feature.get(NetworkMarkerLayer.layer)
      );
      evt.map.getTargetElement().style.cursor = index >= 0 ? 'pointer' : 'default';
    }
    return true; // propagate event
  }
}
