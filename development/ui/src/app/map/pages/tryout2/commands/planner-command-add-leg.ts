import {List} from "immutable";
import {PlannerCommand} from "./planner-command";
import {PlanNode} from "../plan/plan-node";
import {PlannerContext} from "../planner-context";
import {PlanLegFragment} from "../plan/plan-leg-fragment";
import {PlanLeg} from "../plan/plan-leg";
import {Plan} from "../plan/plan";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(private legId: string,
              private legSource: PlanNode,
              private legSink: PlanNode) {
  }

  public do(context: PlannerContext) {
    context.routeLayer.addViaNodeFlag(this.legId, this.legSink.nodeId, this.legSink.coordinate);
    let fragments: List<PlanLegFragment> = List();
    const cachedLeg = context.legCache.get(this.legSource.nodeId, this.legSink.nodeId);
    if (cachedLeg) {
      fragments = cachedLeg.fragments;
      const coordinates = fragments.flatMap(f => f.coordinates);
      context.routeLayer.addRouteLeg(this.legId, coordinates);
    }
    const leg = new PlanLeg(this.legId, this.legSource, this.legSink, fragments);
    const newLegs = context.plan.legs.push(leg);
    const newPlan = new Plan(context.plan.source, newLegs);
    context.updatePlan(newPlan);
  }

  public undo(context: PlannerContext) {
    const legs = context.plan.legs;
    const newLegs = legs.setSize(legs.size - 1);
    const plan = new Plan(context.plan.source, newLegs);
    context.updatePlan(plan);
    context.routeLayer.removeRouteLeg(this.legId);
    context.routeLayer.removeViaNodeFlag(this.legId, this.legSink.nodeId);
  }

}
