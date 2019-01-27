export class Tag {

  constructor(readonly key: string,
              readonly value: string) {
  }

  public static fromJSON(jsonObject): Tag {
    if (!jsonObject) {
      return undefined;
    }
    return new Tag(
      jsonObject[0],
      jsonObject[1]
    );
  }
}
