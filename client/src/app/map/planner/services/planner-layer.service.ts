import {Injectable} from "@angular/core";
import {List} from "immutable";
import {Map as ImmutableMap} from "immutable";
import Map from "ol/Map";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {tap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {map} from "rxjs/operators";
import {ZoomLevel} from "../../../components/ol/domain/zoom-level";
import {MapLayer} from "../../../components/ol/layers/map-layer";
import {MapLayerChange} from "../../../components/ol/layers/map-layer-change";
import {MapLayers} from "../../../components/ol/layers/map-layers";
import {MapLayerService} from "../../../components/ol/services/map-layer.service";
import {MapZoomService} from "../../../components/ol/services/map-zoom.service";
import {PoiTileLayerService} from "../../../components/ol/services/poi-tile-layer.service";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerService} from "../../planner.service";

@Injectable({
  providedIn: "root"
})
export class PlannerLayerService {

  readonly mapLayers$: Observable<MapLayers>;
  readonly standardLayers: List<MapLayer>;
  private _mapLayers$: BehaviorSubject<MapLayers>;
  private readonly networkLayerChange$: Observable<MapLayerChange>;
  private activeNetworkLayer: MapLayer = null;

  private readonly osmLayer: MapLayer;
  private readonly tileNameLayer: MapLayer;
  private readonly poiLayer: MapLayer;
  private readonly bitmapLayers: ImmutableMap<NetworkType, MapLayer>;
  private readonly vectorLayers: ImmutableMap<NetworkType, MapLayer>;
  private readonly allLayers: List<MapLayer>;

  constructor(private mapLayerService: MapLayerService,
              poiTileLayerService: PoiTileLayerService,
              private plannerService: PlannerService,
              private mapZoomService: MapZoomService) {
    this.osmLayer = mapLayerService.osmLayer();
    this.tileNameLayer = mapLayerService.tileNameLayer();
    this.poiLayer = poiTileLayerService.buildLayer();
    this.bitmapLayers = this.buildBitmapLayers();
    this.vectorLayers = this.buildVectorLayers();

    this.standardLayers = List([this.osmLayer, /*this.tileNameLayer,*/ this.poiLayer]);

    this.allLayers = List([this.osmLayer, this.tileNameLayer, this.poiLayer])
      .concat(this.bitmapLayers.values())
      .concat(this.vectorLayers.values());

    this._mapLayers$ = new BehaviorSubject<MapLayers>(new MapLayers(this.standardLayers));
    this.mapLayers$ = this._mapLayers$.asObservable();

    this.networkLayerChange$ = combineLatest([mapZoomService.zoomLevel$, plannerService.context.networkType$]).pipe(
      map(([zoomLevel, networkType]) => {
        return this.networkLayerChange(zoomLevel, networkType);
      }),
      filter(change => change !== null),
      tap(change => {
        this._mapLayers$.next(new MapLayers(this.standardLayers.push(change.newLayer)));
      })
    );
  }

  applyMap(olMap: Map) {
    this.allLayers.forEach(mapLayer => {
      if (mapLayer.applyMap) {
        mapLayer.applyMap(olMap);
      }
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
