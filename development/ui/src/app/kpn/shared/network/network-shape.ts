// this class is generated, please do not modify

import {Bounds} from '../bounds';

export class NetworkShape {
  readonly bounds: Bounds;
  readonly coordinates: string;

  constructor(bounds: Bounds,
              coordinates: string) {
    this.bounds = bounds;
    this.coordinates = coordinates;
  }

  public static fromJSON(jsonObject): NetworkShape {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkShape(
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.coordinates
    );
  }
}
