import {Map} from "immutable";

export class PoiPreference {
  constructor(public minLevel: number) {
  }
}

export class PoiGroupPreference {
  constructor(public enabled: boolean,
              public pois: Map<string, PoiPreference>) {
  }
}

export class PoiPreferences {

  constructor(readonly groups: Map<string, PoiGroupPreference>,
              public enabled: boolean) {
  }

  poi(poiName: string): PoiPreference {
    let result: PoiPreference = null;
    this.groups.forEach((group, groupName) => {
      group.pois.forEach((poi, poiNameKey) => {
        if (poiNameKey == poiName) {
          result = poi;
        }
      });
    });
    return result;
  }

}
