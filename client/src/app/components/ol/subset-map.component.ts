import {AfterViewInit, Component, EventEmitter, Input, Output} from "@angular/core";
import {List} from "immutable";
import {MapBrowserEvent} from "ol";
import {Coordinate} from "ol/coordinate";
import {Extent} from "ol/extent";
import {FeatureLike} from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import PointerInteraction from "ol/interaction/Pointer";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import VectorSource from "ol/source/Vector";
import View from "ol/View";
import {NetworkAttributes} from "../../kpn/api/common/network/network-attributes";
import {Util} from "../shared/util";
import {Marker} from "./domain/marker";
import {ZoomLevel} from "./domain/zoom-level";
import {MapControls} from "./layers/map-controls";
import {MapLayerService} from "./map-layer.service";

@Component({
  selector: "kpn-subset-map",
  template: `
    <div id="subset-map" class="map"></div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 210px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
    }
  `]
})
export class SubsetMapComponent implements AfterViewInit {

  @Input() networks: List<NetworkAttributes>;
  @Output() networkClicked = new EventEmitter<number>();

  map: Map;

  private readonly networkId = "network-id";
  private readonly layer = "layer";
  private readonly networkMarker = "network-marker";

  constructor(private mapLayerService: MapLayerService) {
  }

  ngAfterViewInit(): void {

    this.map = new Map({
      target: "subset-map",
      layers: [
        this.mapLayerService.osmLayer(),
        this.buildMarkerLayer()
      ],
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    const view = this.map.getView();
    view.fit(this.buildExtent());

    this.map.addInteraction(this.buildInteraction());
  }

  updateSize() {
    if (this.map != null) {
      this.map.updateSize();
    }
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

  private buildMarkerLayer() {

    const markers = this.networks.map(network => {
      const coordinate = Util.toCoordinate(network.center.latitude, network.center.longitude);
      const marker = Marker.create("blue", coordinate);
      marker.set(this.networkId, network.id.toString());
      marker.set(this.layer, this.networkMarker);
      return marker;
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    markers.forEach(marker => source.addFeature(marker));
    return layer;
  }

  private buildInteraction(): Interaction {
    return new PointerInteraction({
      handleDownEvent: (evt: MapBrowserEvent) => {
        const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
        if (features) {
          const index = features.findIndex(feature => this.networkMarker === feature.get(this.layer));
          if (index >= 0) {
            const networkId = features[index].get(this.networkId);
            this.networkClicked.emit(+networkId);
            return true;
          }
        }
        return false;
      },
      handleMoveEvent: (evt: MapBrowserEvent) => {
        const features: FeatureLike[] = evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
        if (features) {
          const index = features.findIndex(feature => this.networkMarker === feature.get(this.layer));
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
