// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { SubsetInfo } from './subset-info';
import { SubsetMapNetwork } from './subset-map-network';

export interface SubsetMapPage {
  readonly subsetInfo: SubsetInfo;
  readonly networks: SubsetMapNetwork[];
  readonly bounds: Bounds;
}
