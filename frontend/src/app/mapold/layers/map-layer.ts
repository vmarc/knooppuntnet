import { RouteType } from '@api/common/route-type';
import BaseLayer from 'ol/layer/Base';
import { LayerType } from '@app/state/layer-type';

export interface MapLayer {
  layerType: LayerType;
  routeType?: RouteType;
  minZoom: number;
  maxZoom: number;
  layer: BaseLayer;
}
