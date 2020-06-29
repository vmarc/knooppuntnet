import {FeatureLike} from "ol/Feature";

export class RouteFeature {
  constructor(readonly routeId: number,
              readonly pathId: number,
              readonly routeName: string,
              readonly feature: FeatureLike) {
  }
}
