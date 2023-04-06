import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit, Component, Input } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { List } from 'immutable';
import View from 'ol/View';
import { UniqueId } from '@app/kpn/common/unique-id';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { NodeMovedLayer } from '@app/components/ol/layers/node-moved-layer';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { NewMapService } from '@app/components/ol/services/new-map.service';

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

  protected mapId = UniqueId.get();
  protected layers: MapLayers;
  private map: OpenLayersMap;

  constructor(private newMapService: NewMapService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    this.map.destroy();
  }

  private buildMap(): void {
    this.layers = this.buildLayers();
    this.map = this.newMapService.build({
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
    this.map.map.getView().setCenter(center);
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(NodeMovedLayer.build(this.nodeMoved));
    return new MapLayers(mapLayers);
  }
}
