// this class is generated, please do not modify

import {PoiAnalysis} from "./poi-analysis";

export class PoiPage {

  constructor(readonly elementType: string,
              readonly elementId: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly analysis: PoiAnalysis) {
  }

  public static fromJSON(jsonObject: any): PoiPage {
    if (!jsonObject) {
      return undefined;
    }
    return new PoiPage(
      jsonObject.elementType,
      jsonObject.elementId,
      jsonObject.latitude,
      jsonObject.longitude,
      PoiAnalysis.fromJSON(jsonObject.analysis)
    );
  }
}
