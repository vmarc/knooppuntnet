import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {NodeClick} from "../../../components/ol/domain/node-click";
import {PoiClick} from "../../../components/ol/domain/poi-click";
import {PoiId} from "../../../components/ol/domain/poi-id";
import {RouteClick} from "../../../components/ol/domain/route-click";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerCommandAddStartPoint} from "../commands/planner-command-add-start-point";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {FlagFeature} from "../features/flag-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {RouteFeature} from "../features/route-feature";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {DropEndNodeOnRoute} from "./actions/drop-end-node-on-route";
import {DropViaNodeOnRoute} from "./actions/drop-via-node-on-route";
import {DropViaRouteOnRoute} from "./actions/drop-via-route-on-route";
import {MoveFirstLegSource} from "./actions/move-first-leg-source";
import {MoveNodeViaPointToNode} from "./actions/move-node-via-point-to-node";
import {MoveRouteViaPointToNode} from "./actions/move-route-via-point-to-node";
import {Features} from "./features";
import {PlannerDragFlag} from "./planner-drag-flag";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";
import {PlannerDragLeg} from "./planner-drag-leg";
import {PlannerDragViaRouteFlag} from "./planner-drag-via-route-flag";
import {PlannerDragViaRouteFlagAnalyzer} from "./planner-drag-via-route-flag-analyzer";
import {PlannerEngine} from "./planner-engine";
import {RemoveViaLegRouteViaPoint} from "./actions/remove-via-leg-route-via-point";
import {RemoveEndLegRouteViaPoint} from "./actions/remove-end-leg-route-via-point";
import {AddLeg} from "./actions/add-leg";
import {DropLegOnNode} from "./actions/drop-leg-on-node";
import {MoveEndPoint} from "./actions/move-end-point";
import {AddViaRouteLeg} from "./actions/add-via-route-leg";
import {RemoveViaPoint} from "./actions/remove-via-point";

export class PlannerEngineImpl implements PlannerEngine {

  private legDrag: PlannerDragLeg = null;
  private nodeDrag: PlannerDragFlag = null;
  private viaRouteDrag: PlannerDragViaRouteFlag = null;

  constructor(private context: PlannerContext) {
  }

  handleDownEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean {

    if (features.isEmpty()) {
      this.context.closeOverlay();
      return false;
    }

    const flag = Features.findFlag(features);
    if (flag != null) {
      if (this.flagDragStarted(flag, coordinate)) {
        return true;
      }
    }

    const networkNode = Features.findNetworkNode(features);
    if (networkNode != null) {
      if (modifierKeyOnly) {
        this.context.overlay.nodeClicked(new NodeClick(coordinate, networkNode));
      } else {
        this.nodeSelected(networkNode);
      }
      return true;
    }

    const leg = Features.findLeg(features);
    if (leg != null) {
      if (this.legDragStarted(leg.id, coordinate)) {
        return true;
      }
    }


    if (modifierKeyOnly === true) {
      const route = Features.findRoute(features);
      if (route != null) {
        this.context.overlay.routeClicked(new RouteClick(coordinate, route));
        return true;
      }
    } else if (this.context.plan.sourceNode !== null) {
      const routes = Features.findRoutes(features);
      if (!routes.isEmpty()) {
        new AddViaRouteLeg(this.context).add(routes, coordinate);
        return true;
      }
    }

    const poiFeature = Features.findPoi(features);
    if (poiFeature != null) {
      this.context.overlay.poiClicked(new PoiClick(poiFeature.coordinate, new PoiId(poiFeature.poiType, +poiFeature.poiId)));
      return true;
    }

    return false;
  }

  handleMoveEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean {

    if (features.isEmpty()) {
      this.context.highlighter.reset();
      this.context.cursor.setStyleDefault();
      return false;
    }

    const flagFeature = Features.findFlag(features);
    if (!!flagFeature) {
      this.context.cursor.setStyleGrab();
      return true;
    }

    const networkNodeFeature = Features.findNetworkNode(features);
    if (networkNodeFeature != null) {
      this.context.highlighter.highlightNode(networkNodeFeature.node);
      this.context.cursor.setStylePointer();
      return true;
    }

    const leg = Features.findLeg(features);
    if (leg != null) {
      this.context.cursor.setStyleGrabbing();
      return true;
    }

    const poiFeature = Features.findPoi(features);
    if (poiFeature != null) {
      this.context.cursor.setStylePointer();
      return true;
    }

    const route = Features.findRoute(features);
    if (route != null) {
      if (modifierKeyOnly || this.context.plan.sourceNode !== null) { // no clicking routes when start node has not been selected yet
        this.context.cursor.setStylePointer();
        this.context.highlighter.highlightRoute(route);
        return true;
      }
    }

    this.context.highlighter.reset();
    this.context.cursor.setStyleDefault();

    return false;
  }

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean {

    if (this.isDraggingNode()) {

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.context.highlighter.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, networkNodeFeature.node.coordinate);
        this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
        return true;
      }

