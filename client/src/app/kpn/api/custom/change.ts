export class Change {

  constructor(readonly action: number,
              readonly elements: any[]) {
  }

  public static fromJSON(jsonObject: any): Change {
    if (!jsonObject) {
      return undefined;
    }
    return new Change(
      jsonObject.action,
      jsonObject.elements
    );
  }
}
