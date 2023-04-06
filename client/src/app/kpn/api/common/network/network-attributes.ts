// this file is generated, please do not modify

import { Country } from '@api/custom/country';
import { Integrity } from './integrity';
import { LatLonImpl } from '../lat-lon-impl';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { Timestamp } from '@api/custom/timestamp';

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
