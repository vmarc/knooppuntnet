// this file is generated, please do not modify

import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';

export interface SubsetInfo {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkCount: number;
  readonly factCount: number;
  readonly changesCount: number;
  readonly orphanNodeCount: number;
  readonly orphanRouteCount: number;
}
