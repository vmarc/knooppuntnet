// this class is generated, please do not modify

import {List} from 'immutable';

export class DirectionsInstruction {

  constructor(readonly text: string,
              readonly streetName: string,
              readonly distance: number,
              readonly time: number,
              readonly interval: List<number>,
              readonly sign: number,
              readonly annotationText: string,
              readonly annotationImportance: number,
              readonly exitNumber: number,
              readonly turnAngle: number) {
  }

  public static fromJSON(jsonObject): DirectionsInstruction {
    if (!jsonObject) {
      return undefined;
    }
    return new DirectionsInstruction(
      jsonObject.text,
      jsonObject.streetName,
      jsonObject.distance,
      jsonObject.time,
      jsonObject.interval ? List(jsonObject.interval) : List(),
      jsonObject.sign,
      jsonObject.annotationText,
      jsonObject.annotationImportance,
      jsonObject.exitNumber,
      jsonObject.turnAngle
    );
  }
}
