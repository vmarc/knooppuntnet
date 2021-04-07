// this class is generated, please do not modify

import { Check } from './check';
import { Ref } from './common/ref';

export class NetworkFact {
  constructor(
    readonly name: string,
    readonly elementType: string | undefined,
    readonly elementIds: Array<number> | undefined,
    readonly elements: Array<Ref> | undefined,
    readonly checks: Array<Check> | undefined
  ) {}

  static fromJSON(jsonObject: any): NetworkFact {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFact(
      jsonObject.name,
      jsonObject.elementType,
      jsonObject.elementIds,
      jsonObject.elements?.map((json: any) => Ref.fromJSON(json)),
      jsonObject.checks?.map((json: any) => Check.fromJSON(json))
    );
  }
}
