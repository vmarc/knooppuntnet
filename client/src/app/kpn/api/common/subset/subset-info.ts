// this file is generated, please do not modify

import { Country } from '../../custom/country';
import { NetworkType } from '../../custom/network-type';

export interface SubsetInfo {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkCount: number;
  readonly factCount: number;
  readonly changesCount: number;
  readonly orphanNodeCount: number;
  readonly orphanRouteCount: number;
}
