import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit, Component, Input} from "@angular/core";
import {List} from "immutable";
import Map from "ol/Map";
import View from "ol/View";
import {NodeMoved} from "../../../kpn/api/common/diff/node/node-moved";
import {UniqueId} from "../../../kpn/common/unique-id";
import {Util} from "../../shared/util";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayer} from "../layers/map-layer";
import {MapLayers} from "../layers/map-layers";
import {MapLayerService} from "../services/map-layer.service";

@Component({
  selector: "kpn-node-moved-map",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class NodeMovedMapComponent implements AfterViewInit {

  @Input() nodeMoved: NodeMoved;

  mapId = UniqueId.get();
  layers: MapLayers;
  private map: Map;

  constructor(private mapLayerService: MapLayerService) {
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  buildMap(): void {
    this.layers = this.buildLayers();
    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18
      })
    });
    this.layers.applyMap(this.map);
    const center = Util.latLonToCoordinate(this.nodeMoved.after);
    this.map.getView().setCenter(center);
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer2(this.mapId));
    mapLayers = mapLayers.push(this.mapLayerService.nodeMovedLayer(this.nodeMoved));
    return new MapLayers(mapLayers);
  }

}
