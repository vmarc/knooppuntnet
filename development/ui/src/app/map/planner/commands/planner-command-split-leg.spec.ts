import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";

describe("PlannerCommandSplitLeg", () => {

  it("split leg - do and undo", () => {

    const node1 = PlanNode.create("1001", "01", [1, 1]);
    const node2 = PlanNode.create("1002", "02", [2, 2]);
    const node3 = PlanNode.create("1003", "03", [3, 3]);

    const oldLeg = new PlanLeg("12", node1, node2, 0, List());
    const newLeg1 = new PlanLeg("13", node1, node3, 0, List());
    const newLeg2 = new PlanLeg("32", node3, node2, 0, List());

    const setup = new PlannerTestSetup();

    setup.context.legs.add(oldLeg);
    setup.context.legs.add(newLeg1);
    setup.context.legs.add(newLeg2);

    const plan = Plan.create(node1, List([oldLeg]));
    setup.context.routeLayer.addRouteLeg(oldLeg);
    setup.context.updatePlan(plan);

    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    const command = new PlannerCommandSplitLeg(oldLeg.featureId, newLeg1.featureId, newLeg2.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node3.featureId, [3, 3]);

    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("13", newLeg1);
    setup.routeLayer.expectRouteLegExists("32", newLeg2);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("32");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1002");

  });

});
