import { PlanNode } from '@api/common/planner';
import { OlUtil } from '@app/ol';
import { PoiId } from '@app/ol/domain';
import * as Sentry from '@sentry/angular-ivy';
import { List } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { PlannerCommandAddStartPoint } from '../commands/planner-command-add-start-point';
import { PlannerCommandMoveStartPoint } from '../commands/planner-command-move-start-point';
import { PlannerContext } from '../context/planner-context';
import { PlanPrinter } from '../debug/plan-printer';
import { FeatureId } from '../features/feature-id';
import { FlagFeature } from '../features/flag-feature';
import { MapFeature } from '../features/map-feature';
import { NetworkNodeFeature } from '../features/network-node-feature';
import { PoiFeature } from '../features/poi-feature';
import { RouteFeature } from '../features/route-feature';
import { PlanFlag } from '../plan/plan-flag';
import { PlanFlagType } from '../plan/plan-flag-type';
import { AddLeg } from './actions/add-leg';
import { AddViaRouteLeg } from './actions/add-via-route-leg';
import { DropEndNodeOnRoute } from './actions/drop-end-node-on-route';
import { DropLegOnNode } from './actions/drop-leg-on-node';
import { DropViaNodeOnRoute } from './actions/drop-via-node-on-route';
import { DropViaRouteOnRoute } from './actions/drop-via-route-on-route';
import { MoveEndPoint } from './actions/move-end-point';
import { MoveFirstLegSource } from './actions/move-first-leg-source';
import { MoveNodeViaPointToNode } from './actions/move-node-via-point-to-node';
import { MoveRouteViaPointToNode } from './actions/move-route-via-point-to-node';
import { NodeClick } from './actions/node-click';
import { PoiClick } from './actions/poi-click';
import { RemoveEndLegRouteViaPoint } from './actions/remove-end-leg-route-via-point';
import { RemoveViaLegRouteViaPoint } from './actions/remove-via-leg-route-via-point';
import { RemoveViaPoint } from './actions/remove-via-point';
import { RouteClick } from './actions/route-click';
import { Features } from './features';
import { PlannerDragFlag } from './planner-drag-flag';
import { PlannerDragFlagAnalyzer } from './planner-drag-flag-analyzer';
import { PlannerDragLeg } from './planner-drag-leg';
import { PlannerDragViaRouteFlag } from './planner-drag-via-route-flag';
import { PlannerDragViaRouteFlagAnalyzer } from './planner-drag-via-route-flag-analyzer';
import { PlannerEngine } from './planner-engine';

export class PlannerEngineImpl implements PlannerEngine {
  private legDrag: PlannerDragLeg = null;
  private nodeDrag: PlannerDragFlag = null;
  private viaRouteDrag: PlannerDragViaRouteFlag = null;

  constructor(private context: PlannerContext) {}

  handleDownEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {
    const networkNodeFeature = Features.findNetworkNode(features);
    if (networkNodeFeature != null) {
      if (!this.context.planProposed && networkNodeFeature.proposed) {
        // ignore this feature
      } else {
        this.context.highlighter.mouseDown(coordinate);
        return true;
      }
    }

    const route = Features.findRoute(features);
    if (route != null) {
      if (!this.context.planProposed && route.proposed) {
        // ignore this feature
      } else {
        this.context.highlighter.mouseDown(coordinate);
        return true;
      }
    }

    const poiFeature = Features.findPoi(features);
    if (poiFeature != null) {
      this.context.highlighter.mouseDown(coordinate);
      return true;
    }

    const flag = Features.findFlag(features);
    if (flag != null) {
      this.context.highlighter.mouseDown(coordinate);
      return true;
    }

    return true; // propagate
  }

