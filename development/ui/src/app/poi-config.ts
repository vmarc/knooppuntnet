import {List} from "immutable";

export class PoiConfigPoi {
  constructor(readonly minLevel: number) {
  }
}

export class PoiConfigGroup {
  constructor(readonly enabled: boolean,
              readonly pois: List<PoiConfigPoi>
  ) {
  }
}

export class PoiConfig {
  constructor(readonly enabled: boolean,
              readonly groups: List<PoiConfigGroup>) {
  }
}
