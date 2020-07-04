import {List} from "immutable";
import {Feature} from "ol";
import {Coordinate} from "ol/coordinate";
import {MultiLineString} from "ol/geom";
import {Point} from "ol/geom";
import {LineString} from "ol/geom";
import GeometryLayout from "ol/geom/GeometryLayout";
import GeometryType from "ol/geom/GeometryType";
import RenderFeature from "ol/render/Feature";
import {NodeClick} from "../../../components/ol/domain/node-click";
import {PoiClick} from "../../../components/ol/domain/poi-click";
import {PoiId} from "../../../components/ol/domain/poi-id";
import {RouteClick} from "../../../components/ol/domain/route-click";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerCommandAddLeg} from "../commands/planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "../commands/planner-command-add-start-point";
import {PlannerCommandMoveEndPoint} from "../commands/planner-command-move-end-point";
import {PlannerCommandMoveFirstLegSource} from "../commands/planner-command-move-first-leg-source";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerCommandMoveViaPoint} from "../commands/planner-command-move-via-point";
import {PlannerCommandRemoveViaPoint} from "../commands/planner-command-remove-via-point";
import {PlannerCommandSplitLeg} from "../commands/planner-command-split-leg";
import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {FlagFeature} from "../features/flag-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {RouteFeature} from "../features/route-feature";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {Features} from "./features";
import {PlannerDragFlag} from "./planner-drag-flag";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";
import {PlannerDragLeg} from "./planner-drag-leg";
import {PlannerEngine} from "./planner-engine";

export class PlannerEngineImpl implements PlannerEngine {

  private newInteractionToggle = false;

  private legDrag: PlannerDragLeg = null;
  private nodeDrag: PlannerDragFlag = null;

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

    if (modifierKeyOnly === true) {
      const route = Features.findRoute(features);
      if (route != null) {
        this.context.overlay.routeClicked(new RouteClick(coordinate, route));
        return true;
      }
    } else if (this.context.plan.sourceNode !== null) {
      if (this.newInteractionToggle) {
        const route = Features.findRoute(features);
        if (route != null) {
          this.routeSelected(route);
          return true;
        }
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
      this.context.highlightLayer.reset();
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

    if (this.context.plan.sourceNode !== null) { // no clicking routes when start node has not been selected yet
      if (this.newInteractionToggle) {
        const route = Features.findRoute(features);
        if (route != null) {
          this.context.cursor.setStylePointer();
          this.highlightRoute(route);
          return true;
        }
      }
    }

    this.context.highlightLayer.reset();
    this.context.cursor.setStyleDefault();

    return false;
  }

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean {

    // console.log("handleDrageEvent");

    if (this.isDraggingNode()) {


      // console.log("handleDrageEvent isDraggingNode is true");

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
        if (this.newInteractionToggle) {
          this.highlightRoute(routeFeature);
        }
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

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate, singleClick: boolean, modifierKeyOnly: boolean): boolean {

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
      } else if (geometryType === GeometryType.MULTI_LINE_STRING) {
        const coordinates: number[] = renderFeature.getOrientedFlatCoordinates();
        const ends: number[] = [];
        renderFeature.getEnds().forEach(num => {
          if (typeof num === "number") {
            ends.push(num);
          }
        });
        const lineString = new MultiLineString(coordinates, GeometryLayout.XY, ends);
        const feature = new Feature(lineString);
        this.context.highlightLayer.highlightFeature(feature);
      } else {
        console.log("OTHER GEOMETRY TYPE " + geometryType);
      }
    }
  }

  private removeViaPoint(): void {

    const legs = this.context.plan.legs;
    const nextLegIndex = legs.findIndex(leg => leg.sourceNode.featureId === this.nodeDrag.oldNode.featureId);
    const oldLeg1 = legs.get(nextLegIndex - 1);
    const oldLeg2 = legs.get(nextLegIndex);

    const newLeg: PlanLeg = this.context.oldBuildLeg(FeatureId.next(), oldLeg1.sourceNode, oldLeg2.sinkNode);

    const command = new PlannerCommandRemoveViaPoint(
      oldLeg1.featureId,
      oldLeg2.featureId,
      newLeg.featureId
    );
    this.context.execute(command);
  }

  private nodeSelected(networkNode: NetworkNodeFeature): void {
    if (this.context.plan.sourceNode === null) {
      this.addStartPoint(networkNode.node);
    } else {
      this.addLeg(networkNode.node);
    }
  }

  private addStartPoint(planNode: PlanNode): void {
    const sourceFlag = PlanFlag.start(FeatureId.next(), planNode);
    const command = new PlannerCommandAddStartPoint(planNode, sourceFlag);
    this.context.execute(command);
  }

  private addLeg(sinkNode: PlanNode): void {
    const sourceNode: PlanNode = this.context.plan.sinkNode();
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const leg = this.context.buildLeg(source, sink, sourceNode, sinkNode);
    const command = new PlannerCommandAddLeg(leg.featureId);
    this.context.execute(command);
  }

  private routeSelected(route: RouteFeature): void {

    // current sink is source for new leg
    const sourceNode = PlanUtil.planSinkNode(this.context.plan);
    const source: LegEnd = PlanUtil.legEndNode(+sourceNode.nodeId);

    const sink: LegEnd = PlanUtil.legEndRoute(route.routeId, route.pathId);
    const leg = this.context.buildLeg(source, sink, sourceNode, PlanUtil.planNode("0", "", new LatLonImpl("0", "0")));
    const command = new PlannerCommandAddLeg(leg.featureId);
    this.context.execute(command);
  }

