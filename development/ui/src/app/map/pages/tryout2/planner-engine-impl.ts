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

export class PlannerEngineImpl implements PlannerEngine {

  private legIdGenerator = 0;

  // construct layers here???

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

  public undo(): void {
    const command = this.context.commandStack.undo();
    command.undo(this.context);
  }

  public redo(): void {
    const command = this.context.commandStack.redo();
    command.do(this.context);
  }

}
