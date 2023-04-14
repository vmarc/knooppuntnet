// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { NetworkMapNode } from './network-map-node';
import { NetworkSummary } from './network-summary';

export interface NetworkMapPage {
  readonly summary: NetworkSummary;
  readonly nodes: NetworkMapNode[];
  readonly nodeIds: number[];
  readonly routeIds: number[];
  readonly bounds: Bounds;
}
