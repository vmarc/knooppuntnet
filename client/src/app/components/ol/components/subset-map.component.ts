import {OnInit} from "@angular/core";
import {AfterViewInit, Component, EventEmitter, Input, Output} from "@angular/core";
import {List} from "immutable";
import {MapBrowserEvent} from "ol";
import {Coordinate} from "ol/coordinate";
import {Extent} from "ol/extent";
import {FeatureLike} from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import PointerInteraction from "ol/interaction/Pointer";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import View from "ol/View";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayers} from "../layers/map-layers";
import {NetworkMarkerLayer} from "../layers/network-marker-layer";
import {MapLayerService} from "../services/map-layer.service";

@Component({
  selector: "kpn-subset-map",
  template: `
    <div id="subset-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class SubsetMapComponent implements OnInit, AfterViewInit {

  @Input() networks: List<NetworkAttributes>;
  @Output() networkClicked = new EventEmitter<number>();

  layers: MapLayers;
  private map: Map;

  constructor(private mapLayerService: MapLayerService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {

    this.map = new Map({
      target: "subset-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.layers.applyMap(this.map);
    const view = this.map.getView();
    view.fit(this.buildExtent());

    this.map.addInteraction(this.buildInteraction());
  }

  buildExtent(): Extent {
    const latitudes = this.networks.map(network => +network.center.latitude);
    const longitudes = this.networks.map(network => +network.center.longitude);
    const latMin = latitudes.min();
    const lonMin = longitudes.min();
    const latMax = latitudes.max();
    const lonMax = longitudes.max();
    const a: Coordinate = fromLonLat([lonMin, latMin]);
    const b: Coordinate = fromLonLat([lonMax, latMax]);
    return [a[0], a[1], b[0], b[1]];
  }

  private buildLayers(): MapLayers {
    return new MapLayers(
      List([
        this.mapLayerService.osmLayer(),
        this.mapLayerService.networkMarkerLayer(this.networks)
      ])
    );
  }

  private buildInteraction(): Interaction {
    return new PointerInteraction({
      handleDownEvent: (evt: MapBrowserEvent) => {
        const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
        if (features) {
          const index = features.findIndex(feature => NetworkMarkerLayer.networkMarker === feature.get(NetworkMarkerLayer.layer));
          if (index >= 0) {
            const networkId = features[index].get(NetworkMarkerLayer.networkId);
            this.networkClicked.emit(+networkId);
            return true;
          }
        }
        return false;
      },
      handleMoveEvent: (evt: MapBrowserEvent) => {
        const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
        if (features) {
          const index = features.findIndex(feature => NetworkMarkerLayer.networkMarker === feature.get(NetworkMarkerLayer.layer));
          evt.map.getTargetElement().style.cursor = index >= 0 ? "pointer" : "default";
        }
        return false;
      },
      handleDragEvent: (evt: MapBrowserEvent) => {
        return false;
      },
      handleUpEvent: (evt: MapBrowserEvent) => {
        return false;
      }
    });
  }

}
