import { RouteType } from '@api/common';
import BaseLayer from 'ol/layer/Base';
import { LayerType } from './layer-type';

export interface MapLayer {
  layerType: LayerType;
  routeType?: RouteType;
  minZoom: number;
  maxZoom: number;
  layer: BaseLayer;
}