  handleMoveEvent(
    features: List<MapFeature>,
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean {
    if (features.isEmpty()) {
      this.context.highlighter.reset();
      this.context.cursor.setStyleDefault();
      return false;
    }

    const flagFeature = Features.findFlag(features);
    if (flagFeature) {
      this.context.cursor.setStyleGrab();
      return true;
    }

    const networkNodeFeature = Features.findNetworkNode(features);
    if (networkNodeFeature != null) {
      if (modifierKeyOnly || networkNodeFeature.node.nodeName !== '*') {
        if (!this.context.planProposed && networkNodeFeature.proposed) {
          // ignore this feature
        } else {
          this.context.highlighter.highlightNode(networkNodeFeature.node);
          this.context.cursor.setStylePointer();
          return true;
        }
      }
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
      if (modifierKeyOnly || this.context.plan().sourceNode !== null) {
        if (!this.context.planProposed && route.proposed) {
          // ignore this feature
        } else {
          // no clicking routes when start node has not been selected yet
          this.context.cursor.setStylePointer();
          this.context.highlighter.highlightRoute(route);
          return true;
        }
      }
    }

    this.context.highlighter.reset();
    this.context.cursor.setStyleDefault();

    return false;
  }

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {
    if (this.isDraggingNode()) {
      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        if (!this.context.planProposed && networkNodeFeature.proposed) {
          // ignore this feature
        } else {
          if (networkNodeFeature.node.nodeName !== '*') {
            this.context.highlighter.highlightNode(networkNodeFeature.node);
            // snap to node position
            this.context.markerLayer.updateFlagCoordinate(
              this.nodeDrag.planFlag.featureId,
              networkNodeFeature.node.coordinate
            );
            this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
            return false;
          }
        }
      }

      if (!this.isDraggingStartNode()) {
        const routeFeature = Features.findRoute(features);
        if (routeFeature != null) {
          if (!this.context.planProposed && routeFeature.proposed) {
            // ignore this feature
            this.context.highlighter.reset();
          } else {
            this.context.highlighter.highlightRoute(routeFeature);
          }
        } else {
          this.context.highlighter.reset();
        }
      }

      this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.updatePosition(coordinate);
      return false;
    }

    if (this.isDraggingViaRouteFlag()) {
      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        if (!this.context.planProposed && networkNodeFeature.proposed) {
          // ignore this feature
        } else {
          this.context.highlighter.highlightNode(networkNodeFeature.node);
          // snap to node position
          this.context.markerLayer.updateFlagCoordinate(
            this.viaRouteDrag.planFlag.featureId,
            networkNodeFeature.node.coordinate
          );
          this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
          return false;
        }
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        if (!this.context.planProposed && routeFeature.proposed) {
          // ignore this feature
        } else {
          this.context.highlighter.highlightRoute(routeFeature);
          this.context.markerLayer.updateFlagCoordinate(
            this.viaRouteDrag.planFlag.featureId,
            coordinate
          );
          this.context.elasticBand.updatePosition(coordinate);
          return false;
        }
      }

      this.context.highlighter.reset();
      this.context.markerLayer.updateFlagCoordinate(
        this.viaRouteDrag.planFlag.featureId,
        coordinate
      );
      this.context.elasticBand.updatePosition(coordinate);
      return false;
    }

    if (this.isDraggingLeg()) {
      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        if (!this.context.planProposed && networkNodeFeature.proposed) {
          // ignore this feature
        } else {
          this.context.highlighter.highlightNode(networkNodeFeature.node);
          // snap to node position
          this.context.elasticBand.updatePosition(networkNodeFeature.node.coordinate);
          return false;
        }
      }

