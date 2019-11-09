// this class is generated, please do not modify

export class NetworkScope {

  constructor(readonly name: string,
              readonly letter: string) {
  }

  public static fromJSON(jsonObject): NetworkScope {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkScope(
      jsonObject.name,
      jsonObject.letter
    );
  }
}
