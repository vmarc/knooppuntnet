export class NetworkType {
  readonly name: string;

  constructor(name: string) {
    this.name = name;
  }

  public static fromJSON(jsonObject): NetworkType {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkType(jsonObject);
  }
}
