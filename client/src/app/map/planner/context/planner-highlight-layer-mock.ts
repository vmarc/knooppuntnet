import Feature from "ol/Feature";
import {PlannerHighlightLayer} from "./planner-highlight-layer";

export class PlannerHighlightLayerMock implements PlannerHighlightLayer {

  feature: Feature = null;

  highlightFeature(feature: Feature): void {
    this.feature = feature;
  }

  reset(): void {
    this.feature = null;
  }

}
