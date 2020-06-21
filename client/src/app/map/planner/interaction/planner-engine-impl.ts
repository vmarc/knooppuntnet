import {List} from "immutable";
import {Feature} from "ol";
import {Coordinate} from "ol/coordinate";
import {Point} from "ol/geom";
import {LineString} from "ol/geom";
import GeometryLayout from "ol/geom/GeometryLayout";
import GeometryType from "ol/geom/GeometryType";
import RenderFeature from "ol/render/Feature";
import {NodeClick} from "../../../components/ol/domain/node-click";
import {PoiClick} from "../../../components/ol/domain/poi-click";
import {PoiId} from "../../../components/ol/domain/poi-id";
import {RouteClick} from "../../../components/ol/domain/route-click";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {PlannerCommandAddLeg} from "../commands/planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "../commands/planner-command-add-start-point";
import {PlannerCommandMoveEndPoint} from "../commands/planner-command-move-end-point";
import {PlannerCommandMoveFirstLegSource} from "../commands/planner-command-move-first-leg-source";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerCommandMoveViaPoint} from "../commands/planner-command-move-via-point";
import {PlannerCommandMoveViaPointToViaRoute} from "../commands/planner-command-move-via-point-to-via-route";
import {PlannerCommandRemoveViaPoint} from "../commands/planner-command-remove-via-point";
import {PlannerCommandSplitLeg} from "../commands/planner-command-split-leg";
import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {FlagFeature} from "../features/flag-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {RouteFeature} from "../features/route-feature";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {Features} from "./features";
import {PlannerDragFlag} from "./planner-drag-flag";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";
import {PlannerDragLeg} from "./planner-drag-leg";
import {PlannerEngine} from "./planner-engine";

export class PlannerEngineImpl implements PlannerEngine {

  private legDrag: PlannerDragLeg = null;
  private nodeDrag: PlannerDragFlag = null;

  constructor(private context: PlannerContext) {
  }

  handleDownEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {

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
      this.nodeSelected(networkNode);
      return true;
    }

    const leg = Features.findLeg(features);
    if (leg != null) {
      if (this.legDragStarted(leg.id, coordinate)) {
        return true;
      }
    }

    const node = Features.findNode(features);
    if (node != null) {
      this.context.overlay.nodeClicked(new NodeClick(coordinate, node));
      return true;
    }

    const route = Features.findRoute(features);
    if (route != null) {
      this.context.overlay.routeClicked(new RouteClick(coordinate, route));
      return true;
    }

    const poiFeature = Features.findPoi(features);
    if (poiFeature != null) {
      this.context.overlay.poiClicked(new PoiClick(poiFeature.coordinate, new PoiId(poiFeature.poiType, +poiFeature.poiId)));
      return true;
    }

