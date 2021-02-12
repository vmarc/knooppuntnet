// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RouteNodeDiff {

  constructor(readonly title: string,
              readonly added: Array<Ref>,
              readonly removed: Array<Ref>) {
  }

  public static fromJSON(jsonObject: any): RouteNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNodeDiff(
      jsonObject.title,
      jsonObject.added.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removed.map((json: any) => Ref.fromJSON(json))
    );
  }
}
