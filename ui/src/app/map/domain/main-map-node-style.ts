import Style from 'ol/style/Style';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Text from 'ol/style/Text';
import Stroke from 'ol/style/Stroke';
import {MainStyleColors} from "./main-style-colors";
import {MapState} from "./map-state";

export class MainMapNodeStyle {

  private readonly largeMinZoomLevel = 13;
  private readonly yellow /*ol.Color*/ = [255, 255, 0];
  private readonly white /*ol.Color*/ = [255, 255, 255];

  private readonly smallNodeSelectedStyle = new Style({
    image: new Circle({
      radius: 8,
      fill: new Fill({
        color: this.yellow
      })
    })
  });

  private readonly largeNodeSelectedStyle = new Style({
    image: new Circle({
      radius: 20,
      fill: new Fill({
        color: this.yellow
      })
    })
  });

  private readonly largeNodeStyle = new Style({
    image: new Circle({
      radius: 14,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.green,
        width: 3
      })
    }),
    text: new Text({
      text: "",
      textAlign: "center",
      textBaseline: "middle",
      font: "14px Arial, Verdana, Helvetica, sans-serif",
      stroke: new Stroke({
        color: this.white,
        width: 5
      })
    })
  });

  private readonly smallNodeStyle = new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.green,
        width: 2
      })
    })
  });

  private readonly smallNodeStyleError = new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.blue,
        width: 2
      })
    })
  });

  private readonly smallNodeStyleDisabled = new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.gray,
        width: 2
      })
    })
  });

  private readonly smallNodeStyleOrphan = new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.darkGreen,
        width: 2
      })
    })
  });

  private readonly smallNodeStyleErrorOrphan = new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: this.white
      }),
      stroke: new Stroke({
        color: MainStyleColors.darkBlue,
        width: 2
      })
    })
  });

  public createNodeStyle(state: MapState, zoom: number, feature /*: ol.render.Feature*/, enabled: boolean): Style[] {

    const featureId = feature.get("id");
    const layer = feature.get("layer");

    let selectedStyle = null;
    if (state.selectedNodeId && featureId && feature.get("id") == state.selectedNodeId) {
      if (zoom >= this.largeMinZoomLevel) {
        selectedStyle = this.largeNodeSelectedStyle;
      }
      else {
        selectedStyle = this.smallNodeSelectedStyle;
      }
    }

    let style: Style = null;
    if (zoom >= this.largeMinZoomLevel) {
      let nodeColor = MainStyleColors.gray;
      if (enabled) {
        if ("error-node" == layer) {
          nodeColor = MainStyleColors.blue;
        }
        else if ("orphan-node" == layer) {
          nodeColor = MainStyleColors.darkGreen;
        }
        else if ("error-orphan-node" == layer) {
          nodeColor = MainStyleColors.darkBlue;
        }
        else {
          nodeColor = MainStyleColors.green;
        }
      }

      this.largeNodeStyle.getText().setText(feature.get("name"));
      this.largeNodeStyle.getImage().getStroke().setColor(nodeColor);

      if (state.highlightedNodeId && feature.get("id") == state.highlightedNodeId) {
        this.largeNodeStyle.getImage().getStroke().setWidth(5);
        this.largeNodeStyle.getImage().setRadius(16);
      }
      else {
        this.largeNodeStyle.getImage().getStroke().setWidth(3);
        this.largeNodeStyle.getImage().setRadius(14);
      }
      style = this.largeNodeStyle;
    }
    else {
      if (enabled) {
        if ("error-node" == layer) {
          style = this.smallNodeStyleError;
        }
        else if ("orphan-node" == layer) {
          style = this.smallNodeStyleOrphan;
        }
        else if ("error-orphan-node" == layer) {
          style = this.smallNodeStyleErrorOrphan;
        }
        else {
          style = this.smallNodeStyle;
        }
      }
      else {
        style = this.smallNodeStyleDisabled
      }
    }

    return selectedStyle ? [selectedStyle, style] : [style];
  }

}
