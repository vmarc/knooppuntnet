// this class is generated, please do not modify

import {Timestamp} from '../../custom/timestamp';

export class MetaData {

  constructor(readonly version: number,
              readonly timestamp: Timestamp,
              readonly changeSetId: number) {
  }

  static fromJSON(jsonObject: any): MetaData {
    if (!jsonObject) {
      return undefined;
    }
    return new MetaData(
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId
    );
  }
}