      const routeFeature = Features.findRoute(features);
      if (routeFeature != null) {
        if (!this.context.planProposed && routeFeature.proposed) {
          // ignore this feature
        } else {
          this.context.highlighter.highlightRoute(routeFeature);
        }
      } else {
        this.context.highlighter.reset();
      }
      this.context.elasticBand.updatePosition(coordinate);
      return false;
    }

    // not currently dragging yet, start drag?

    const flag = Features.findFlag(features);
    if (flag != null) {
      if (this.flagDragStarted(flag, coordinate)) {
        return false; // prevent propagation
      }
    }

    const leg = Features.findLeg(features);
    if (leg != null) {
      this.breadcrumb('down event leg', {
        featureId: leg?.id,
        plan: this.planSummary(),
      });

      if (this.legDragStarted(leg.id, coordinate)) {
        return false; // prevent propagation
      }
    }

    return true; // propagate further: panning map itself?
  }

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate): boolean {
    if (this.isDraggingLeg() || this.isDraggingNode()) {
      const networkNode = Features.findNetworkNode(features);
      if (networkNode != null) {
        if (!this.context.planProposed && networkNode.proposed) {
          // ignore this feature
        } else {
          if (this.isDraggingLeg()) {
            new DropLegOnNode(this.context).drop(this.legDrag, networkNode.node);
            this.dragCancel();
          } else if (this.isDraggingNode()) {
            this.dropNodeOnNode(networkNode.node);
            this.dragCancel();
          }
          return false; // do not propagate
        }
      }

      if (!this.isDraggingStartNode()) {
        const routeFeatures = Features.findRoutes(features).filter((route) => {
          return !(!this.context.planProposed && route.proposed);
        });
        if (!routeFeatures.isEmpty()) {
          if (this.isDraggingLeg()) {
            this.dropLegOnRoute(routeFeatures, coordinate);
          } else if (this.isDraggingNode()) {
            this.dropNodeOnRoute(routeFeatures, coordinate);
          }
          this.dragCancel();
          return false; // do not propagate
        }
      }

      if (this.isDraggingNode()) {
        // cancel drag - put flag at its original coordinate again
        this.context.markerLayer.updateFlagCoordinate(
          this.nodeDrag.planFlag.featureId,
          this.nodeDrag.planFlag.coordinate
        );
      }

      this.dragCancel();

      return false; // do not propagate
    }

    if (this.isDraggingViaRouteFlag()) {
      const networkNodeFeature = Features.findNetworkNode(features);
      if (networkNodeFeature != null) {
        if (!this.context.planProposed && networkNodeFeature.proposed) {
          // ignore this feature
        } else {
          const oldLeg = this.context
            .plan()
            .legs.find((leg) => leg.featureId === this.viaRouteDrag.legFeatureId);
          if (oldLeg) {
            new MoveRouteViaPointToNode(this.context).viaRouteDragMove(
              this.viaRouteDrag,
              networkNodeFeature.node,
              oldLeg
            );
          }
          this.dragCancel();
          return false; // do not propagate
        }
      }

      const routeFeatures = Features.findRoutes(features).filter((route) => {
        return !(!this.context.planProposed && route.proposed);
      });
      if (!routeFeatures.isEmpty()) {
        const oldLeg = this.context
          .plan()
          .legs.find((leg) => leg.featureId === this.viaRouteDrag.legFeatureId);
        if (oldLeg) {
          new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
        }
        this.dragCancel();
        return false; // do not propagate
      }

      this.dragCancel();
      return false; // do not propagate
    }

    return true; // propagate
  }

  handleMouseLeave(): void {
    this.context.highlighter.reset();
  }

  handleSingleClickEvent(
    features: List<MapFeature>,
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean {
    if (features.isEmpty()) {
      this.context.highlighter.reset();
      this.context.closeOverlay();
      return false; // prevent further propagation
    }

    const networkNode = Features.findNetworkNode(features);
    if (networkNode != null) {
      if (!modifierKeyOnly && !this.context.planProposed && networkNode.proposed) {
        // ignore this feature
      } else {
        return this.singleClickNetworkNode(networkNode, coordinate, modifierKeyOnly);
      }
    }

    const flag = Features.findFlag(features);
    if (flag != null) {
      return this.singleClickFlag(flag, coordinate);
    }

    if (modifierKeyOnly === true) {
      const route = Features.findRoute(features);
      if (route != null) {
        return this.ctrlSingleClickRoute(route, coordinate);
      }
    } else if (this.context.plan().sourceNode !== null) {
      const routes = Features.findRoutes(features).filter((route) => {
        return !(!this.context.planProposed && route.proposed);
      });
      if (!routes.isEmpty()) {
        return this.singleClickRoutes(routes, coordinate);
      }
    }

    const poiFeature = Features.findPoi(features);
    if (poiFeature != null) {
      return this.singleClickPoi(poiFeature, modifierKeyOnly);
    }

    return false; // prevent further propagation
  }

  private singleClickNetworkNode(
    networkNode: NetworkNodeFeature,
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean {
    this.breadcrumb('single click event network node', {
      modifierKeyOnly,
      featureId: networkNode?.node?.featureId,
      nodeId: networkNode?.node?.nodeId,
      nodeName: networkNode?.node?.nodeName,
      coordinate: OlUtil.coordinateToString(networkNode?.node?.coordinate),
      plan: this.planSummary(),
    });

    if (modifierKeyOnly) {
      this.context.plannerPopup.nodeClicked(new NodeClick(coordinate, networkNode));
    } else {
      if (!this.context.planProposed && networkNode.proposed) {
        // ignore this feature
      } else {
        this.nodeSelected(networkNode);
      }
    }
    return false; // prevent further propagation
  }

  private singleClickFlag(flag: FlagFeature, coordinate: Coordinate): boolean {
    if (flag.flagType === PlanFlagType.via) {
      const legs = this.context.plan().legs;
      if (!legs.isEmpty()) {
        const legIndex = legs.findIndex((leg) => flag.id === leg.sinkFlag?.featureId);
        if (legIndex >= 0) {
          this.breadcrumb('single click via flag', {
            flagType: flag.flagType,
            flagFeatureId: flag.id,
            coordinate: OlUtil.coordinateToString(coordinate),
            plan: this.planSummary(),
          });
          const previousLeg = legs.get(legIndex);
          const nextLeg = legs.get(legIndex + 1);
          const plannerDragFlag = new PlannerDragFlag(
            previousLeg.sinkFlag,
            nextLeg.featureId,
            previousLeg.sourceNode.coordinate,
            nextLeg.sinkNode.coordinate,
            nextLeg.sourceNode
          );
          new RemoveViaPoint(this.context).remove(plannerDragFlag);
          return false; // do not propagate
        }
      }
    }

    const localViaRouteDrag = new PlannerDragViaRouteFlagAnalyzer(this.context.plan()).dragStarted(
      flag
    );
    if (localViaRouteDrag !== null) {
      this.breadcrumb('single click route flag', {
        flagType: flag.flagType,
        flagFeatureId: flag.id,
        coordinate: OlUtil.coordinateToString(coordinate),
        plan: this.planSummary(),
      });

      const clickedLeg = this.context
        .plan()
        .legs.find((leg) => leg.featureId === localViaRouteDrag.legFeatureId);
      if (clickedLeg != null) {
        if (clickedLeg.sinkFlag.flagType === PlanFlagType.end) {
          new RemoveEndLegRouteViaPoint(this.context).remove(clickedLeg);
        } else {
          new RemoveViaLegRouteViaPoint(this.context).remove(clickedLeg);
        }
      }
      return false; // prevent further propagation
    }
    return false; // prevent further propagation
  }

  private ctrlSingleClickRoute(route: RouteFeature, coordinate: Coordinate): boolean {
    this.breadcrumb('ctrl single click route', {
      routeId: route?.routeId,
      pathId: route?.pathId,
      routeName: route?.routeName,
      oneWay: route?.oneWay,
      plan: this.planSummary(),
    });
    this.context.plannerPopup.routeClicked(new RouteClick(coordinate, route));
    return false; // prevent further propagation
  }

  private singleClickRoutes(routes: List<RouteFeature>, coordinate: Coordinate): boolean {
    this.breadcrumb('single click routes', {
      routeIds: routes?.map((route) => route?.routeId).join(', '),
      plan: this.planSummary(),
    });
    new AddViaRouteLeg(this.context).add(routes, coordinate);
    return false; // prevent further propagation
  }

  private singleClickPoi(poiFeature: PoiFeature, modifierKeyOnly: boolean): boolean {
    this.breadcrumb('single click event poi', {
      modifierKeyOnly,
      poiId: poiFeature.poiId,
      poiType: poiFeature.poiType,
      layer: poiFeature.layer,
      coordinate: OlUtil.coordinateToString(poiFeature.coordinate),
      plan: this.planSummary(),
    });

    this.context.plannerPopup.poiClicked(
      new PoiClick(poiFeature.coordinate, new PoiId(poiFeature.poiType, +poiFeature.poiId))
    );
    return false; // prevent further propagation
  }

  private nodeSelected(networkNode: NetworkNodeFeature): void {
    if (this.context.plan().sourceNode) {
      const sinkNode = this.context.plan().sinkNode();
      if (sinkNode && sinkNode.nodeId === networkNode.node.nodeId) {
        // we are already at that node, no need to add an extra leg here
      } else {
        new AddLeg(this.context).add(networkNode.node);
      }
    } else {
      this.addStartPoint(networkNode.node);
    }
  }

  private addStartPoint(planNode: PlanNode): void {
    const sourceFlag = PlanFlag.start(FeatureId.next(), planNode.coordinate);
    const command = new PlannerCommandAddStartPoint(planNode, sourceFlag);
    this.context.execute(command);
  }

  private legDragStarted(legId: string, coordinate: Coordinate): boolean {
    const leg = this.context.plan().legs.find((planLeg) => planLeg.featureId === legId);
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
    this.breadcrumb('flag drag started', {
      flagType: flag.flagType,
      flagFeatureId: flag.id,
      coordinate: OlUtil.coordinateToString(coordinate),
      plan: this.planSummary(),
    });

    this.nodeDrag = new PlannerDragFlagAnalyzer(this.context.plan()).dragStarted(flag);
    if (this.nodeDrag !== null) {
      this.context.markerLayer.updateFlagCoordinate(this.nodeDrag.planFlag.featureId, coordinate);
      this.context.elasticBand.set(this.nodeDrag.anchor1, this.nodeDrag.anchor2, coordinate);
      return true;
    }

    this.viaRouteDrag = new PlannerDragViaRouteFlagAnalyzer(this.context.plan()).dragStarted(flag);
    if (this.viaRouteDrag !== null) {
      this.context.markerLayer.updateFlagCoordinate(
        this.viaRouteDrag.planFlag.featureId,
        coordinate
      );
      this.context.elasticBand.set(
        this.viaRouteDrag.anchor1,
        this.viaRouteDrag.anchor2,
        coordinate
      );
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
    return this.nodeDrag !== null && this.nodeDrag.planFlag.flagType === PlanFlagType.start;
  }

  private isDraggingViaRouteFlag(): boolean {
    return this.viaRouteDrag !== null;
  }

  private dropNodeOnNode(targetNode: PlanNode): void {
    if (this.nodeDrag.planFlag.flagType === PlanFlagType.start) {
      if (this.context.plan().legs.isEmpty()) {
        this.moveStartPoint(targetNode);
      } else {
        new MoveFirstLegSource(this.context).move(this.nodeDrag, targetNode);
      }
    } else if (this.nodeDrag.planFlag.flagType === PlanFlagType.end) {
      new MoveEndPoint(this.context).move(this.nodeDrag, targetNode);
    } else {
      this.moveViaPoint(targetNode);
    }
  }

  private moveStartPoint(newSourceNode: PlanNode): void {
    const command = new PlannerCommandMoveStartPoint(this.nodeDrag.oldNode, newSourceNode);
    this.context.execute(command);
  }

  private moveViaPoint(targetNode: PlanNode): void {
    const legs = this.context.plan().legs;
    const legIndex1 = legs.findIndex(
      (leg) => leg.sinkFlag.featureId === this.nodeDrag.planFlag.featureId
    );
    if (legIndex1 >= 0) {
      new MoveNodeViaPointToNode(this.context).move(this.nodeDrag, targetNode, legIndex1);
    } else {
      const viaLeg = legs.find(
        (leg) => this.nodeDrag.planFlag.featureId === leg.viaFlag?.featureId
      );
      if (viaLeg) {
        new MoveRouteViaPointToNode(this.context).nodeDragMove(this.nodeDrag, targetNode, viaLeg);
      }
    }
  }

  private dropLegOnRoute(routeFeatures: List<RouteFeature>, coordinate: Coordinate) {
    const oldLeg = this.context.plan().legs.find((leg) => leg.featureId === this.legDrag.oldLegId);
    if (oldLeg) {
      new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
    }
  }

  private dropNodeOnRoute(routeFeatures: List<RouteFeature>, coordinate: Coordinate) {
    if (this.nodeDrag.planFlag.flagType === PlanFlagType.via) {
      if (this.nodeDrag.oldNode === null) {
        const oldLeg = this.context
          .plan()
          .legs.find((leg) => this.nodeDrag.planFlag.featureId === leg.viaFlag?.featureId);
        if (oldLeg) {
          new DropViaRouteOnRoute(this.context).drop(oldLeg, routeFeatures, coordinate);
        }
      } else {
        new DropViaNodeOnRoute(this.context).drop(this.nodeDrag, routeFeatures, coordinate);
      }
    } else if (this.nodeDrag.planFlag.flagType === PlanFlagType.end) {
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

  private breadcrumb(message: string, data: { [key: string]: any }): void {
    Sentry.addBreadcrumb({
      type: 'user',
      category: 'action',
      level: 'info',
      message,
      data,
    });
  }

  private planSummary(): string {
    return new PlanPrinter().plan(this.context.plan()).result();
  }
}
