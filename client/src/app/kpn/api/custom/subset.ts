import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {

  constructor(readonly country: Country,
              readonly networkType: NetworkType) {
  }

  public static fromJSON(jsonObject: any): Subset {
    if (!jsonObject) {
      return undefined;
    }
    return new Subset(
      jsonObject.country,
      jsonObject.networkType
    );
  }

}
