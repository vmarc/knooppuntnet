import Map from 'ol/Map';
import MapBrowserEvent from 'ol/events'
import PointerInteraction from 'ol/interaction/Pointer';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {PlannerEngine} from "./planner-engine";
import {PlannerContext} from "./planner-context";

export class PlannerInteraction {

  viewPort: HTMLElement;

  constructor(private context: PlannerContext,
              private engine: PlannerEngine) {
  }

  private interaction = new PointerInteraction({
    handleDownEvent: (evt: MapBrowserEvent) => {

      const tolerance = 20;
      const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
      if (features !== null) {
        const legNode = this.findDraggableLegNode(features);
        if (legNode !== null) {
          console.log("DEBUG moving over leg node " + legNode.getId());
          const nodeId = legNode.get("id");
          if (this.engine.legNodeDragStarted(legNode.getId(), nodeId, evt.coordinate)) {
            return true;
          }
        }

        const networkNode: Feature = this.findNetworkNode(features);
        if (networkNode !== null) {
          const nodeId = networkNode.get("id");
          const nodeName = networkNode.get("name");
          const point: Point = networkNode.getGeometry() as Point;
          this.engine.nodeSelected(nodeId, nodeName, point.getCoordinates());
          this.context.crosshairLayer.updatePosition(point.getCoordinates());
        } else {
          const leg = this.findLeg(features);
          if (leg !== null) {
            if (this.engine.legDragStarted(leg.getId(), evt.coordinate)) {
              return true;
            }
          }
          this.context.crosshairLayer.updatePosition(evt.coordinate);
        }
      } else {
        this.context.crosshairLayer.updatePosition(evt.coordinate);
      }

      return false;
    },
    handleMoveEvent: (evt: MapBrowserEvent) => {
      const tolerance = 20;
      const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
      if (features !== null) {
        const legNode = this.findDraggableLegNode(features);
        if (legNode !== null) {
          console.log("DEBUG moving over leg node " + legNode.getId());
          this.context.crosshairLayer.setVisible(false);
          this.viewPort.style.cursor = "move";
        } else {
          const networkNode: Feature = this.findNetworkNode(features);
          if (networkNode !== null) {
            const point: Point = networkNode.getGeometry() as Point;
            this.context.crosshairLayer.setVisible(true);
            this.viewPort.style.cursor = "default";
            this.context.crosshairLayer.updatePosition(point.getCoordinates());
          } else {
            const leg = this.findLeg(features);
            if (leg !== null) {
              console.log("DEBUG moving over leg " + leg.getId());
              this.context.crosshairLayer.setVisible(false);
              this.viewPort.style.cursor = "move";
            } else {
              this.context.crosshairLayer.setVisible(true);
              this.viewPort.style.cursor = "default";
              this.context.crosshairLayer.updatePosition(evt.coordinate);
            }
          }
        }
      } else {
        this.context.crosshairLayer.setVisible(true);
        this.viewPort.style.cursor = "default";
        this.context.crosshairLayer.updatePosition(evt.coordinate);
      }

      return true;
    },
    handleDragEvent: (evt: MapBrowserEvent) => {
      this.context.routeLayer.updateDoubleElasticBandPosition(evt.coordinate);
      return true;
    },
    handleUpEvent: (evt: MapBrowserEvent) => {

      if (this.engine.isDraggingLeg() || this.engine.isDraggingNode()) {
        this.viewPort.style.cursor = "default";
        const tolerance = 20;
        const features = evt.map.getFeaturesAtPixel(evt.pixel, tolerance);
        if (features !== null) {
          const networkNode: Feature = this.findNetworkNode(features);
          if (networkNode !== null) {

            const point: Point = networkNode.getGeometry() as Point;
            const nodeId = networkNode.get("id");
            const nodeName = networkNode.get("name");

            console.log("DEBUG PlannerInteraction handleUpEvent nodeId=" + nodeId + ", nodeName=" + nodeName);

            if (nodeId) {
              if (this.engine.isDraggingLeg()) {
                this.engine.endDragLeg(nodeId, nodeName, point.getCoordinates());
              } else if (this.engine.isDraggingNode()) {
                this.engine.endDragNode(nodeId, nodeName, point.getCoordinates());
              }
            }
          }
        }
      } else {
        this.engine.dragCancel();
      }

      this.context.crosshairLayer.setVisible(true);

      console.log("DEBUG PlannerInteraction handleUpEvent einde");

      return true;
    }
  });

  addToMap(map: Map) {
    map.addInteraction(this.interaction);
    this.viewPort = map.getViewport();
    map.getViewport().addEventListener("mouseout", (e) => this.handleMouseOut(e));
    map.getViewport().addEventListener("mouseenter", (e) => this.handleMouseEnter(e));
  }

  private handleMouseOut(event: MouseEvent) {
    this.context.crosshairLayer.setVisible(false);
  }

  private handleMouseEnter(event: MouseEvent) {
    this.context.crosshairLayer.setVisible(true);
  }

  private findNetworkNode(features: Array<Feature>): Feature {
    const nodes = features.filter(f => {
      const layer = f.get("layer");
      return layer && layer.endsWith("node") && layer !== "leg-node";
    });
    if (nodes.length == 0) {
      return null;
    }
    return nodes[0]; // TODO find the closest
  }

  private findLeg(features: Array<Feature>): Feature {
    const legs = features.filter(f => {
      const layer = f.get("layer");
      return layer == "leg";
    });
    if (legs.length == 0) {
      return null;
    }
    return legs[0]; // TODO find the closest
  }

  private findDraggableLegNode(features: Array<Feature>): Feature {

    if (this.context.plan.legs.isEmpty()) {
      return null;
    }

    const nodes = features.filter(f => {
      const layer = f.get("layer");
      return layer == "leg-node";
    });

    if (nodes.length == 0) {
      return null;
    }
    return nodes[0]; // TODO find the closest
  }

}
