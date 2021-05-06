// this class is generated, please do not modify

import { Timestamp } from '../custom/timestamp';

export class OrphanRouteInfo {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly meters: number,
    readonly isBroken: boolean,
    readonly accessible: boolean,
    readonly lastSurvey: string,
    readonly lastUpdated: Timestamp
  ) {}

  static fromJSON(jsonObject: any): OrphanRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new OrphanRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.meters,
      jsonObject.isBroken,
      jsonObject.accessible,
      jsonObject.lastSurvey,
      Timestamp.fromJSON(jsonObject.lastUpdated)
    );
  }
}
