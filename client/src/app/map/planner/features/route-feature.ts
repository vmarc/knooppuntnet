import {FeatureLike} from "ol/Feature";

export class RouteFeature {
  constructor(readonly routeId: number,
              readonly routeName: string,
              readonly feature: FeatureLike) {
  }
}
