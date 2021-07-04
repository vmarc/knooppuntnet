// this class is generated, please do not modify

import { List } from 'immutable';
import { NodeIntegrityDetail } from './node-integrity-detail';

export class NodeIntegrity {
  constructor(readonly details: List<NodeIntegrityDetail>) {}

  public static fromJSON(jsonObject: any): NodeIntegrity {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrity(
      jsonObject.details
        ? List(
            jsonObject.details.map((json: any) =>
              NodeIntegrityDetail.fromJSON(json)
            )
          )
        : List()
    );
  }
}
