import {List} from "immutable";
import {Feature} from "ol";
import {Coordinate} from "ol/coordinate";
import {MultiLineString} from "ol/geom";
import {Point} from "ol/geom";
import {LineString} from "ol/geom";
import GeometryLayout from "ol/geom/GeometryLayout";
import GeometryType from "ol/geom/GeometryType";
import RenderFeature from "ol/render/Feature";
import {LegEndRoute} from "src/app/kpn/api/common/planner/leg-end-route";
import {NodeClick} from "../../../components/ol/domain/node-click";
import {PoiClick} from "../../../components/ol/domain/poi-click";
import {PoiId} from "../../../components/ol/domain/poi-id";
import {RouteClick} from "../../../components/ol/domain/route-click";
import {TrackPathKey} from "../../../kpn/api/common/common/track-path-key";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerCommandAddLeg} from "../commands/planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "../commands/planner-command-add-start-point";
import {PlannerCommandMoveEndPoint} from "../commands/planner-command-move-end-point";
import {PlannerCommandMoveFirstLegSource} from "../commands/planner-command-move-first-leg-source";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerCommandMoveViaPoint} from "../commands/planner-command-move-via-point";
import {PlannerCommandRemoveRouteViaPoint} from "../commands/planner-command-remove-route-via-point";
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
import {DropEndNodeOnRoute} from "./drop-end-node-on-route";
import {DropViaNodeOnRoute} from "./drop-via-node-on-route";
import {Features} from "./features";
import {PlannerDragFlag} from "./planner-drag-flag";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";
import {PlannerDragLeg} from "./planner-drag-leg";
import {PlannerDragViaRouteFlag} from "./planner-drag-via-route-flag";
import {PlannerDragViaRouteFlagAnalyzer} from "./planner-drag-via-route-flag-analyzer";
import {PlannerEngine} from "./planner-engine";

export class PlannerEngineImpl implements PlannerEngine {

  private newInteractionToggle = true;

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
        const routes = Features.findRoutes(features);
        if (!routes.isEmpty()) {
          this.routeSelected(routes, coordinate);
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

    if (this.isDraggingNode()) {
      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        this.highlightNode(networkNodeFeature.node);
        // snap to node position
        this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, networkNodeFeature.node.coordinate);
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

      this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, coordinate);
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

    if (this.isViaRouteFlagClicked(singleClick)) {
      this.removeRouteViaPoint();
      this.dragCancel();
      return true;
    }

    if (this.isViaRouteFlagClicked(singleClick)) {
      console.log("viaRouteFlagClicked");
    }


    if (this.isDraggingLeg() || this.isDraggingNode()) {

      console.log("end draggingLeg or draggingNode");

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

      const routeFeatures = Features.findRoutes(features);
      if (!routeFeatures.isEmpty()) {


        if (this.isDraggingLeg()) {
          // TODO PLAN make following line work again
          // this.dropLegOnRoute(routeFeature, coordinate);
        } else if (this.isDraggingNode()) {
          this.dropNodeOnRoute(routeFeatures, coordinate);
        }
        return true;
      }

      if (this.isDraggingNode()) {
        // cancel drag - put flag at its original coordinate again
        this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, this.nodeDrag.oldNode.coordinate);
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

    const sourceNode = oldLeg1.sourceNode;
    const sinkNode = oldLeg2.sinkNode;
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    const newLeg = this.context.buildLeg(source, sink, sourceNode, sinkNode, oldLeg2.sinkFlag.flagType);

    const command = new PlannerCommandRemoveViaPoint(
      oldLeg1.featureId,
      oldLeg2.featureId,
      newLeg.featureId
    );
    this.context.execute(command);
  }

  private removeRouteViaPoint(): void {
    const clickedLeg = this.context.plan.legs.find(leg => leg.featureId === this.viaRouteDrag.legFeatureId);
    if (clickedLeg != null) {
      if (clickedLeg.sinkFlag.flagType === PlanFlagType.End) {
        this.removeEndLegRouteViaPoint(clickedLeg);
      } else {
        this.removeViaLegRouteViaPoint(clickedLeg);
      }
    }
  }

