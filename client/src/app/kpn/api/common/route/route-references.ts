// this class is generated, please do not modify

import { Reference } from '../common/reference';

export class RouteReferences {
  constructor(readonly networkReferences: Array<Reference>) {}

  public static fromJSON(jsonObject: any): RouteReferences {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteReferences(
      jsonObject.networkReferences.map((json: any) => Reference.fromJSON(json))
    );
  }
}
