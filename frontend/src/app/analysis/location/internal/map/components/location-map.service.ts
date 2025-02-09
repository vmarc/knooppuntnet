import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';
import { LocationKey } from '@api/custom/location-key';
import { SurveyDateValues } from '@app/shared/core/shared/survey-date-values';
import { CachedMapPosition } from '@app/ol/domain/cached-map-position';
import { MapPosition } from '@app/ol/domain/map-position';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { OldOpenDataLayers } from '@app/ol/layers/old-open-data-layers';
import { OldBackgroundLayer } from '@app/ol/layers/old-background-layer';
import { OldOsmLayer } from '@app/ol/layers/old-osm-layer';
import { NetworkVectorTileLayer } from '@app/ol/layers/network-vector-tile-layer';
import { NetworkBitmapTileLayer } from '@app/ol/layers/network-bitmap-tile-layer';
import { LocationBoundaryLayer } from '@app/ol/layers/location-boundary-layer';
import { MapControls } from '@app/ol/layers/map-controls';
import { OldMapLayerRegistry } from '@app/ol/layers/old-map-layer-registry';
import { OpenlayersMapService } from '@app/ol/services/openlayers-map-service';
import { MapClickService } from '@app/ol/services/map-click.service';
import { MainMapStyleParameters } from '@app/ol/style/main-map-style-parameters';
import { MainMapStyle } from '@app/ol/style/main-map-style';
import { BrowserStorageService } from '@app/shared/services/browser-storage.service';
import { Util } from '@app/shared/components/util';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class LocationMapService extends OpenlayersMapService {
  private readonly mapClickService = inject(MapClickService);
  private readonly storage = inject(BrowserStorageService);
  private locationMapPositionKey = 'location-map-position';

  locationId: string | null = null;

  constructor() {
    super();
    effect(() => {
      const mapPosition = this.mapPosition();
      if (mapPosition && this.locationId) {
        const cachedMapPosition = mapPosition.toCachedMapPosition(this.locationId);
        this.storage.set(this.locationMapPositionKey, JSON.stringify(cachedMapPosition));
      }
    });
  }

  init(
    locationKey: LocationKey,
    surveyDateValues: SurveyDateValues,
    geoJson: string,
    geoJson2: string,
    bounds: Bounds,
    mapPositionFromUrl: MapPosition,
    urlLayerIds: string[]
  ): void {
    this.locationId = locationKey.name;
    this.registerLayers(locationKey.routeType, surveyDateValues, geoJson, geoJson2, urlLayerIds);

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.vectorTileMaxOverZoom,
        }),
      })
    );
    if (mapPositionFromUrl) {
      this.gotoLastKnownPosition(mapPositionFromUrl);
    } else {
      const mapPositionString = this.storage.get(this.locationMapPositionKey);
      if (mapPositionString == null) {
        this.map.getView().fit(Util.toExtent(bounds, 0.05));
      } else {
        const cachedMapPosition: CachedMapPosition = JSON.parse(mapPositionString);
        if (this.locationId === cachedMapPosition.id) {
          const mapPosition = MapPosition.fromCachedMapPosition(cachedMapPosition);
          this.gotoLastKnownPosition(mapPosition);
        } else {
          this.map.getView().fit(Util.toExtent(bounds, 0.1));
        }
      }
    }
    this.mapClickService.installOn(this.map);
    this.finalizeSetup(true);
  }

  private registerLayers(
    routeType: RouteType,
    surveyDateValues: SurveyDateValues,
    geoJson: string,
    geoJson2: string,
    urlLayerIds: string[]
  ): void {
    const parameters = signal<MainMapStyleParameters>(
      new MainMapStyleParameters('analysis', true, surveyDateValues, null, null)
    );
    const mainMapStyle = new MainMapStyle(parameters);
    const networkLayers = [
      NetworkVectorTileLayer.build(routeType, mainMapStyle.styleFunction()),
      NetworkBitmapTileLayer.build(routeType, 'analysis'),
    ];

    const registry = new OldMapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);
    registry.registerAll(urlLayerIds, networkLayers, true);
    registry.register(urlLayerIds, LocationBoundaryLayer.build(geoJson), true);
    if (geoJson2) {
      registry.register(urlLayerIds, LocationBoundaryLayer.build2(geoJson2), true);
    }
    OldOpenDataLayers.register(registry, routeType, urlLayerIds);
    this.register(registry);
  }

  private gotoLastKnownPosition(mapPosition: MapPosition): void {
    this.map.getView().setZoom(mapPosition.zoom);
    this.map.getView().setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.map.getView().setCenter(center);
  }
}
