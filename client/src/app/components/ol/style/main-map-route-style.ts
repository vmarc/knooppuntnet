import {Color} from "ol/color";
import {FeatureLike} from "ol/Feature";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {MapMode} from "../services/map-mode";
import {MapService} from "../services/map.service";
import {MainStyleColors} from "./main-style-colors";
import {RouteStyle} from "./route-style";
import {SurveyDateStyle} from "./survey-date-style";

export class MainMapRouteStyle {

  private readonly routeStyleBuilder = new RouteStyle();
  private readonly defaultRouteSelectedStyle = this.initRouteSelectedStyle();
  private readonly surveyDateStyle: SurveyDateStyle;

  constructor(private mapService: MapService) {
    this.surveyDateStyle = new SurveyDateStyle(mapService);
  }

  public routeStyle(zoom: number, feature: FeatureLike): Array<Style> {
    const selectedStyle = this.determineRouteSelectedStyle(feature);
    const style = this.determineRouteStyle(feature, zoom);
    return selectedStyle ? [selectedStyle, style] : [style];
  }

  private determineRouteSelectedStyle(feature: FeatureLike): Style {
    const featureId = feature.get("id");
    let style = null;
    if (this.mapService.selectedRouteId && featureId && featureId.startsWith(this.mapService.selectedRouteId)) {
      style = this.defaultRouteSelectedStyle;
    }
    return style;
  }

  private determineRouteStyle(feature: FeatureLike, zoom: number): Style {
    const color = this.routeColor(feature);
    const highligthed = this.mapService.highlightedRouteId && feature.get("id").startsWith(this.mapService.highlightedRouteId);
    return this.routeStyleBuilder.style(color, zoom, highligthed);
  }

  private initRouteStyle() {
    return new Style({
      stroke: new Stroke({
        color: MainStyleColors.green,
        width: 1
      })
    });
  }

  private initRouteSelectedStyle(): Style {
    return new Style({
      stroke: new Stroke({
        color: MainStyleColors.yellow,
        width: 14
      })
    });
  }

  private routeColor(feature: FeatureLike): Color {
    let color = MainStyleColors.gray;
    if (this.mapService.mapMode === MapMode.surface) {
      color = this.routeColorSurface(feature);
    } else if (this.mapService.mapMode === MapMode.survey) {
      color = this.routeColorSurvey(feature);
    } else if (this.mapService.mapMode === MapMode.analysis) {
      color = this.routeColorAnalysis(feature);
    }
    return color;
  }

  private routeColorSurface(feature: FeatureLike): Color {
    const surface = feature.get("surface");
    let color = MainStyleColors.green;
    if ("unpaved" === surface) {
      color = MainStyleColors.orange;
    }
    return color;
  }

  private routeColorSurvey(feature: FeatureLike): Color {
    return this.surveyDateStyle.surveyColor(feature);
  }

  private routeColorAnalysis(feature: FeatureLike): Color {
    const layer = feature.get("layer");
    let color = MainStyleColors.gray;
    if ("route" === layer) {
      color = MainStyleColors.green;
    } else if ("orphan-route" === layer) {
      color = MainStyleColors.darkGreen;
    } else if ("incomplete-route" === layer) {
      color = MainStyleColors.red;
    } else if ("error-route" === layer) {
      color = MainStyleColors.orange;
    }
    return color;
  }
}
