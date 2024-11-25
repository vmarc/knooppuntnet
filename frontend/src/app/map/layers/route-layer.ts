import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { NetworkType } from '@api/custom';
import { ZoomLevel } from '@app/ol/domain';
import { OldLayers } from '@app/ol/layers';
import { FeatureLike } from 'ol/Feature';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { MapStyleOptions } from '../../state/map-style-options';
import { ExploreStyle } from '../style/explore-style';

export class RouteLayer {
  private styleOptions: MapStyleOptions; // local copy for performance reasons

  constructor(styleOptionsSignal: Signal<MapStyleOptions>) {
    effect(() => {
      this.styleOptions = styleOptionsSignal();
      const zoom = this.styleOptions.zoom;
      const mode = this.styleOptions.mode;
      const international = this.styleOptions.scopeInternational;
      const national = this.styleOptions.scopeNational;
      const regional = this.styleOptions.scopeRegional;
      const local = this.styleOptions.scopeLocal;
      const nodeRoutes = this.styleOptions.scopeNodeRoutes;
      const route = this.styleOptions.selectedRoute;
      console.log(
        `mapOptions zoom=${zoom}, mode=${mode}, international=${international}, national=${national}, regional=${regional}, local=${local}, nodeRoutes=${nodeRoutes}, route=${route}`
      );
    });
  }

  build(networkType: NetworkType): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/${networkType}/{z}/{x}/{y}.mvt`,
    });
    return new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      source: source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });
  }

  private styleFunction(): StyleFunction {
    return (feature: FeatureLike) => {
      return ExploreStyle.style(this.styleOptions, feature);
    };
  }
}
