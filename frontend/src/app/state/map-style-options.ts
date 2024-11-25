export interface MapStyleOptions {
  zoom: number;
  mode: string; // standard, surface, survey, analysis
  scopeInternational: boolean;
  scopeNational: boolean;
  scopeRegional: boolean;
  scopeLocal: boolean;
  scopeNodeRoutes: boolean;
  selectedRoute: number | undefined;
}
