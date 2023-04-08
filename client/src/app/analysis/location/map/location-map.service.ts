import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers/network-bitmap-tile-layer';
import { LocationBoundaryLayer } from '@app/components/ol/layers/location-boundary-layer';
import { SurveyDateValues } from '@app/components/ol/services/survey-date-values';
import { NetworkType } from '@api/custom/network-type';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Util } from '@app/components/shared/util';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import Map from 'ol/Map';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { Bounds } from '@api/common/bounds';

@Injectable()
export class LocationMapService extends OpenlayersMapService {
  constructor(private mapClickService: MapClickService) {
    super();
  }

  init(
    networkType: NetworkType,
    surveyDateValues: SurveyDateValues,
    geoJson: string,
    bounds: Bounds
  ): void {
    this.registerLayers(networkType, surveyDateValues, geoJson);

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

    this.map.getView().fit(Util.toExtent(bounds, 0.05));
    this.mapClickService.installOn(this.map);
    this.finalizeSetup();
  }

  private registerLayers(
    networkType: NetworkType,
    surveyDateValues: SurveyDateValues,
    geoJson: string
  ): void {
    const parameters$: Observable<MainMapStyleParameters> = of(
      new MainMapStyleParameters(
        'analysis',
        true,
        surveyDateValues,
        null,
        null,
        null
      )
    );
    const mainMapStyle = new MainMapStyle(parameters$);
    const networkLayers = [
      NetworkVectorTileLayer.build(networkType, mainMapStyle.styleFunction()),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
    ];

    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.registerAll([], networkLayers, true);
    registry.register([], LocationBoundaryLayer.build(geoJson), true);
    this.register(registry);
  }
}
