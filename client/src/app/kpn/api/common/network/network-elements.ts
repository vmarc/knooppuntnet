// this class is generated, please do not modify

import {ElementIds} from "../../../server/analyzer/engine/changes/changes/element-ids";

export class NetworkElements {

  constructor(readonly networkId: number,
              readonly elementsIds: ElementIds) {
  }

  public static fromJSON(jsonObject: any): NetworkElements {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkElements(
      jsonObject.networkId,
      ElementIds.fromJSON(jsonObject.elementsIds)
    );
  }
}
