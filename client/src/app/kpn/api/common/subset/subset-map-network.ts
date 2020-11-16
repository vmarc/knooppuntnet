// this class is generated, please do not modify

import {LatLonImpl} from '../lat-lon-impl';

export class SubsetMapNetwork {

  constructor(readonly id: number,
              readonly name: string,
              readonly km: number,
              readonly nodeCount: number,
              readonly routeCount: number,
              readonly center: LatLonImpl) {
  }

  public static fromJSON(jsonObject: any): SubsetMapNetwork {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetMapNetwork(
      jsonObject.id,
      jsonObject.name,
      jsonObject.km,
      jsonObject.nodeCount,
      jsonObject.routeCount,
      LatLonImpl.fromJSON(jsonObject.center)
    );
  }
}
