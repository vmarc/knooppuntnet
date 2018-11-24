// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../common/ref';

export class NetworkFactRefs {
  readonly networkId: number;
  readonly networkName: string;
  readonly factRefs: List<Ref>;

  constructor(networkId: number,
              networkName: string,
              factRefs: List<Ref>) {
    this.networkId = networkId;
    this.networkName = networkName;
    this.factRefs = factRefs;
  }

  public static fromJSON(jsonObject): NetworkFactRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFactRefs(
      jsonObject.networkId,
      jsonObject.networkName,
      jsonObject.factRefs ? List(jsonObject.factRefs.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
