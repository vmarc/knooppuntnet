import Feature from "ol/Feature";

export interface PlannerHighlightLayer {

  highlightFeature(feature: Feature): void;

  reset(): void;
}
