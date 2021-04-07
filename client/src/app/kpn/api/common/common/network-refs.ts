// this class is generated, please do not modify

import { Country } from '../../custom/country';
import { NetworkType } from '../../custom/network-type';
import { Ref } from './ref';

export class NetworkRefs {
  constructor(
    readonly country: Country,
    readonly networkType: NetworkType,
    readonly networkRef: Ref,
    readonly refType: string,
    readonly refs: Array<Ref>
  ) {}

  static fromJSON(jsonObject: any): NetworkRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRefs(
      jsonObject.country,
      jsonObject.networkType,
      Ref.fromJSON(jsonObject.networkRef),
      jsonObject.refType,
      jsonObject.refs.map((json: any) => Ref.fromJSON(json))
    );
  }
}
