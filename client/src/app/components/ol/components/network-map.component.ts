import {ChangeDetectionStrategy} from "@angular/core";
import {OnDestroy} from "@angular/core";
import {OnInit} from "@angular/core";
import {AfterViewInit, Component, Input} from "@angular/core";
import {List} from "immutable";
import Map from "ol/Map";
import View from "ol/View";
import {NetworkMapPage} from "../../../kpn/api/common/network/network-map-page";
import {Subscriptions} from "../../../util/Subscriptions";
import {PageService} from "../../shared/page.service";
import {Util} from "../../shared/util";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayer} from "../layers/map-layer";
import {MapLayers} from "../layers/map-layers";
import {MapClickService} from "../services/map-click.service";
import {MapLayerService} from "../services/map-layer.service";
import {MapZoomService} from "../services/map-zoom.service";

@Component({
  selector: "kpn-network-map",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="network-nodes-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class NetworkMapComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input() page: NetworkMapPage;

  layers: MapLayers;
  private map: Map;

  private readonly subscriptions = new Subscriptions();

  constructor(private mapLayerService: MapLayerService,
              private mapClickService: MapClickService,
              private mapZoomService: MapZoomService,
              private pageService: PageService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
    this.subscriptions.add(
      this.pageService.sidebarOpen.subscribe(state => {
        if (this.map) {
          setTimeout(() => this.map.updateSize(), 250);
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.map.dispose();
    this.subscriptions.unsubscribe();
  }

  ngAfterViewInit(): void {

    this.map = new Map({
      target: "network-nodes-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.layers.applyMap(this.map);
    const view = this.map.getView();
    view.fit(Util.toExtent(this.page.bounds, 0.1));
    this.mapZoomService.install(view);
    this.mapClickService.installOn(this.map);
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    mapLayers = mapLayers.push(this.mapLayerService.networkNodesTileLayer(this.page.networkSummary.networkType, this.page.nodeIds, this.page.routeIds));
    mapLayers = mapLayers.push(this.mapLayerService.networkNodesMarkerLayer(this.page.nodes));
    // mapLayers = mapLayers.push(this.mapLayerService.tileNameLayer());
    return new MapLayers(mapLayers);
  }
}
