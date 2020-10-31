export class Settings {

  constructor(public instructions: boolean,
              public extraLayers: boolean) {
  }

  public static fromJSON(jsonObject: any): Settings {
    if (!jsonObject) {
      return undefined;
    }
    return new Settings(
      jsonObject.instructions,
      jsonObject.extraLayers
    );
  }
}
