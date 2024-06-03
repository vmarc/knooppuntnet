// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { NetworkMapNode } from './network-map-node';
import { NetworkSummary } from './network-summary';

export interface NetworkMapPage {
  readonly summary: NetworkSummary;
  readonly nodes: NetworkMapNode[];
  readonly networkNodeIds: number[];
  readonly connectionNodeIds: number[];
  readonly networkRouteIds: number[];
  readonly connectionRouteIds: number[];
  readonly bounds: Bounds;
}
