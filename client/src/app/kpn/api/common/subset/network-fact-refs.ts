// this class is generated, please do not modify

import { List } from 'immutable';
import { Ref } from '../common/ref';

export class NetworkFactRefs {
  constructor(
    readonly networkId: number,
    readonly networkName: string,
    readonly factRefs: List<Ref>
  ) {}

  public static fromJSON(jsonObject: any): NetworkFactRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFactRefs(
      jsonObject.networkId,
      jsonObject.networkName,
      jsonObject.factRefs
        ? List(jsonObject.factRefs.map((json: any) => Ref.fromJSON(json)))
        : List()
    );
  }
}
