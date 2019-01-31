// this class is generated, please do not modify

export class TilePoiDefinition {
  readonly name: string;
  readonly icon: string;
  readonly minLevel: number;

  constructor(name: string,
              icon: string,
              minLevel: number) {
    this.name = name;
    this.icon = icon;
    this.minLevel = minLevel;
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
