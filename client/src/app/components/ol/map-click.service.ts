import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {click} from "ol/events/condition";
import Feature from "ol/Feature";
import Interaction from 'ol/interaction/Interaction';
import Select from "ol/interaction/Select";
import SelectEvent from "ol/interaction/Select";
import Style from "ol/style/Style";

/*
   Navigates to node or route specific page when clicking on node or route
   in the node or route map.
 */
@Injectable()
export class MapClickService {

  constructor(private router: Router) {
  }

  createInteraction(): Interaction {
    const interaction = new Select({
      condition: click,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => this.handle(e));
    return interaction;
  }

  private handle(e: SelectEvent): boolean {
    for (let feature of e.selected) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        this.handleRouteClicked(feature);
      } else if (layer.endsWith("node")) {
        this.handleNodeClicked(feature);
      }
    }
    return true;
  }

  private handleRouteClicked(feature: Feature): void {
    const featureId = feature.get("id");
    const routeId = featureId.substring(0, featureId.indexOf("-"));
    this.router.navigateByUrl(`/analysis/route/${routeId}`);
  }

  private handleNodeClicked(feature: Feature): void {
    const nodeId = feature.get("id");
    this.router.navigateByUrl(`/analysis/node/${nodeId}`);
  }
}
