import {Injectable} from "@angular/core";
import {List} from "immutable";
import {Map as ImmutableMap} from "immutable";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import {ReplaySubject} from "rxjs";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {tap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {map} from "rxjs/operators";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {GpxLayer} from "../../../components/ol/layers/gpx-layer";
import {MapLayer} from "../../../components/ol/layers/map-layer";
import {MapLayerChange} from "../../../components/ol/layers/map-layer-change";
import {MapLayers} from "../../../components/ol/layers/map-layers";
import {MapLayerService} from "../../../components/ol/services/map-layer.service";
import {MapZoomService} from "../../../components/ol/services/map-zoom.service";
import {MapService} from "../../../components/ol/services/map.service";
import {PoiTileLayerService} from "../../../components/ol/services/poi-tile-layer.service";
import {MainMapStyle} from "../../../components/ol/style/main-map-style";
import {I18nService} from "../../../i18n/i18n.service";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerService} from "../../planner.service";

@Injectable()
export class PlannerLayerService {

  standardLayers: List<MapLayer>;
  mapLayers$: Observable<MapLayers>;
  gpxVectorLayer: VectorLayer;
  private _mapLayers$ = new ReplaySubject<MapLayers>();
  private networkLayerChange$: Observable<MapLayerChange>;
  private activeNetworkLayer: MapLayer = null;
  private osmLayer: MapLayer;
  private tileNameLayer: MapLayer;
  private poiLayer: MapLayer;
  private gpxLayer: MapLayer;
  private bitmapLayers: ImmutableMap<NetworkType, MapLayer>;
  private vectorLayers: ImmutableMap<NetworkType, MapLayer>;
  private allLayers: List<MapLayer>;

  constructor(private mapLayerService: MapLayerService,
              private poiTileLayerService: PoiTileLayerService,
              private plannerService: PlannerService,
              private mapService: MapService,
              private mapZoomService: MapZoomService,
              private i18nService: I18nService) {

    this.mapLayers$ = this._mapLayers$.asObservable();

    this.networkLayerChange$ = combineLatest([this.mapZoomService.zoomLevel$, this.plannerService.context.networkType$]).pipe(
      map(([zoomLevel, networkType]) => {
        return this.networkLayerChange(zoomLevel, networkType);
      }),
      filter(change => change !== null),
      tap(change => {
        this._mapLayers$.next(new MapLayers(this.standardLayers.push(change.newLayer)));
      })
    );
  }

  init(): void {
    this.osmLayer = this.mapLayerService.osmLayer("main-map");
    this.tileNameLayer = this.mapLayerService.tileNameLayer();
    this.poiLayer = this.poiTileLayerService.buildLayer();

    this.gpxVectorLayer = new GpxLayer().build();
    const gpxLayerName = this.i18nService.translation("@@map.layer.gpx");
    this.gpxVectorLayer.set("name", gpxLayerName);
    this.gpxLayer = new MapLayer("gpx-layer", this.gpxVectorLayer);

    this.bitmapLayers = this.buildBitmapLayers();
    this.vectorLayers = this.buildVectorLayers();

    this.standardLayers = List([this.osmLayer, this.tileNameLayer, this.poiLayer, this.gpxLayer]);

    this.allLayers = List([this.osmLayer, this.tileNameLayer, this.poiLayer, this.gpxLayer])
      .concat(this.bitmapLayers.values())
      .concat(this.vectorLayers.values());
  }

  applyMap(olMap: Map) {
    this.allLayers.forEach(mapLayer => {
      if (mapLayer.applyMap) {
        mapLayer.applyMap(olMap);
      }
    });

    const mainMapStyle = new MainMapStyle(olMap, this.mapService).styleFunction();
    List(this.vectorLayers.values()).forEach(l => {
      const vectorTileLayer = (l.layer) as VectorTileLayer;
      vectorTileLayer.setStyle(mainMapStyle);
      this.mapService.mapMode$.subscribe(() => vectorTileLayer.getSource().changed());
    });

    this.networkLayerChange$.subscribe(change => {
      if (change.oldLayer !== null) {
        olMap.removeLayer(change.oldLayer.layer);
      }
      olMap.getLayers().insertAt(this.standardLayers.size, change.newLayer.layer);
    });
  }

  private networkLayerChange(zoomLevel: number, networkType: NetworkType): MapLayerChange {
    let newLayer: MapLayer;
    if (zoomLevel <= ZoomLevel.bitmapTileMaxZoom) {
      newLayer = this.bitmapLayers.get(networkType);
    } else if (zoomLevel >= ZoomLevel.vectorTileMinZoom) {
      newLayer = this.vectorLayers.get(networkType);
    }

    const oldLayer = this.activeNetworkLayer;
    if (oldLayer !== null && oldLayer.name === newLayer.name) {
      // no change
      return null;
    }

    this.activeNetworkLayer = newLayer;
    return new MapLayerChange(oldLayer, newLayer);
  }

  private buildBitmapLayers(): ImmutableMap<NetworkType, MapLayer> {
    const keysAndValues: List<[NetworkType, MapLayer]> = NetworkType.all.map(networkType => {
      return [networkType, this.mapLayerService.networkBitmapTileLayer(networkType)];
    });
    return ImmutableMap<NetworkType, MapLayer>(keysAndValues.toArray());
  }

  private buildVectorLayers(): ImmutableMap<NetworkType, MapLayer> {
    const keyAndValues: List<[NetworkType, MapLayer]> = NetworkType.all.map(networkType => {
      return [networkType, this.mapLayerService.networkVectorTileLayer(networkType)];
    });
    return ImmutableMap<NetworkType, MapLayer>(keyAndValues.toArray());
  }
}
