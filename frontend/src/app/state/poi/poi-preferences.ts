import { PoiGroupPreference } from './poi-group-preference';
import { PoiPreference } from './poi-preference';

export class PoiPreferences {
  constructor(
    readonly groups: ReadonlyMap<string, PoiGroupPreference>,
    public enabled: boolean
  ) {}

  static fromJSON(jsonObject: any): PoiPreferences {
    if (!jsonObject) {
      return undefined;
    }
    const groups: Map<string, PoiGroupPreference> = new Map(
      Object.keys(jsonObject.groups).map((groupName) => {
        const groupJson = jsonObject.groups[groupName];
        const group = PoiGroupPreference.fromJSON(groupJson);
        return [groupName, group];
      })
    );
    return new PoiPreferences(groups, jsonObject.enabled);
  }

  poi(poiName: string): PoiPreference {
    let result: PoiPreference = null;
    this.groups.forEach((group, groupName) => {
      group.pois.forEach((poi, poiNameKey) => {
        if (poiNameKey === poiName) {
          result = poi;
        }
      });
    });
    return result;
  }
}
