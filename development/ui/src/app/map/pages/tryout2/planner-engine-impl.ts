import Coordinate from 'ol/View';
import {PlannerEngine} from "./planner-engine";
import {PlannerContext} from "./planner-context";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlanNode} from "./plan-node";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {AppService} from "../../../app.service";
import {List} from "immutable";
import {PlanLegFragment} from "./plan-leg-fragment";
import {fromLonLat} from 'ol/proj';
import {PlanLeg} from "./plan-leg";
import {PlannerDragNodeAnalyzer} from "./planner-drag-node-analyzer";
import {PlannerDragNode} from "./planner-drag-node";
import {PlannerDragLeg} from "./planner-drag-leg";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";

export class PlannerEngineImpl implements PlannerEngine {

  private legIdGenerator = 0;

  private legDrag: PlannerDragLeg = null;
  private nodeDrag: PlannerDragNode = null;

  constructor(private context: PlannerContext, private appService: AppService) {
  }

  nodeSelected(nodeId: string, nodeName: string, coordinate: Coordinate): void {
    if (this.context.plan.source === null) {
      const node = new PlanNode(nodeId, nodeName, coordinate);
      const command = new PlannerCommandAddStartPoint(node);
      this.context.commandStack.push(command);
      command.do(this.context);
    } else {
      const legId = "" + ++this.legIdGenerator;
      const source: PlanNode = this.context.plan.lastNode();
      const sink = new PlanNode(nodeId, nodeName, coordinate);
      const command = new PlannerCommandAddLeg(legId, source, sink);
      this.context.commandStack.push(command);
      command.do(this.context);

      const cachedLeg = this.context.legCache.get(source.nodeId, sink.nodeId);
      if (cachedLeg == null) {
        this.appService.routeLeg("rwn", legId, source.nodeId, sink.nodeId).subscribe(response => {
          if (response.result) {
            const fragments = response.result.fragments.map(routeLegFragment => {
              const nodeId: string = routeLegFragment.sink.nodeId;
              const nodeName: string = routeLegFragment.sink.nodeName;

              const lon = parseFloat(routeLegFragment.sink.latLon.longitude);
              const lat = parseFloat(routeLegFragment.sink.latLon.latitude);

              const coordinate: Coordinate = fromLonLat([lon, lat]);

              const sink: PlanNode = new PlanNode(nodeId, nodeName, coordinate);
              const meters: number = routeLegFragment.meters;
              const coordinates: List<Coordinates> = routeLegFragment.latLons.map(f => {
                const lon = parseFloat(f.longitude);
                const lat = parseFloat(f.latitude);
                return fromLonLat([lon, lat]);
              });

              return new PlanLegFragment(sink, meters, coordinates);
            });

            this.context.updatePlanLeg(legId, fragments);
            const leg = new PlanLeg(legId, source, sink, fragments);
            this.context.legCache.add(leg);
            const coordinates = fragments.flatMap(f => f.coordinates);
            this.context.routeLayer.addRouteLeg(legId, coordinates);
          } else {
            // TODO handle leg not found
          }
        });
      }
    }
  }

  legDragStarted(legId: string, coordinate: Coordinate): boolean {
    console.log("DEBUG start drag leg " + legId);
    const leg = this.context.legCache.getById(legId);
    if (leg) {
      const anchor1 = leg.source.coordinate;
      const anchor2 = leg.sink.coordinate;
      this.legDrag = new PlannerDragLeg(legId, anchor1, anchor2);
      this.context.routeLayer.showDoubleElasticBand(anchor1, anchor2, coordinate);
      return true;
    }
    return false;
  }

  legNodeDragStarted(legNodeId: string, nodeId: string, coordinate: Coordinate): boolean {
    console.log("DEBUG start drag leg node " + legNodeId);
    this.nodeDrag = new PlannerDragNodeAnalyzer(this.context.plan).dragStarted(legNodeId, nodeId);
    if (this.nodeDrag !== null) {
      this.context.routeLayer.showDoubleElasticBand(this.nodeDrag.anchor1, this.nodeDrag.anchor2, coordinate);
      return true;
    }
    return false;
  }

  isDraggingLeg(): boolean {
    return this.legDrag !== null;
  }

  isDraggingNode(): boolean {
    return this.nodeDrag !== null;
  }

  endDragLeg(nodeId: string, nodeName: string, coordinate: Coordinate): void {
    if (this.legDrag !== null) {
      const oldLeg = this.context.legCache.getById(this.legDrag.legId);
      if (oldLeg) {
        const connection = new PlanNode(nodeId, nodeName, coordinate);

        const newLegId1 = "" + ++this.legIdGenerator;
        const newLeg1 = this.buildLeg(newLegId1, oldLeg.source, connection);

        const newLegId2 = "" + ++this.legIdGenerator;
        const newLeg2 = this.buildLeg(newLegId2, connection, oldLeg.sink);

        const command = new PlannerCommandSplitLeg(oldLeg, newLeg1, newLeg2);
        this.context.commandStack.push(command);
        command.do(this.context);
      }
      this.legDrag = null;
    }
  }

  endDragNode(nodeId: string, nodeName: string, coordinate: Coordinate): void {
   this.nodeDrag = null;
  }

  dragCancel(): void {
    if (this.legDrag !== null) {
      this.context.routeLayer.hideDoubleElasticBand();
      this.legDrag = null;
    }

    if (this.nodeDrag !== null) {
      this.context.routeLayer.hideDoubleElasticBand();
      this.nodeDrag = null;
    }
  }

  undo(): void {
    const command = this.context.commandStack.undo();
    command.undo(this.context);
  }

  redo(): void {
    const command = this.context.commandStack.redo();
    command.do(this.context);
  }


  private buildLeg(legId: string, source: PlanNode, sink: PlanNode): PlanLeg {

    const cachedLeg = this.context.legCache.get(source.nodeId, sink.nodeId);
    if (cachedLeg) {
      return new PlanLeg(legId, source, sink, cachedLeg.fragments);
    }

    this.appService.routeLeg("rwn", legId, source.nodeId, sink.nodeId).subscribe(response => {
      if (response.result) {
        const fragments = response.result.fragments.map(routeLegFragment => {
          const nodeId: string = routeLegFragment.sink.nodeId;
          const nodeName: string = routeLegFragment.sink.nodeName;
          const lon = parseFloat(routeLegFragment.sink.latLon.longitude);
          const lat = parseFloat(routeLegFragment.sink.latLon.latitude);
          const coordinate: Coordinate = fromLonLat([lon, lat]);
          const sink: PlanNode = new PlanNode(nodeId, nodeName, coordinate);
          const meters: number = routeLegFragment.meters;
          const coordinates: List<Coordinates> = routeLegFragment.latLons.map(f => {
            const lon = parseFloat(f.longitude);
            const lat = parseFloat(f.latitude);
            return fromLonLat([lon, lat]);
          });

          return new PlanLegFragment(sink, meters, coordinates);
        });

        console.log("DEBUG PlannerEngineImpl received leg " + legId);

        this.context.updatePlanLeg(legId, fragments);
        const leg = new PlanLeg(legId, source, sink, fragments);
        this.context.legCache.add(leg);
        const coordinates = fragments.flatMap(f => f.coordinates);
        this.context.routeLayer.addRouteLeg(legId, coordinates);
      } else {
        // TODO handle leg not found
      }
    });
    return new PlanLeg(legId, source, sink, List());
  }

}
