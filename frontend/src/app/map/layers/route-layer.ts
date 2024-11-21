import { ZoomLevel } from '@app/ol/domain';
import { OldLayers } from '@app/ol/layers';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { ExploreStyle } from '../style/explore-style';

export class RouteLayer {
  private static readonly source = new VectorTile({
    tileSize: 512,
    minZoom: 6, // ZoomLevel.vectorTileMinZoom,
    maxZoom: ZoomLevel.vectorTileMaxZoom,
    format: new MVT(),
    url: `/tiles/hiking/{z}/{x}/{y}.mvt`,
  });

  static build(): VectorTileLayer {
    return new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      source: this.source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });
  }

  static styleFunction(): StyleFunction {
    return (feature, resolution) => {
      // const routeId = feature.get('routeId');
      // const name = feature.get('name');
      // const layer = feature.get('layer');
      // const surface = feature.get('surface');
      // const segmentId = feature.get('segmentId');
      // const segmentElementId = feature.get('segmentElementId');
      // const pathIds = feature.get('pathIds');
      const scope = feature.get('layer');

      // console.log(
      //   `feature routeId=${routeId}, name=${name}, layer=${layer}, surface=${surface}, segmentId=${segmentId}, segmentElementId=${segmentElementId}, pathIds=${pathIds}`
      // );

      return ExploreStyle.style(scope, resolution);
    };
  }
}
