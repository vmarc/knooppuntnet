import { Map } from 'immutable';

export class OldOldPoiPreference {
  constructor(public minLevel: number) {}

  static fromJSON(jsonObject: any): OldOldPoiPreference {
    if (!jsonObject) {
      return undefined;
    }
    return new OldOldPoiPreference(jsonObject.minLevel);
  }
}

export class OldOldPoiGroupPreference {
  constructor(
    public enabled: boolean,
    public pois: Map<string, OldOldPoiPreference>
  ) {}

  static fromJSON(jsonObject: any): OldOldPoiGroupPreference {
    if (!jsonObject) {
      return undefined;
    }
    const poiEntries: Array<[string, OldOldPoiPreference]> = Object.keys(jsonObject.pois).map(
      (poiName) => {
        const poiPreference = OldOldPoiPreference.fromJSON(jsonObject.pois[poiName]);
        return [poiName, poiPreference];
      }
    );
    const pois = Map(poiEntries);
    return new OldOldPoiGroupPreference(jsonObject.enabled, pois);
  }
}

export class OldOldPoiPreferences {
  constructor(
    readonly groups: Map<string, OldOldPoiGroupPreference>,
    public enabled: boolean
  ) {}

  static fromJSON(jsonObject: any): OldOldPoiPreferences {
    if (!jsonObject) {
      return undefined;
    }
    const groups: Map<string, OldOldPoiGroupPreference> = Map(
      Object.keys(jsonObject.groups).map((groupName) => {
        const groupJson = jsonObject.groups[groupName];
        const group = OldOldPoiGroupPreference.fromJSON(groupJson);
        return [groupName, group];
      })
    );
    return new OldOldPoiPreferences(groups, jsonObject.enabled);
  }

  poi(poiName: string): OldOldPoiPreference {
    let result: OldOldPoiPreference = null;
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
