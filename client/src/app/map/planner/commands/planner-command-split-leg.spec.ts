import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";

describe("PlannerCommandSplitLeg", () => {

  it("split leg - do and undo", () => {

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);

    const oldLeg = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.via("n2", node2), null, List());
    const newLeg1 = new PlanLeg("13", "", legEnd1, legEnd3, PlanFlag.via("n3", node3), null, List());
    const newLeg2 = new PlanLeg("32", "", legEnd3, legEnd2, PlanFlag.via("n2", node2), null, List());

    const setup = new PlannerTestSetup();

    setup.context.legs.add(oldLeg);
    setup.context.legs.add(newLeg1);
    setup.context.legs.add(newLeg2);

    const plan = new Plan(node1, PlanFlag.start("n1", node1), List([oldLeg]));
    setup.context.routeLayer.addPlanLeg(oldLeg);
    setup.context.updatePlan(plan);

    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    const command = new PlannerCommandSplitLeg(oldLeg.featureId, newLeg1.featureId, newLeg2.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectViaFlagExists(node3.featureId, [3, 3]);

    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("13", newLeg1);
    setup.routeLayer.expectRouteLegExists("32", newLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("32");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1002");

  });

});