      if (!this.isDraggingStartNode()) {
        const routeFeature = Features.findRoute(features);
        if (routeFeature != null) {
          this.context.highlighter.highlightRoute(routeFeature);
        } else {
          this.context.highlighter.reset();
        }
      }

      this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.updatePosition(coordinate);
      return true;
    }

    if (this.isDraggingViaRouteFlag()) {

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.context.highlighter.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.markerLayer.updateFlagCoordinate(this.viaRouteDrag.planFlag.featureId, networkNodeFeature.node.coordinate);
        this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
        return true;
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        this.context.highlighter.highlightRoute(routeFeature);
        this.context.markerLayer.updateFlagCoordinate(this.viaRouteDrag.planFlag.featureId, coordinate);
        this.context.elasticBand.updatePosition(coordinate);
        return true;
      }

      this.context.highlighter.reset();
      this.context.markerLayer.updateFlagCoordinate(this.viaRouteDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.updatePosition(coordinate);
      return true;
    }

    if (this.isDraggingLeg()) {

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.context.highlighter.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
        return true;
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        this.context.highlighter.highlightRoute(routeFeature);
      } else {
        this.context.highlighter.reset();
      }
      this.context.elasticBand.updatePosition(coordinate);
      return true;
    }

    return false;
  }

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate, singleClick: boolean, modifierKeyOnly: boolean): boolean {

    this.context.highlighter.reset();

    if (this.isViaFlagClicked(singleClick)) {
      new RemoveViaPoint(this.context).remove(this.nodeDrag);
      this.dragCancel();
      return true;
    }

    if (this.isViaRouteFlagClicked(singleClick)) {
      this.removeRouteViaPoint();
      this.dragCancel();
      return true;
    }

    if (this.isDraggingLeg() || this.isDraggingNode()) {

      this.context.cursor.setStyleDefault();
      this.context.elasticBand.setInvisible();
      const networkNode = Features.findNetworkNode(features);
      if (networkNode != null) {
        if (this.isDraggingLeg()) {
          new DropLegOnNode(this.context).drop(this.legDrag, networkNode.node);
          this.legDrag = null;
        } else if (this.isDraggingNode()) {
          this.dropNodeOnNode(networkNode.node);
        }
        return true;
      }

      if (!this.isDraggingStartNode()) {
        const routeFeatures = Features.findRoutes(features);
        if (!routeFeatures.isEmpty()) {
          if (this.isDraggingLeg()) {
            this.dropLegOnRoute(routeFeatures, coordinate);
          } else if (this.isDraggingNode()) {
            this.dropNodeOnRoute(routeFeatures, coordinate);
          }
          return true;
        }
      }

      if (this.isDraggingNode()) {
        // cancel drag - put flag at its original coordinate again
        this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, this.nodeDrag.planFlag.coordinate);
      }

      this.dragCancel();
    }

    if (this.isDraggingViaRouteFlag()) {

      this.context.cursor.setStyleDefault();
      this.context.elasticBand.setInvisible();

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        const oldLeg = this.context.plan.legs.find(leg => leg.featureId === this.viaRouteDrag.legFeatureId);
        if (oldLeg) {
          new MoveRouteViaPointToNode(this.context).move(networkNodeFeature.node, oldLeg);
        }
        return true;
      }

      const routeFeatures = Features.findRoutes(features);
      if (!routeFeatures.isEmpty()) {
        const oldLeg = this.context.plan.legs.find(leg => leg.featureId === this.viaRouteDrag.legFeatureId);
        if (oldLeg) {
          new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
        }
        return true;
      }

      return true;
    }

    return false;
  }

  handleMouseLeave(): void {
    this.context.highlighter.reset();
  }

  private removeRouteViaPoint(): void {
    const clickedLeg = this.context.plan.legs.find(leg => leg.featureId === this.viaRouteDrag.legFeatureId);
    if (clickedLeg != null) {
      if (clickedLeg.sinkFlag.flagType === PlanFlagType.End) {
        new RemoveEndLegRouteViaPoint(this.context).remove(clickedLeg);
      } else {
        new RemoveViaLegRouteViaPoint(this.context).remove(clickedLeg);
      }
    }
  }

  private nodeSelected(networkNode: NetworkNodeFeature): void {
    if (this.context.plan.sourceNode === null) {
      this.addStartPoint(networkNode.node);
    } else {
      new AddLeg(this.context).add(networkNode.node);
    }
  }

  private addStartPoint(planNode: PlanNode): void {
    const sourceFlag = PlanFlag.start(FeatureId.next(), planNode.coordinate);
    const command = new PlannerCommandAddStartPoint(planNode, sourceFlag);
    this.context.execute(command);
  }

  private legDragStarted(legId: string, coordinate: Coordinate): boolean {
    const leg = this.context.plan.legs.find(leg => leg.featureId === legId);
    if (leg) {
      const anchor1 = leg.sourceNode.coordinate;
      const anchor2 = leg.sinkNode.coordinate;
      this.legDrag = new PlannerDragLeg(legId, anchor1, anchor2);
      this.context.elasticBand.set(anchor1, anchor2, coordinate);
      return true;
    }
    return false;
  }

  private flagDragStarted(flag: FlagFeature, coordinate: Coordinate): boolean {

    this.nodeDrag = new PlannerDragFlagAnalyzer(this.context.plan).dragStarted(flag);
    if (this.nodeDrag !== null) {
      this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.set(this.nodeDrag.anchor1, this.nodeDrag.anchor2, coordinate);
      return true;
    }

    this.viaRouteDrag = new PlannerDragViaRouteFlagAnalyzer(this.context.plan).dragStarted(flag);
    if (this.viaRouteDrag !== null) {
      this.context.markerLayer.updateFlagCoordinate(this.viaRouteDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.set(this.viaRouteDrag.anchor1, this.viaRouteDrag.anchor2, coordinate);
      return true;
    }

    return false;
  }

  private isDraggingLeg(): boolean {
    return this.legDrag !== null;
  }

  private isDraggingNode(): boolean {
    return this.nodeDrag !== null;
  }

  private isDraggingStartNode(): boolean {
    return this.nodeDrag !== null && this.nodeDrag.planFlag.flagType === PlanFlagType.Start;
  }

  private isDraggingViaRouteFlag(): boolean {
    return this.viaRouteDrag !== null;
  }

  private isViaFlagClicked(singleClick: boolean): boolean {
    return singleClick === true && this.nodeDrag !== null && (this.nodeDrag.planFlag.flagType === PlanFlagType.Via);
  }

  private isViaRouteFlagClicked(singleClick: boolean): boolean {
    return singleClick === true && this.viaRouteDrag !== null;
  }

  private dropNodeOnNode(targetNode: PlanNode): void {

    if (this.nodeDrag.planFlag.flagType === PlanFlagType.Start) {
      if (this.context.plan.legs.isEmpty()) {
        this.moveStartPoint(targetNode);
      } else {
        new MoveFirstLegSource(this.context).move(targetNode);
      }
    } else if (this.nodeDrag.planFlag.flagType === PlanFlagType.End) {
      new MoveEndPoint(this.context).move(targetNode);
    } else {
      this.moveViaPoint(targetNode);
    }

    this.nodeDrag = null;
  }

  private moveStartPoint(newSourceNode: PlanNode): void {
    const command = new PlannerCommandMoveStartPoint(this.nodeDrag.oldNode, newSourceNode);
    this.context.execute(command);
  }

  private moveViaPoint(targetNode: PlanNode): void {
    const legs = this.context.plan.legs;
    const legIndex1 = legs.findIndex(leg => leg.sinkFlag.featureId === this.nodeDrag.planFlag.featureId);
    if (legIndex1 >= 0) {
      new MoveNodeViaPointToNode(this.context).move(targetNode, legIndex1);
    } else {
      const viaLeg = legs.find(leg => this.nodeDrag.planFlag.featureId === leg.viaFlag?.featureId);
      if (viaLeg) {
        new MoveRouteViaPointToNode(this.context).move(targetNode, viaLeg);
      }
    }
  }

  private dropLegOnRoute(routeFeatures: List<RouteFeature>, coordinate: Coordinate) {
    const oldLeg = this.context.plan.legs.find(leg => leg.featureId === this.legDrag.oldLegId);
    if (oldLeg) {
      new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
    }
  }

  private dropNodeOnRoute(routeFeatures: List<RouteFeature>, coordinate: Coordinate) {
    if (this.nodeDrag.planFlag.flagType === PlanFlagType.Via) {
      if (this.nodeDrag.oldNode === null) {
        const oldLeg = this.context.plan.legs.find(leg => this.nodeDrag.planFlag.featureId === leg.viaFlag?.featureId);
        if (oldLeg) {
          new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
        }
      } else {
        new DropViaNodeOnRoute(this.context).drop(this.nodeDrag, routeFeatures, coordinate);
      }
    } else if (this.nodeDrag.planFlag.flagType === PlanFlagType.End) {
      new DropEndNodeOnRoute(this.context).drop(this.nodeDrag, routeFeatures, coordinate);
    }
  }

  private dragCancel(): void {

    if (this.legDrag !== null) {
      this.context.elasticBand.setInvisible();
      this.legDrag = null;
    }

    if (this.nodeDrag !== null) {
      this.context.elasticBand.setInvisible();
      this.nodeDrag = null;
    }

    if (this.viaRouteDrag !== null) {
      this.context.elasticBand.setInvisible();
      this.viaRouteDrag = null;
    }
  }

}
