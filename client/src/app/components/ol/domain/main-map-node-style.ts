import {FeatureLike} from "ol/Feature";
import CircleStyle from "ol/style/Circle";
import Circle from "ol/style/Circle";
import Fill from "ol/style/Fill";
import Style from "ol/style/Style";
import {MapService} from "../map.service";
import {MainStyleColors} from "./main-style-colors";
import {NodeStyle} from "./node-style";

export class MainMapNodeStyle {

  private readonly largeMinZoomLevel = 13;
  private readonly smallNodeSelectedStyle = this.nodeSelectedStyle(8);
  private readonly largeNodeSelectedStyle = this.nodeSelectedStyle(20);
  private readonly smallNodeStyle = NodeStyle.smallNodeStyle();
  private readonly largeNodeStyle = NodeStyle.largeNodeStyle();

  constructor(private mapService: MapService) {
  }

  public nodeStyle(zoom: number, feature: FeatureLike, enabled: boolean): Array<Style> {

    const featureId = feature.get("id");
    const layer = feature.get("layer");
    const large = zoom >= this.largeMinZoomLevel;

    const selectedStyle = this.determineNodeSelectedStyle(featureId, large);
    const style = this.determineNodeMainStyle(feature, layer, enabled, large);

    return selectedStyle ? [selectedStyle, style] : [style];
  }

  private determineNodeSelectedStyle(featureId: string, large: boolean): Style {
    let style = null;
    if (this.mapService.selectedNodeId && featureId && featureId === this.mapService.selectedNodeId) {
      if (large) {
        style = this.largeNodeSelectedStyle;
      } else {
        style = this.smallNodeSelectedStyle;
      }
    }
    return style;
  }

  private determineNodeMainStyle(feature: FeatureLike, layer: string, enabled: boolean, large: boolean): Style {
    let style: Style = null;
    if (large) {
      style = this.determineLargeNodeStyle(feature, layer, enabled);
    } else {
      style = this.determineSmallNodeStyle(layer, enabled);
    }
    return style;
  }

  private determineLargeNodeStyle(feature: FeatureLike, layer: string, enabled: boolean): Style {

    const color = this.nodeColor(layer, enabled);

    const circleStyle: CircleStyle = this.largeNodeStyle.getImage() as CircleStyle;

    this.largeNodeStyle.getText().setText(feature.get("name"));
    circleStyle.getStroke().setColor(color);

    if (this.mapService.highlightedNodeId && feature.get("id") === this.mapService.highlightedNodeId) {
      circleStyle.getStroke().setWidth(5);
      circleStyle.setRadius(16);
    } else {
      circleStyle.getStroke().setWidth(3);
      circleStyle.setRadius(14);
    }
    return this.largeNodeStyle;
  }

  private determineSmallNodeStyle(layer: string, enabled: boolean): Style {
    const color = this.nodeColor(layer, enabled);
    const circleStyle: CircleStyle = this.smallNodeStyle.getImage() as CircleStyle;
    circleStyle.getStroke().setColor(color);
    return this.smallNodeStyle;
  }

  private nodeSelectedStyle(radius: number) {
    return new Style({
      image: new Circle({
        radius: radius,
        fill: new Fill({
          color: MainStyleColors.yellow
        })
      })
    });
  }

  private nodeColor(layer: string, enabled: boolean) {
    let nodeColor = MainStyleColors.gray;
    if (enabled) {
      if ("error-node" === layer) {
        nodeColor = MainStyleColors.blue;
      } else if ("orphan-node" === layer) {
        nodeColor = MainStyleColors.darkGreen;
      } else if ("error-orphan-node" === layer) {
        nodeColor = MainStyleColors.darkBlue;
      } else {
        nodeColor = MainStyleColors.green;
      }
    }
    return nodeColor;
  }

}
