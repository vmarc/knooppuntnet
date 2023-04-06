import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { UniqueId } from '../../../kpn/common/unique-id';
import { Subscriptions } from '../../../util/Subscriptions';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { NodeMovedLayer } from '@app/components/ol/layers/node-moved-layer';

@Component({
  selector: 'kpn-node-moved-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-old-layer-switcher [mapLayers]="layers" />
    </div>
  `,
})
export class NodeMovedMapComponent implements AfterViewInit, OnDestroy {
  @Input() nodeMoved: NodeMoved;
  private readonly subscriptions = new Subscriptions();

  mapId = UniqueId.get();
  layers: MapLayers;
  private map: Map;

  constructor(private mapLayerService: MapLayerService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    if (this.map) {
      this.map.setTarget(null);
    }
  }

  private buildMap(): void {
    this.layers = this.buildLayers();
    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18,
      }),
    });
    const center = Util.latLonToCoordinate(this.nodeMoved.after);
    this.map.getView().setCenter(center);

    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );
  }

  private updateSize(): void {
    if (this.map) {
      setTimeout(() => {
        this.map.updateSize();
        this.layers.updateSize();
      }, 0);
    }
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(NodeMovedLayer.build(this.nodeMoved));

    return new MapLayers(mapLayers);
  }
}
