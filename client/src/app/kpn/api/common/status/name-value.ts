// this class is generated, please do not modify

export class NameValue {

  constructor(readonly name: string,
              readonly value: number) {
  }

  public static fromJSON(jsonObject: any): NameValue {
    if (!jsonObject) {
      return undefined;
    }
    return new NameValue(
      jsonObject.name,
      jsonObject.value
    );
  }
}