  private removeEndLegRouteViaPoint(oldLeg: PlanLeg): void {
    const source = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);
    const newLeg = this.context.buildLeg(source, sink, oldLeg.sourceNode, oldLeg.sinkNode, oldLeg.sinkFlag.flagType);
    const command = new PlannerCommandRemoveRouteViaPoint(oldLeg.featureId, newLeg.featureId);
    this.context.execute(command);
  }

  private removeViaLegRouteViaPoint(oldLeg1: PlanLeg): void {

    const oldLeg2 = this.context.plan.legs.find(leg => {
      console.log("  leg.sourceNode.featureId=" + leg.sourceNode.nodeId + ", oldLeg1.sinkNode.featureId=" + oldLeg1.sinkNode.nodeId);
      return leg.sourceNode.nodeId === oldLeg1.sinkNode.nodeId;
    });

    if (oldLeg2 != null) {
      const sourceNode = oldLeg1.sourceNode;
      const sinkNode = oldLeg2.sinkNode;
      const source = PlanUtil.legEndNode(+sourceNode.nodeId);
      const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

      const newLeg = this.context.buildLeg(source, sink, sourceNode, sinkNode, oldLeg2.sinkFlag.flagType);

      const command = new PlannerCommandRemoveViaPoint(
        oldLeg1.featureId,
        oldLeg2.featureId,
        newLeg.featureId
      );
      this.context.execute(command);
    }
  }

  private nodeSelected(networkNode: NetworkNodeFeature): void {
    if (this.context.plan.sourceNode === null) {
      this.addStartPoint(networkNode.node);
    } else {
      this.addLeg(networkNode.node);
    }
  }

  private addStartPoint(planNode: PlanNode): void {
    const sourceFlag = PlanFlag.start(FeatureId.next(), planNode.coordinate);
    const command = new PlannerCommandAddStartPoint(planNode, sourceFlag);
    this.context.execute(command);
  }

  private addLeg(sinkNode: PlanNode): void {
    const sourceNode: PlanNode = this.context.plan.sinkNode();
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const leg = this.context.buildLeg(source, sink, sourceNode, sinkNode, PlanFlagType.End);
    const command = new PlannerCommandAddLeg(leg.featureId);
    this.context.execute(command);
  }

  private routeSelected(routes: List<RouteFeature>, coordinate: Coordinate): void {

    console.log("routeSelected()");

    // current sink is source for new leg
    const sourceNode = PlanUtil.planSinkNode(this.context.plan);
    const source: LegEnd = PlanUtil.legEndNode(+sourceNode.nodeId);

    const trackPathKeys = routes.flatMap(routeFeature => {
      const trackPathKey = routeFeature.toTrackPathKey();
      if (routeFeature.oneWay) {
        return List([trackPathKey]);
      }
      const extraTrackPathKey = new TrackPathKey(routeFeature.routeId, 100 + routeFeature.pathId);
      return List([trackPathKey, extraTrackPathKey]);
    });

    const sink: LegEnd = new LegEnd(null, new LegEndRoute(trackPathKeys));
    const sinkNode = PlanUtil.planNodeWithCoordinate("0", "", coordinate);
    const leg = this.context.buildLeg(source, sink, sourceNode, sinkNode, PlanFlagType.End);

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

  private isDraggingViaRouteFlag(): boolean {
    return this.viaRouteDrag !== null;
  }

  private isViaFlagClicked(singleClick: boolean): boolean {
    return singleClick === true && this.nodeDrag !== null
      && this.nodeDrag.oldNode.featureId !== this.context.plan.sinkNode().featureId
      && this.nodeDrag.oldNode.featureId !== this.context.plan.sourceNode.featureId;
  }

  private isViaRouteFlagClicked(singleClick: boolean): boolean {
    return singleClick === true && this.viaRouteDrag !== null;
  }

  private dropLegOnNode(connection: PlanNode): void {
    if (this.legDrag !== null) {
      const oldLeg = this.context.legs.getById(this.legDrag.oldLegId);
      if (oldLeg) {

        const sourceNode1 = oldLeg.sourceNode;
        const sinkNode1 = connection;
        const source1 = PlanUtil.legEndNode(+sourceNode1.nodeId);
        const sink1 = PlanUtil.legEndNode(+sinkNode1.nodeId);
        const sinkFlagType1 = PlanFlagType.Via;
        const newLeg1 = this.context.buildLeg(source1, sink1, sourceNode1, sinkNode1, sinkFlagType1);

        const sourceNode2 = connection;
        const sinkNode2 = oldLeg.sinkNode;
        const source2 = PlanUtil.legEndNode(+sourceNode2.nodeId);
        const sink2 = PlanUtil.legEndNode(+sinkNode2.nodeId);
        const isLastLeg = oldLeg.sinkNode.featureId === this.context.plan.sinkNode().featureId;
        const sinkFlagType2 = isLastLeg ? PlanFlagType.End : PlanFlagType.Via;
        const newLeg2 = this.context.buildLeg(source2, sink2, sourceNode2, sinkNode2, sinkFlagType2);

        const command = new PlannerCommandSplitLeg(oldLeg.featureId, newLeg1.featureId, newLeg2.featureId);
        this.context.execute(command);
      }
      this.legDrag = null;
    }
  }

  private dropNodeOnNode(targetNode: PlanNode): void {

    if (this.nodeDrag.planFlag.flagType === PlanFlagType.Start) {
      if (this.context.plan.legs.isEmpty()) {
        this.moveStartPoint(targetNode);
      } else {
        this.moveFirstLegSource(targetNode);
      }
    } else if (this.nodeDrag.planFlag.flagType === PlanFlagType.End) {
      this.moveEndPoint(targetNode);
    } else {
      this.moveViaPoint(targetNode);
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
    const newSinkNode = oldLeg.sinkNode;
    const newSource = PlanUtil.legEndNode(+newSourceNode.nodeId);
    const newSink = PlanUtil.legEndNode(+newSinkNode.nodeId);
    const newLeg = this.context.buildLeg(newSource, newSink, newSourceNode, newSinkNode, oldLeg.sinkFlag.flagType);

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
    const newLeg = this.context.buildLeg(source, sink, sourceNode, sinkNode, PlanFlagType.End);
    const command = new PlannerCommandMoveEndPoint(oldLeg.featureId, newLeg.featureId);
    this.context.execute(command);
  }

  private moveViaPoint(targetNode: PlanNode): void {

    const legs = this.context.plan.legs;
    const legIndex1 = legs.findIndex(leg => leg.sinkFlag.featureId === this.nodeDrag.planFlag.featureId);
    const oldLeg1 = legs.get(legIndex1);
    const oldLeg2 = legs.get(legIndex1 + 1);

    const sourceNode1 = oldLeg1.sourceNode;
    const sinkNode1 = targetNode;
    const source1 = PlanUtil.legEndNode(+sourceNode1.nodeId);
    const sink1 = PlanUtil.legEndNode(+sinkNode1.nodeId);

    const sourceNode2 = targetNode;
    const sinkNode2 = oldLeg2.sinkNode;
    const source2 = PlanUtil.legEndNode(+sourceNode2.nodeId);
    const sink2 = PlanUtil.legEndNode(+sinkNode2.nodeId);

    const newLeg1 = this.context.buildLeg(source1, sink1, sourceNode1, sinkNode1, PlanFlagType.Via);
    const newLeg2 = this.context.buildLeg(source2, sink2, sourceNode2, sinkNode2, PlanFlagType.End);

    const command = new PlannerCommandMoveViaPoint(
      oldLeg1.featureId,
      oldLeg2.featureId,
      newLeg1.featureId,
      newLeg2.featureId
    );
    this.context.execute(command);
  }


  private dropLegOnRoute(routeFeature: RouteFeature, coordinate: Coordinate) {
    console.log("drop leg on routeFeature id=" + routeFeature.feature.get("id"));
  }

  private dropNodeOnRoute(routeFeatures: List<RouteFeature>, coordinate: Coordinate) {
    if (this.nodeDrag.planFlag.flagType === PlanFlagType.Via) {
      new DropViaNodeOnRoute(this.context).drop(this.nodeDrag, routeFeatures, coordinate);
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