  private legDragStarted(legId: string, coordinate: Coordinate): boolean {
    const leg = this.context.legs.getById(legId);
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
      && this.nodeDrag.oldNode.featureId !== this.context.plan.sinkNode().featureId
      && this.nodeDrag.oldNode.featureId !== this.context.plan.sourceNode.featureId;
  }

  private dropLegOnNode(connection: PlanNode): void {
    if (this.legDrag !== null) {
      const oldLeg = this.context.legs.getById(this.legDrag.oldLegId);
      if (oldLeg) {
        const newLeg1 = this.context.oldBuildLeg(FeatureId.next(), oldLeg.sourceNode, connection);
        const newLeg2 = this.context.oldBuildLeg(FeatureId.next(), connection, oldLeg.sinkNode);
        const command = new PlannerCommandSplitLeg(oldLeg.featureId, newLeg1.featureId, newLeg2.featureId);
        this.context.execute(command);
      }
      this.legDrag = null;
    }
  }

  private dropNodeOnNode(newNode: PlanNode): void {
    if (this.nodeDrag.flagType === PlanFlagType.Start) {
      if (this.context.plan.legs.isEmpty()) {
        this.moveStartPoint(newNode);
      } else {
        this.moveFirstLegSource(newNode);
      }
    } else if (this.nodeDrag.flagType === PlanFlagType.End) {
      this.moveEndPoint(newNode);
    } else {
      const oldLastLeg: PlanLeg = this.context.plan.legs.last();

      const legs = this.context.plan.legs;
      const nextLegIndex = legs.findIndex(leg => leg.featureId === this.nodeDrag.legFeatureId);
      const oldLeg1 = legs.get(nextLegIndex - 1);
      const oldLeg2 = legs.get(nextLegIndex);

      const newLeg1: PlanLeg = this.context.oldBuildLeg(FeatureId.next(), oldLeg1.sourceNode, newNode);
      const newLeg2: PlanLeg = this.context.oldBuildLeg(FeatureId.next(), newNode, oldLeg2.sinkNode);

      const command = new PlannerCommandMoveViaPoint(
        oldLeg1.featureId,
        oldLeg2.featureId,
        newLeg1.featureId,
        newLeg2.featureId
      );
      this.context.execute(command);
    }

    this.nodeDrag = null;
  }

  private moveStartPoint(newSourceNode: PlanNode): void {
    const command = new PlannerCommandMoveStartPoint(this.nodeDrag.oldNode, newSourceNode);
    this.context.execute(command);
  }

  private moveFirstLegSource(newSourceNode: PlanNode): void {

    const oldLeg: PlanLeg = this.context.plan.legs.get(0, null);
    const oldSourceNode = this.context.plan.sourceNode;
    const oldSourceFlag = this.context.plan.sourceFlag;

    const newSourceFlag = oldSourceFlag.withCoordinate(newSourceNode.coordinate);
    const sinkNode = oldLeg.sinkNode;
    const source = PlanUtil.legEndNode(+newSourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const newLeg = this.context.buildLeg(source, sink, newSourceNode, sinkNode);

    const command = new PlannerCommandMoveFirstLegSource(
      oldLeg.featureId,
      oldSourceNode,
      oldSourceFlag,
      newLeg.featureId,
      newSourceNode,
      newSourceFlag
    );
    this.context.execute(command);
  }

  private moveEndPoint(sinkNode: PlanNode): void {
    const oldLeg = this.context.plan.legs.get(-1, null);
    const sourceNode = oldLeg.sourceNode;
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const newLeg = this.context.buildLeg(source, sink, sourceNode, sinkNode);
    const command = new PlannerCommandMoveEndPoint(oldLeg.featureId, newLeg.featureId);
    this.context.execute(command);
  }

  private dropLegOnRoute(routeFeature: RouteFeature, coordinate: Coordinate) {
    console.log("drop leg on routeFeature id=" + routeFeature.feature.get("id"));
  }

  private dropNodeOnRoute(routeFeature, coordinate: Coordinate) {

    if (this.nodeDrag.flagType === PlanFlagType.Via) {

      // TODO PLAN continue
      // const legs = this.context.plan.legs;
      //
      // const nextLegIndex = legs.findIndex(leg => leg.source.featureId === this.nodeDrag.oldNode.featureId);
      // const oldLeg1 = legs.get(nextLegIndex - 1);
      // const oldLeg2 = legs.get(nextLegIndex);
      //
      // const routeFeatureId = routeFeature.feature.get("id");
      // const idParts = routeFeatureId.split("-");
      // const viaRoute = new ViaRoute(idParts[0], idParts[1]);
      //
      // const newLeg: PlanLeg = this.context.buildLeg(FeatureId.next(), oldLeg1.source, oldLeg2.sink, viaRoute);
      //
      // const command = new PlannerCommandMoveViaPointToViaRoute(
      //   oldLeg1.featureId,
      //   oldLeg2.featureId,
      //   newLeg.featureId,
      //   viaRoute,
      //   coordinate
      // );
      //
      // this.context.execute(command);
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
