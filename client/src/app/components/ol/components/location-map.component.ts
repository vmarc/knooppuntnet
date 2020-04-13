import {ChangeDetectionStrategy} from "@angular/core";
import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {List} from "immutable";
import {boundingExtent} from "ol/extent";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import View from "ol/View";
import {Bounds} from "../../../kpn/api/common/bounds";
import {Subscriptions} from "../../../util/Subscriptions";
import {PageService} from "../../shared/page.service";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayer} from "../layers/map-layer";
import {MapLayers} from "../layers/map-layers";
import {MapClickService} from "../services/map-click.service";
import {MapLayerService} from "../services/map-layer.service";
import {MapService} from "../services/map.service";

@Component({
  selector: "kpn-location-map",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="location-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class LocationMapComponent {

  @Input() bounds: Bounds;
  @Input() geoJson: string;

  layers: MapLayers;
  private map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService,
              private mapClickService: MapClickService,
              private mapLayerService: MapLayerService) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
    this.subscriptions.add(this.pageService.sidebarOpen.subscribe(state => {
      if (this.map) {
        setTimeout(() => {
          this.map.updateSize();
        }, 250);
      }
    }));
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  buildMap(): void {
    this.map = new Map({
      target: "location-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom
      })
    });

    const southWest = fromLonLat([this.bounds.minLon, this.bounds.minLat]);
    const northEast = fromLonLat([this.bounds.maxLon, this.bounds.maxLat]);
    this.map.getView().fit(boundingExtent([southWest, northEast]));
    this.layers.applyMap(this.map);

    this.mapClickService.installOn(this.map);
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
    this.subscriptions.unsubscribe();
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    mapLayers = mapLayers.push(this.mapLayerService.mainMapLayer());
    mapLayers = mapLayers.push(this.mapLayerService.locationBoundaryLayer(this.geoJson));
    return new MapLayers(mapLayers);
  }
}
