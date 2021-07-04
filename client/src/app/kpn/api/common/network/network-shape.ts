// this class is generated, please do not modify

import { Bounds } from '../bounds';

export class NetworkShape {
  constructor(readonly bounds: Bounds, readonly coordinates: string) {}

  public static fromJSON(jsonObject: any): NetworkShape {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkShape(
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.coordinates
    );
  }
}
