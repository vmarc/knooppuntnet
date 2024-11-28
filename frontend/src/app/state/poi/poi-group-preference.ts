import { PoiPreference } from './poi-preference';

export class PoiGroupPreference {
  constructor(
    public enabled: boolean,
    public pois: ReadonlyMap<string, PoiPreference>
  ) {}

  static fromJSON(jsonObject: any): PoiGroupPreference {
    if (!jsonObject) {
      return undefined;
    }
    const poiEntries: Array<[string, PoiPreference]> = Object.keys(jsonObject.pois).map(
      (poiName) => {
        const poiPreference = PoiPreference.fromJSON(jsonObject.pois[poiName]);
        return [poiName, poiPreference];
      }
    );
    const pois = new Map<string, PoiPreference>(poiEntries);
    return new PoiGroupPreference(jsonObject.enabled, pois);
  }
}
