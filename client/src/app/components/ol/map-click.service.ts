import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {click, pointerMove} from "ol/events/condition";
import Feature from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import Select from "ol/interaction/Select";
import SelectEvent from "ol/interaction/Select";
import Map from "ol/Map";
import Style from "ol/style/Style";

/*
   Navigates to node or route specific page when clicking on node or route
   in the node or route map.
 */
@Injectable()
export class MapClickService {

  constructor(private router: Router) {
  }

  installOn(map: Map): void {
    map.addInteraction(this.buildClickInteraction());
    map.addInteraction(this.buildMoveInteraction(map));
  }

  private buildClickInteraction(): Interaction {
    const interaction = new Select({
      condition: click,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => this.handleClick(e));
    return interaction;
  }

  private buildMoveInteraction(map: Map): Interaction {
    const interaction = new Select({
      condition: pointerMove,
      multi: false,
      style: new Style() // this overrides the normal openlayers default edit style
    });
    interaction.on("select", (e) => this.handleMove(e, map));
    return interaction;
  }

  private handleClick(e: SelectEvent): boolean {
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

  private handleMove(e: SelectEvent, map: Map): boolean {
    let cursorStyle = "cursor: default";
    if (this.isHooveringOverNodeOrRoute(e)) {
      cursorStyle = "cursor: pointer";
    }
    map.getTargetElement().setAttribute("style", cursorStyle);
    return true;
  }

  private isHooveringOverNodeOrRoute(e: SelectEvent): boolean {
    if (e) {
      for (let feature of e.selected) {
        const layer = feature.get("layer");
        if (!!layer && (layer.endsWith("route") || layer.endsWith("node"))) {
          return true;
        }
      }
    }
    return false;
  }

}
