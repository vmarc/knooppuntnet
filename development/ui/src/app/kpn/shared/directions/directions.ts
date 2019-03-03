// this class is generated, please do not modify

import {List} from 'immutable';
import {DirectionsInstruction} from './directions-instruction';

export class Directions {

  constructor(readonly distance: number,
              readonly ascend: number,
              readonly descend: number,
              readonly instructions: List<DirectionsInstruction>) {
  }

  public static fromJSON(jsonObject): Directions {
    if (!jsonObject) {
      return undefined;
    }
    return new Directions(
      jsonObject.distance,
      jsonObject.ascend,
      jsonObject.descend,
      jsonObject.instructions ? List(jsonObject.instructions.map(json => DirectionsInstruction.fromJSON(json))) : List()
    );
  }
}
