// this class is generated, please do not modify

export class TilePoiDefinition {

  constructor(readonly name: string,
              readonly icon: string,
              readonly minLevel: number) {
  }

  public static fromJSON(jsonObject): TilePoiDefinition {
    if (!jsonObject) {
      return undefined;
    }
    return new TilePoiDefinition(
      jsonObject.name,
      jsonObject.icon,
      jsonObject.minLevel
    );
  }
}
