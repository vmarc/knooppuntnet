import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { FrisoLayer } from '@app/components/ol/layers/friso-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers/network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { Util } from '@app/components/shared/util';
import { FrisoNode } from '@app/friso/friso/friso-node';
import { actionFrisoNodeClicked } from '@app/friso/store/friso.actions';
import { Store } from '@ngrx/store';
import { MapBrowserEvent } from 'ol';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import Map from 'ol/Map';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import View from 'ol/View';
import { of } from 'rxjs';
import { Observable } from 'rxjs';

@Injectable()
export class FrisoMapService extends OpenlayersMapService {
  private readonly bounds: Bounds = {
    minLat: 50.92176250622567,
    minLon: 3.5257314989690713,
    maxLat: 53.28321294207922,
    maxLon: 6.729255052272727,
  };

  constructor(private store: Store) {
    super();
  }

  init(mode: string): void {
    this.registerLayers(mode);

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

    this.map.getView().fit(Util.toExtent(this.bounds, 0.1));
    this.map.addInteraction(this.buildInteraction());
    this.finalizeSetup();
  }

  updateMode(mode: string): void {
    this.mapLayers.forEach((layer) => {
      if (layer.name.startsWith('friso-')) {
        const visible = layer.id === mode;
        const changed = layer.layer.getVisible() !== visible;
        layer.layer.setVisible(visible);
        if (changed) {
          layer.layer.changed();
        }
      }
    });
  }

  private registerLayers(mode: string): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);

    const parameters$: Observable<MainMapStyleParameters> = of(
      new MainMapStyleParameters('analysis', true, null, null, null, null)
    );
    const mainMapStyle = new MainMapStyle(parameters$);

    const networkLayers = [
      NetworkVectorTileLayer.oldBuild(
        NetworkType.hiking,
        mainMapStyle.styleFunction()
      ),
      NetworkBitmapTileLayer.build(NetworkType.hiking, 'analysis'),
    ];
    registry.registerAll([], networkLayers, true);

    this.register(registry);

    this.frisoLayer(mode, 'rename');
    this.frisoLayer(mode, 'minor-rename');
    this.frisoLayer(mode, 'removed');
    this.frisoLayer(mode, 'added');
    this.frisoLayer(mode, 'no-change');
    this.frisoLayer(mode, 'moved-short-distance');
    this.frisoLayer(mode, 'other');
    this.frisoLayer(mode, 'moved-long-distance');
  }

  private frisoLayer(mode: string, name: string): void {
    const layer = FrisoLayer.build(name, `${name}.geojson`);
    const visible = name === mode;
    layer.layer.setVisible(visible);
    this.mapLayers.push(layer);
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
        (feature) => !!feature.get('distance closest node')
      );
      if (index >= 0) {
        const name = features[index].get('rwn_ref');
        const distanceClosestNode = features[index].get(
          'distance closest node'
        );
        const node = new FrisoNode(name, Math.round(+distanceClosestNode));
        this.store.dispatch(actionFrisoNodeClicked({ node }));
        return false; // do not propagate event
      }
    }
    return true; // propagate event
  }

  private handleMoveEvent(evt: MapBrowserEvent<MouseEvent>): boolean {
    const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {
      hitTolerance: 10,
    });
    if (features && features.length > 0) {
      const index = features.findIndex((feature) => !!feature.get('rwn_ref'));
      evt.map.getTargetElement().style.cursor =
        index >= 0 ? 'pointer' : 'default';
    }
    return true; // propagate event
  }
}
