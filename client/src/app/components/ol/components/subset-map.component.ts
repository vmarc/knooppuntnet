import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Input } from '@angular/core';
import { Output } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { List } from 'immutable';
import { MapBrowserEvent } from 'ol';
import { FeatureLike } from 'ol/Feature';
import Interaction from 'ol/interaction/Interaction';
import MapBrowserEventType from 'ol/MapBrowserEventType';
import View from 'ol/View';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayers } from '../layers/map-layers';
import { NetworkMarkerLayer } from '../layers/network-marker-layer';
import { MapLayerService } from '../services/map-layer.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

@Component({
  selector: 'kpn-subset-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="subset-map" class="kpn-map">
      <kpn-old-layer-switcher [mapLayers]="layers" />
      <kpn-map-link-menu [openLayersMap]="map" />
    </div>
  `,
})
export class SubsetMapComponent implements AfterViewInit, OnDestroy {
  @Input() bounds: Bounds;
  @Input() networks: SubsetMapNetwork[];
  @Output() networkClicked = new EventEmitter<number>();

  protected layers: MapLayers;
  protected map: OpenLayersMap;

  private readonly mapId = 'subset-map';

  constructor(
    private newMapService: NewMapService,
    private mapLayerService: MapLayerService
  ) {}

  ngAfterViewInit(): void {
    this.layers = this.buildLayers();
    setTimeout(
      () => this.mapLayerService.restoreMapLayerStates(this.layers),
      0
    );
    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
      }),
    });

    const view = this.map.map.getView();
    view.fit(Util.toExtent(this.bounds, 0.1));

    this.map.map.addInteraction(this.buildInteraction());
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  private buildLayers(): MapLayers {
    return new MapLayers(
      List([
        BackgroundLayer.build(),
        new NetworkMarkerLayer().build(this.networks),
      ])
    );
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
        const networkId = features[index].get(NetworkMarkerLayer.networkId);
        this.networkClicked.emit(+networkId);
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