    return false;
  }

  handleMoveEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {

    if (features.isEmpty()) {
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

    const node = Features.findNode(features);
    if (node != null) {
      this.context.cursor.setStylePointer();
      return true;
    }

    const route = Features.findRoute(features);
    if (route != null) {
      this.context.cursor.setStylePointer();
      return true;
    }

    this.context.cursor.setStyleDefault();

    return false;
  }

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {

    if (this.isDraggingNode()) {

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.routeLayer.updateFlagCoordinate(this.nodeDrag.oldNode.featureId, networkNodeFeature.node.coordinate);
        this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
        return true;
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        this.highlightRoute(routeFeature);
      } else {
        this.context.highlightLayer.reset();
      }

      this.context.routeLayer.updateFlagCoordinate(this.nodeDrag.oldNode.featureId, coordinate);
      this.context.elasticBand.updatePosition(coordinate);
      return true;
    }

    if (this.isDraggingLeg()) {

      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
        return true;
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        this.highlightRoute(routeFeature);
      } else {
        this.context.highlightLayer.reset();
      }

      this.context.elasticBand.updatePosition(coordinate);
      return true;
    }

    return false;
  }

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate, singleClick: boolean): boolean {

    this.context.highlightLayer.reset();

    if (this.isViaFlagClicked(singleClick)) {
      this.removeViaPoint();
      this.dragCancel();
      return true;
    }

    if (this.isDraggingLeg() || this.isDraggingNode()) {
      this.context.cursor.setStyleDefault();
      this.context.elasticBand.setInvisible();
      const networkNode = Features.findNetworkNode(features);
      if (networkNode != null) {
        if (this.isDraggingLeg()) {
          this.dropLegOnNode(networkNode.node);
        } else if (this.isDraggingNode()) {
          this.dropNodeOnNode(networkNode.node);
        }
        return true;
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        if (this.isDraggingLeg()) {
          this.dropLegOnRoute(routeFeature, coordinate);
        } else if (this.isDraggingNode()) {
          this.dropNodeOnRoute(routeFeature, coordinate);
        }
        return true;
      }

      if (this.isDraggingNode()) {
        // cancel drag - put flag at its original coordinate again
        this.context.routeLayer.updateFlagCoordinate(this.nodeDrag.oldNode.featureId, this.nodeDrag.oldNode.coordinate);
      }

      this.dragCancel();
    }

    return false;
  }

  private highlightNode(node: PlanNode): void {
    const point = new Point(node.coordinate);
    const feature = new Feature(point);
    this.context.highlightLayer.highlightFeature(feature);
  }

  private highlightRoute(routeFeature: RouteFeature): void {
    if (routeFeature.feature instanceof RenderFeature) {
      const renderFeature = routeFeature.feature as RenderFeature;
      const geometryType = renderFeature.getType();
      if (geometryType === GeometryType.LINE_STRING) {
        const coordinates: number[] = renderFeature.getOrientedFlatCoordinates();
        const lineString = new LineString(coordinates, GeometryLayout.XY);
        const feature = new Feature(lineString);
        this.context.highlightLayer.highlightFeature(feature);
      } else {
        console.log("OTHER GEOMETRY TYPE " + geometryType);
      }
    }
  }

  private removeViaPoint(): void {

    const legs = this.context.plan.legs;
    const nextLegIndex = legs.findIndex(leg => leg.source.featureId === this.nodeDrag.oldNode.featureId);
    const oldLeg1 = legs.get(nextLegIndex - 1);
    const oldLeg2 = legs.get(nextLegIndex);

    const newLeg: PlanLeg = this.context.buildLeg(FeatureId.next(), oldLeg1.source, oldLeg2.sink, null);

    const command = new PlannerCommandRemoveViaPoint(
      oldLeg1.featureId,
      oldLeg2.featureId,
      newLeg.featureId
    );
    this.context.execute(command);
  }

  private nodeSelected(networkNode: NetworkNodeFeature): void {
    if (this.context.plan.source === null) {
      const command = new PlannerCommandAddStartPoint(networkNode.node);
      this.context.execute(command);
    } else {
      const source: PlanNode = this.context.plan.sink;
      const leg = this.context.buildLeg(FeatureId.next(), source, networkNode.node, null);
      const command = new PlannerCommandAddLeg(leg.featureId);
      this.context.execute(command);
    }
  }

  private legDragStarted(legId: string, coordinate: Coordinate): boolean {
    const leg = this.context.legs.getById(legId);
    if (leg) {
      const anchor1 = leg.source.coordinate;
      const anchor2 = leg.sink.coordinate;
      this.legDrag = new PlannerDragLeg(legId, anchor1, anchor2);
      this.context.elasticBand.set(anchor1, anchor2, coordinate);
      return true;
    }
    return false;
  }

  private flagDragStarted(flag: FlagFeature, coordinate: Coordinate): boolean {

    this.nodeDrag = new PlannerDragFlagAnalyzer(this.context.plan).dragStarted(flag);
    if (this.nodeDrag !== null) {
      this.context.routeLayer.updateFlagCoordinate(this.nodeDrag.oldNode.featureId, coordinate);
      this.context.elasticBand.set(this.nodeDrag.anchor1, this.nodeDrag.anchor2, coordinate);
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

  private isViaFlagClicked(singleClick: boolean): boolean {
    return singleClick === true && this.nodeDrag !== null
      && this.nodeDrag.oldNode.featureId !== this.context.plan.sink.featureId
      && this.nodeDrag.oldNode.featureId !== this.context.plan.source.featureId;
  }

  private dropLegOnNode(connection: PlanNode): void {
    if (this.legDrag !== null) {
      const oldLeg = this.context.legs.getById(this.legDrag.oldLegId);
      if (oldLeg) {
        const newLeg1 = this.context.buildLeg(FeatureId.next(), oldLeg.source, connection, null);
        const newLeg2 = this.context.buildLeg(FeatureId.next(), connection, oldLeg.sink, null);
        const command = new PlannerCommandSplitLeg(oldLeg.featureId, newLeg1.featureId, newLeg2.featureId);
        this.context.execute(command);
      }
      this.legDrag = null;
    }
  }

  private dropNodeOnNode(newNode: PlanNode): void {

    if (this.nodeDrag.flagType === PlanFlagType.Start) {
      if (this.context.plan.legs.isEmpty()) {
        const command = new PlannerCommandMoveStartPoint(this.nodeDrag.oldNode, newNode);
        this.context.execute(command);
      } else {
        const oldFirstLeg: PlanLeg = this.context.plan.legs.first();
        const newFirstLeg: PlanLeg = this.context.buildLeg(FeatureId.next(), newNode, oldFirstLeg.sink, null);
        const command = new PlannerCommandMoveFirstLegSource(oldFirstLeg.featureId, newFirstLeg.featureId);
        this.context.execute(command);
      }
    } else { // end node
      const oldLastLeg: PlanLeg = this.context.plan.legs.last();
      if (this.nodeDrag.oldNode.featureId === oldLastLeg.sink.featureId) {
        const newLastLeg: PlanLeg = this.context.buildLeg(FeatureId.next(), oldLastLeg.source, newNode, null);
        const command = new PlannerCommandMoveEndPoint(oldLastLeg.featureId, newLastLeg.featureId);
        this.context.execute(command);
      } else {

        const legs = this.context.plan.legs;
        const nextLegIndex = legs.findIndex(leg => leg.featureId === this.nodeDrag.legFeatureId);
        const oldLeg1 = legs.get(nextLegIndex - 1);
        const oldLeg2 = legs.get(nextLegIndex);

        const newLeg1: PlanLeg = this.context.buildLeg(FeatureId.next(), oldLeg1.source, newNode, null);
        const newLeg2: PlanLeg = this.context.buildLeg(FeatureId.next(), newNode, oldLeg2.sink, null);

        const command = new PlannerCommandMoveViaPoint(
          oldLeg1.featureId,
          oldLeg2.featureId,
          newLeg1.featureId,
          newLeg2.featureId
        );
        this.context.execute(command);
      }
    }

    this.nodeDrag = null;
  }

  private dropLegOnRoute(routeFeature: RouteFeature, coordinate: Coordinate) {
    console.log("drop leg on routeFeature id=" + routeFeature.feature.get("id"));
  }

  private dropNodeOnRoute(routeFeature, coordinate: Coordinate) {

    if (this.nodeDrag.flagType === PlanFlagType.Via) {
      const legs = this.context.plan.legs;

      const nextLegIndex = legs.findIndex(leg => leg.source.featureId === this.nodeDrag.oldNode.featureId);
      const oldLeg1 = legs.get(nextLegIndex - 1);
      const oldLeg2 = legs.get(nextLegIndex);

      const routeFeatureId = routeFeature.feature.get("id");
      const idParts = routeFeatureId.split("-");
      const viaRoute = new ViaRoute(idParts[0], idParts[1]);

      const newLeg: PlanLeg = this.context.buildLeg(FeatureId.next(), oldLeg1.source, oldLeg2.sink, viaRoute);

      const command = new PlannerCommandMoveViaPointToViaRoute(
        oldLeg1.featureId,
        oldLeg2.featureId,
        newLeg.featureId,
        viaRoute,
        coordinate
      );

      this.context.execute(command);
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
  }

}
