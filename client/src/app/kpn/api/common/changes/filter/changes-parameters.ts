// this class is generated, please do not modify

import { Subset } from '../../../custom/subset';

export class ChangesParameters {
  constructor(
    readonly location: string,
    readonly subset: Subset,
    readonly networkId: number,
    readonly routeId: number,
    readonly nodeId: number,
    readonly year: string,
    readonly month: string,
    readonly day: string,
    readonly itemsPerPage: number,
    readonly pageIndex: number,
    readonly impact: boolean
  ) {}

  public static fromJSON(jsonObject: any): ChangesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesParameters(
      jsonObject.location,
      Subset.fromJSON(jsonObject.subset),
      jsonObject.networkId,
      jsonObject.routeId,
      jsonObject.nodeId,
      jsonObject.year,
      jsonObject.month,
      jsonObject.day,
      jsonObject.itemsPerPage,
      jsonObject.pageIndex,
      jsonObject.impact
    );
  }
}
