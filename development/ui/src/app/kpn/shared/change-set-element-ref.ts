// this class is generated, please do not modify

export class ChangeSetElementRef {
  readonly id: number;
  readonly name: string;
  readonly happy: boolean;
  readonly investigate: boolean;

  constructor(id: number,
              name: string,
              happy: boolean,
              investigate: boolean) {
    this.id = id;
    this.name = name;
    this.happy = happy;
    this.investigate = investigate;
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
