// this class is generated, please do not modify

import { Timestamp } from '../custom/timestamp';

export class OrphanNodeInfo {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly longName: string,
    readonly lastUpdated: Timestamp,
    readonly lastSurvey: string,
    readonly factCount: number
  ) {}

  static fromJSON(jsonObject: any): OrphanNodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new OrphanNodeInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.longName,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.lastSurvey,
      jsonObject.factCount
    );
  }
}
