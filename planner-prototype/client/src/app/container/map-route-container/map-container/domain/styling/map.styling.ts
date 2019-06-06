import Map from 'ol/Map';
import {MapNodeStyling} from "./map.node.styling";
import {MapRouteStyling} from "./map.route.styling";
import {MapState} from "../map.state";
import {Injectable} from "@angular/core";
import Feature from 'ol/Feature';

@Injectable()
export class MapStyling {

  private readonly MapNodeStyling;
  private readonly MapRouteStyling;

  constructor(private map: Map,
              private mapState: MapState) {
    this.MapNodeStyling = new MapNodeStyling(mapState);
    this.MapRouteStyling = new MapRouteStyling(mapState);
  }

  public styleFunction(features: Feature[]) {
    return (feature, resolution) => {
      const zoom = this.map.getView().getZoom();
      const layer = feature.get("layer");

      if (layer.includes("node")) {
        if (features.includes(feature)) {
          return MapNodeStyling.selectedNodeStyle(feature.values_.name);
        } else {
          return this.MapNodeStyling.nodeStyle(zoom, feature, true);
        }
      }
      return this.MapRouteStyling.routeStyle(zoom, feature, layer, true);
    };
  }
}
