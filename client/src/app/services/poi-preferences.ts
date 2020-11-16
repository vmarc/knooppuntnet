import {Map} from 'immutable';

export class PoiPreference {

  constructor(public minLevel: number) {
  }

  public static fromJSON(jsonObject: any): PoiPreference {
    if (!jsonObject) {
      return undefined;
    }
    return new PoiPreference(
      jsonObject.minLevel
    );
  }
}

export class PoiGroupPreference {
  constructor(public enabled: boolean,
              public pois: Map<string, PoiPreference>) {
  }

  public static fromJSON(jsonObject: any): PoiGroupPreference {
    if (!jsonObject) {
      return undefined;
    }
    const poiEntries: Array<[string, PoiPreference]> = Object.keys(jsonObject.pois).map(poiName => {
      const poiPreference = PoiPreference.fromJSON(jsonObject.pois[poiName]);
      return [poiName, poiPreference];
    });
    const pois = Map(poiEntries);
    return new PoiGroupPreference(jsonObject.enabled, pois);
  }
}

export class PoiPreferences {

  constructor(readonly groups: Map<string, PoiGroupPreference>,
              public enabled: boolean) {
  }

  public static fromJSON(jsonObject: any): PoiPreferences {
    if (!jsonObject) {
      return undefined;
    }
    const groups: Map<string, PoiGroupPreference> = Map(
      Object.keys(jsonObject.groups).map(groupName => {
        const groupJson = jsonObject.groups[groupName];
        const group = PoiGroupPreference.fromJSON(groupJson);
        return [groupName, group];
      })
    );
    return new PoiPreferences(
      groups,
      jsonObject.enabled
    );
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
