// this class is generated, please do not modify

export class ClientPoiDefinition {
  constructor(
    readonly name: string,
    readonly icon: string,
    readonly minLevel: number,
    readonly defaultLevel: number
  ) {}

  static fromJSON(jsonObject: any): ClientPoiDefinition {
    if (!jsonObject) {
      return undefined;
    }
    return new ClientPoiDefinition(
      jsonObject.name,
      jsonObject.icon,
      jsonObject.minLevel,
      jsonObject.defaultLevel
    );
  }
}
