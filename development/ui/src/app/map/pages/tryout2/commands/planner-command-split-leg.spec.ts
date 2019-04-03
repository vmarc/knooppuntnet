import {List} from "immutable";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegFragment} from "../plan/plan-leg-fragment";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";

describe("PlannerCommandSplitLeg", () => {

  it("split leg - do and undo", () => {

    const coordinate1 = [1, 1];
    const coordinate2 = [2, 2];
    const coordinate3 = [3, 3];

    const node1: PlanNode = new PlanNode("1001", "01", coordinate1);
    const node2: PlanNode = new PlanNode("1002", "02", coordinate2);
    const node3: PlanNode = new PlanNode("1003", "03", coordinate3);

    const f1 = new PlanLegFragment(node2, 10, List());
    const f2 = new PlanLegFragment(node3, 10, List());
    const f3 = new PlanLegFragment(node2, 10, List());

    const oldLeg: PlanLeg = new PlanLeg("12", node1, node2, List([f1]));
    const newLeg1: PlanLeg = new PlanLeg("13", node1, node3, List([f2]));
    const newLeg2: PlanLeg = new PlanLeg("32", node3, node2, List([f3]));

    const command = new PlannerCommandSplitLeg(oldLeg, newLeg1, newLeg2);

    const context /*: PlannerContext */ = jasmine.createSpyObj(
      "context",
      [
        "plan",
        "addViaNodeFlag",
        "removeRouteLeg",
        "addRouteLeg",
        "updatePlan"
      ]
    );

    const plan = new Plan(node1, List([oldLeg]));
    context.plan.and.returnValue(plan);

    command.do(context);

    expect(context.addViaNodeFlag).toHaveBeenCalledWith("13", "1003", [3, 3]);
    expect(context.removeRouteLeg).toHaveBeenCalledWith("12");
    expect(context.addRouteLeg).toHaveBeenCalledTimes(2);
    expect(context.addRouteLeg).toHaveBeenCalledWith("13", List(/*[coordinate3]*/));
    expect(context.addRouteLeg).toHaveBeenCalledWith("32", List(/*[coordinate2]*/));

    const newPlan: Plan = context.updatePlan.calls.mostRecent().args[0];

    expect(newPlan.source.nodeId).toEqual("1001");
    expect(newPlan.legs.get(0).legId).toEqual("13");
    expect(newPlan.legs.get(0).source.nodeId).toEqual("1001");
    expect(newPlan.legs.get(0).sink.nodeId).toEqual("1003");
    expect(newPlan.legs.get(1).legId).toEqual("32");
    expect(newPlan.legs.get(1).source.nodeId).toEqual("1003");
    expect(newPlan.legs.get(1).sink.nodeId).toEqual("1002");

  });

});
