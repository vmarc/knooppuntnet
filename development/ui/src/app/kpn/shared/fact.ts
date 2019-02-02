// this class is generated, please do not modify

import {FactLevel} from './fact-level';

export class Fact {

  constructor(readonly id: number,
              readonly name: string,
              readonly nlName: string,
              readonly level: FactLevel) {
  }

  public static fromJSON(jsonObject): Fact {
    if (!jsonObject) {
      return undefined;
    }
    return new Fact(
      jsonObject.id,
      jsonObject.name,
      jsonObject.nlName,
      FactLevel.fromJSON(jsonObject.level)
    );
  }
}
