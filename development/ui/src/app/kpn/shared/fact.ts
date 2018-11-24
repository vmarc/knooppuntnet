// this class is generated, please do not modify

import {FactLevel} from './fact-level';

export class Fact {
  readonly id: number;
  readonly name: string;
  readonly nlName: string;
  readonly level: FactLevel;

  constructor(id: number,
              name: string,
              nlName: string,
              level: FactLevel) {
    this.id = id;
    this.name = name;
    this.nlName = nlName;
    this.level = level;
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
