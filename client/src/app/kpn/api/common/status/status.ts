// this class is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { DiskUsage } from './disk-usage';

export class Status {
  constructor(
    readonly timestamp: ActionTimestamp,
    readonly diskUsage: DiskUsage
  ) {}

  public static fromJSON(jsonObject: any): Status {
    if (!jsonObject) {
      return undefined;
    }
    return new Status(
      ActionTimestamp.fromJSON(jsonObject.timestamp),
      DiskUsage.fromJSON(jsonObject.diskUsage)
    );
  }
}
