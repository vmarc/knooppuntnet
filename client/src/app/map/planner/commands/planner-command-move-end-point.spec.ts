import {List} from "immutable";
import {TestSupport} from "../../../util/test-support";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandMoveEndPoint} from "./planner-command-move-end-point";

describe("PlannerCommandMoveEndPoint", () => {

  it("move end point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);

    const oldLeg = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.end("n2", [2, 2]), null, List());
    const newLeg = new PlanLeg("13", "", legEnd1, legEnd3, PlanFlag.end("n3", [3, 3]), null, List());

    setup.legs.add(oldLeg);
    setup.legs.add(newLeg);

    const plan = new Plan(node1, PlanFlag.start("n1", [1, 1]), List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveEndPoint("12", "13");
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectEndFlagExists("n3", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, "n3", [3, 3]);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectEndFlagExists("n2", [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, "n2", [2, 2]);
  });

});
