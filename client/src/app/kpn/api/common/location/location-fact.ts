// this class is generated, please do not modify

import { Fact } from '../../custom/fact';
import { Ref } from '../common/ref';

export class LocationFact {
  constructor(
    readonly elementType: string,
    readonly fact: Fact,
    readonly refs: Array<Ref>
  ) {}

  public static fromJSON(jsonObject: any): LocationFact {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationFact(
      jsonObject.elementType,
      jsonObject.fact,
      jsonObject.refs?.map((json: any) => Ref.fromJSON(json))
    );
  }
}
