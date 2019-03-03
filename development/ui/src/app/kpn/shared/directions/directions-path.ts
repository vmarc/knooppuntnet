// this class is generated, please do not modify

import {List} from 'immutable';
import {DirectionsInstruction} from './directions-instruction';

export class DirectionsPath {

  constructor(readonly distance: number,
              readonly weight: number,
              readonly time: number,
              readonly transfers: number,
              readonly ascend: number,
              readonly descend: number,
              readonly instructions: List<DirectionsInstruction>) {
  }

  public static fromJSON(jsonObject): DirectionsPath {
    if (!jsonObject) {
      return undefined;
    }
    return new DirectionsPath(
      jsonObject.distance,
      jsonObject.weight,
      jsonObject.time,
      jsonObject.transfers,
      jsonObject.ascend,
      jsonObject.descend,
      jsonObject.instructions ? List(jsonObject.instructions.map(json => DirectionsInstruction.fromJSON(json))) : List()
    );
  }
}
