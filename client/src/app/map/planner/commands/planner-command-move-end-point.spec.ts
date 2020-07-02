import {List} from "immutable";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
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

    const oldLeg = new PlanLeg("12", "", legEnd1, legEnd2, node1, node2, 0, List());
    const newLeg = new PlanLeg("13", "", legEnd1, legEnd3, node1, node3, 0, List());

    setup.legs.add(oldLeg);
    setup.legs.add(newLeg);

    const plan = new Plan(node1, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveEndPoint("12", "13");
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1003");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
  });

});
