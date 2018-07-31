export class NetworkType {

  constructor(public name?: string) {
  }

  public static fromJSON(jsonObject): NetworkType {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkType();
    instance.name = jsonObject;
    return instance;
  }
}

