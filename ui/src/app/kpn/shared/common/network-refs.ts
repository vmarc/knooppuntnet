// this class is generated, please do not modify

import {Country} from '../country';
import {NetworkType} from '../network-type';
import {Ref} from './ref';

export class NetworkRefs {

  constructor(public country?: Country,
              public networkType?: NetworkType,
              public networkRef?: Ref,
              public refType?: string,
              public refs?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): NetworkRefs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkRefs();
    instance.country = Country.fromJSON(jsonObject.country);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.networkRef = Ref.fromJSON(jsonObject.networkRef);
    instance.refType = jsonObject.refType;
    instance.refs = jsonObject.refs ? jsonObject.refs.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

