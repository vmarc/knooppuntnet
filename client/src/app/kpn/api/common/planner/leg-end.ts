// this class is generated, please do not modify

import {LegEndNode} from './leg-end-node';
import {LegEndRoute} from './leg-end-route';

export class LegEnd {

  constructor(readonly node: LegEndNode,
              readonly route: LegEndRoute) {
  }

  public static fromJSON(jsonObject: any): LegEnd {
    if (!jsonObject) {
      return undefined;
    }
    return new LegEnd(
      LegEndNode.fromJSON(jsonObject.node),
      LegEndRoute.fromJSON(jsonObject.route)
    );
  }
}
