// this class is generated, please do not modify

import {LegEnd} from "./leg-end";

export class LegBuildParams {

  constructor(readonly networkType: string,
              readonly legId: string,
              readonly source: LegEnd,
              readonly sink: LegEnd) {
  }

  public static fromJSON(jsonObject: any): LegBuildParams {
    if (!jsonObject) {
      return undefined;
    }
    return new LegBuildParams(
      jsonObject.networkType,
      jsonObject.legId,
      LegEnd.fromJSON(jsonObject.source),
      LegEnd.fromJSON(jsonObject.sink)
    );
  }
}
