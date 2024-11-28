import { Map } from 'immutable';

export class OldPoiPreference {
  constructor(public minLevel: number) {}

  static fromJSON(jsonObject: any): OldPoiPreference {
    if (!jsonObject) {
      return undefined;
    }
    return new OldPoiPreference(jsonObject.minLevel);
  }
}

export class OldPoiGroupPreference {
  constructor(
    public enabled: boolean,
    public pois: Map<string, OldPoiPreference>
  ) {}

  static fromJSON(jsonObject: any): OldPoiGroupPreference {
    if (!jsonObject) {
      return undefined;
    }
    const poiEntries: Array<[string, OldPoiPreference]> = Object.keys(jsonObject.pois).map(
      (poiName) => {
        const poiPreference = OldPoiPreference.fromJSON(jsonObject.pois[poiName]);
        return [poiName, poiPreference];
      }
    );
    const pois = Map(poiEntries);
    return new OldPoiGroupPreference(jsonObject.enabled, pois);
  }
}

export class OldPoiPreferences {
  constructor(
    readonly groups: Map<string, OldPoiGroupPreference>,
    public enabled: boolean
  ) {}

  static fromJSON(jsonObject: any): OldPoiPreferences {
    if (!jsonObject) {
      return undefined;
    }
    const groups: Map<string, OldPoiGroupPreference> = Map(
      Object.keys(jsonObject.groups).map((groupName) => {
        const groupJson = jsonObject.groups[groupName];
        const group = OldPoiGroupPreference.fromJSON(groupJson);
        return [groupName, group];
      })
    );
    return new OldPoiPreferences(groups, jsonObject.enabled);
  }

  poi(poiName: string): OldPoiPreference {
    let result: OldPoiPreference = null;
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
