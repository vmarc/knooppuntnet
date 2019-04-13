// this class is generated, please do not modify

import {List} from "immutable";
import {Country} from "../country";
import {NetworkType} from "../network-type";
import {Ref} from "./ref";

export class NetworkRefs {

  constructor(readonly country: Country,
              readonly networkType: NetworkType,
              readonly networkRef: Ref,
              readonly refType: string,
              readonly refs: List<Ref>) {
  }

  public static fromJSON(jsonObject): NetworkRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRefs(
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
      Ref.fromJSON(jsonObject.networkRef),
      jsonObject.refType,
      jsonObject.refs ? List(jsonObject.refs.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
