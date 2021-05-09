// this class is generated, please do not modify

import { Country } from '../../custom/country';
import { NetworkType } from '../../custom/network-type';

export class SubsetInfo {
  constructor(
    readonly country: Country,
    readonly networkType: NetworkType,
    readonly networkCount: number,
    readonly factCount: number,
    readonly changesCount: number,
    readonly orphanNodeCount: number,
    readonly orphanRouteCount: number
  ) {}

  static fromJSON(jsonObject: any): SubsetInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetInfo(
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.networkCount,
      jsonObject.factCount,
      jsonObject.changesCount,
      jsonObject.orphanNodeCount,
      jsonObject.orphanRouteCount
    );
  }
}
