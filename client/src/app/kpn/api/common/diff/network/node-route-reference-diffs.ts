// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class NodeRouteReferenceDiffs {

  constructor(readonly removed: Array<Ref>,
              readonly added: Array<Ref>,
              readonly remaining: Array<Ref>) {
  }

  static fromJSON(jsonObject: any): NodeRouteReferenceDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteReferenceDiffs(
      jsonObject.removed.map((json: any) => Ref.fromJSON(json)),
      jsonObject.added.map((json: any) => Ref.fromJSON(json)),
      jsonObject.remaining.map((json: any) => Ref.fromJSON(json))
    );
  }
}
