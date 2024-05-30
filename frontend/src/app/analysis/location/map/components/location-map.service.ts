import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { LocationKey } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { SurveyDateValues } from '@app/core';
import { CachedMapPosition } from '@app/ol/domain';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { LocationBoundaryLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { MapClickService } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { MainMapStyle } from '@app/ol/style';
import { BrowserStorageService } from '@app/services';
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
    this.registerLayers(locationKey.networkType, surveyDateValues, geoJson, geoJson2, urlLayerIds);

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
    networkType: NetworkType,
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
      NetworkVectorTileLayer.build(networkType, mainMapStyle.styleFunction()),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
    ];

    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, BackgroundLayer.build(), true);
    registry.register(urlLayerIds, OsmLayer.build(), false);
    registry.registerAll(urlLayerIds, networkLayers, true);
    registry.register(urlLayerIds, LocationBoundaryLayer.build(geoJson), true);
    registry.register(urlLayerIds, LocationBoundaryLayer.build2(geoJson2), true);
    this.register(registry);
  }

  private gotoLastKnownPosition(mapPosition: MapPosition): void {
    this.map.getView().setZoom(mapPosition.zoom);
    this.map.getView().setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.map.getView().setCenter(center);
  }
}
