// this class is generated, please do not modify

export class DirectionsInstruction {

  constructor(readonly text: string,
              readonly streetName: string,
              readonly distance: number,
              readonly sign: string,
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
      jsonObject.sign,
      jsonObject.annotationText,
      jsonObject.annotationImportance,
      jsonObject.exitNumber,
      jsonObject.turnAngle
    );
  }
}
