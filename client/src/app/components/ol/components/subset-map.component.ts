import {OnInit} from "@angular/core";
import {AfterViewInit, Component, EventEmitter, Input, Output} from "@angular/core";
import {List} from "immutable";
import {MapBrowserEvent} from "ol";
import {FeatureLike} from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import PointerInteraction from "ol/interaction/Pointer";
import Map from "ol/Map";
import View from "ol/View";
import {Bounds} from "../../../kpn/api/common/bounds";
import {SubsetMapNetwork} from "../../../kpn/api/common/subset/subset-map-network";
import {Util} from "../../shared/util";
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

  @Input() bounds: Bounds;
  @Input() networks: List<SubsetMapNetwork>;
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
    view.fit(Util.toExtent(this.bounds, 0.1));

    this.map.addInteraction(this.buildInteraction());
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
