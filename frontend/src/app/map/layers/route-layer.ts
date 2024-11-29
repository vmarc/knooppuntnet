import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { NetworkType } from '@api/custom';
import { ZoomLevel } from '@app/ol/domain';
import { FeatureLike } from 'ol/Feature';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { MapStyleOptions } from '../../state/map-style-options';
import { ExploreStyle } from '../style/explore-style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class RouteLayer {
  private styleOptions: MapStyleOptions; // local copy for performance reasons

  constructor(styleOptionsSignal: Signal<MapStyleOptions>) {
    effect(() => {
      this.styleOptions = styleOptionsSignal();
      const options = [
        'zoom=' + this.styleOptions.zoom,
        'mode=' + this.styleOptions.mode,
        'international=' + this.styleOptions.scopeInternational,
        'national=' + this.styleOptions.scopeNational,
        'regional=' + this.styleOptions.scopeRegional,
        'local=' + this.styleOptions.scopeLocal,
        'nodeRoutes=' + this.styleOptions.scopeNodeRoutes,
        'route=' + this.styleOptions.selectedRoute,
      ];
      console.log(`mapStyleOptions ${options.join(', ')}`);
    });
  }

  build(networkType: NetworkType): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/${networkType}/{z}/{x}/{y}.mvt`,
    });
    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      source: source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });
    return {
      layerType: 'route',
      networkType: networkType,
      minZoom: 2,
      maxZoom: 20,
      layer: layer,
    };
  }

  private styleFunction(): StyleFunction {
    return (feature: FeatureLike) => {
      return ExploreStyle.style(this.styleOptions, feature);
    };
  }
}
