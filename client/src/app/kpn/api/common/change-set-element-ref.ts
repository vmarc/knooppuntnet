// this class is generated, please do not modify

export class ChangeSetElementRef {

  constructor(readonly id: number,
              readonly name: string,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject): ChangeSetElementRef {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetElementRef(
      jsonObject.id,
      jsonObject.name,
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
