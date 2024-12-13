import { NetworkType } from '@api/common';
import BaseLayer from 'ol/layer/Base';
import { LayerType } from './layer-type';

export interface MapLayer {
  layerType: LayerType;
  networkType?: NetworkType;
  minZoom: number;
  maxZoom: number;
  layer: BaseLayer;
}
