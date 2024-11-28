export class PoiPreference {
  constructor(public minLevel: number) {}

  static fromJSON(jsonObject: any): PoiPreference {
    if (!jsonObject) {
      return undefined;
    }
    return new PoiPreference(jsonObject.minLevel);
  }
}
