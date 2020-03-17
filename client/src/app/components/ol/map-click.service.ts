import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {MapBrowserEvent} from "ol";
import {FeatureLike} from "ol/Feature";
import Interaction from "ol/interaction/Interaction";
import PointerInteraction from "ol/interaction/Pointer";
import Map from "ol/Map";

/*
   Navigates to the node or route specific page when clicking on node or route in the map.
 */
@Injectable()
export class MapClickService {

  constructor(private router: Router) {
  }

  installOn(map: Map): void {
    map.addInteraction(this.buildInteraction());
  }

  private buildInteraction(): Interaction {
    return new PointerInteraction({
      handleDownEvent: (e) => this.handleDownEvent(e),
      handleMoveEvent: (e) => this.handleMoveEvent(e)
    });
  }

  private handleDownEvent(evt: MapBrowserEvent): boolean {
    const features = this.getFeatures(evt);
    const nodeFeature = this.findFeature(features, this.isNode);
    if (nodeFeature) {
      this.handleNodeClicked(nodeFeature);
      return true;
    }
    const routeFeature = this.findFeature(features, this.isRoute);
    if (routeFeature) {
      this.handleRouteClicked(routeFeature);
      return true;
    }
    return false;
  }

  private handleMoveEvent(evt: MapBrowserEvent): boolean {
    let cursorStyle = "default";
    if (this.isHooveringOverNodeOrRoute(evt)) {
      cursorStyle = "pointer";
    }
    evt.map.getTargetElement().style.cursor = cursorStyle;
    return true;
  }

  private getFeatures(evt: MapBrowserEvent): Array<FeatureLike> {
    return evt.map.getFeaturesAtPixel(evt.pixel, {hitTolerance: 10});
  }

  private findFeature(features: Array<FeatureLike>, predicate: (feature: FeatureLike) => boolean): FeatureLike {
    for (const feature of features) {
      if (predicate(feature)) {
        return feature;
      }
    }
    return null;
  }

  private handleRouteClicked(feature: FeatureLike): void {
    const featureId = feature.get("id");
    const routeName = feature.get("name");
    const routeId = featureId.substring(0, featureId.indexOf("-"));
    this.router.navigateByUrl(`/analysis/route/${routeId}`, {state: {routeName: routeName}});
  }

  private handleNodeClicked(feature: FeatureLike): void {
    const nodeId = feature.get("id");
    const nodeName = feature.get("name");
    this.router.navigateByUrl(`/analysis/node/${nodeId}`, {state: {nodeName: nodeName}});
  }

  private isHooveringOverNodeOrRoute(evt: MapBrowserEvent): boolean {
    const features = this.getFeatures(evt);
    if (features) {
      for (const feature of features) {
        if (this.isNode(feature) || this.isRoute(feature)) {
          return true;
        }
      }
    }
    return false;
  }

  private isNode(feature: FeatureLike): boolean {
    const layer = feature.get("layer");
    return layer && layer.endsWith("node");
  }

  private isRoute(feature: FeatureLike): boolean {
    const layer = feature.get("layer");
    return layer && layer.endsWith("route");
  }

}
