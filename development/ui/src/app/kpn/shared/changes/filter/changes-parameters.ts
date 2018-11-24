// this class is generated, please do not modify

import {Subset} from '../../subset';

export class ChangesParameters {
  readonly subset: Subset;
  readonly networkId: number;
  readonly routeId: number;
  readonly nodeId: number;
  readonly year: string;
  readonly month: string;
  readonly day: string;
  readonly itemsPerPage: number;
  readonly pageIndex: number;
  readonly impact: boolean;

  constructor(subset: Subset,
              networkId: number,
              routeId: number,
              nodeId: number,
              year: string,
              month: string,
              day: string,
              itemsPerPage: number,
              pageIndex: number,
              impact: boolean) {
    this.subset = subset;
    this.networkId = networkId;
    this.routeId = routeId;
    this.nodeId = nodeId;
    this.year = year;
    this.month = month;
    this.day = day;
    this.itemsPerPage = itemsPerPage;
    this.pageIndex = pageIndex;
    this.impact = impact;
  }

  public static fromJSON(jsonObject): ChangesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesParameters(
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
