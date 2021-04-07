// this class is generated, please do not modify

import { Ref } from './common/ref';

export class RoutesFact {
  constructor(readonly routes: Array<Ref>) {}

  static fromJSON(jsonObject: any): RoutesFact {
    if (!jsonObject) {
      return undefined;
    }
    return new RoutesFact(
      jsonObject.routes.map((json: any) => Ref.fromJSON(json))
    );
  }
}
