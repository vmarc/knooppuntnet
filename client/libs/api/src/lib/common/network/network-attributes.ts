// this file is generated, please do not modify

import { LatLonImpl } from '@api/common';
import { Country } from '@api/custom';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Timestamp } from '@api/custom';
import { Integrity } from './integrity';

export interface NetworkAttributes {
  readonly id: number;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly name: string;
  readonly km: number;
  readonly meters: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly integrity: Integrity;
  readonly inaccessibleRouteCount: number;
  readonly connectionCount: number;
  readonly lastUpdated: Timestamp;
  readonly relationLastUpdated: Timestamp;
  readonly center: LatLonImpl;
}
