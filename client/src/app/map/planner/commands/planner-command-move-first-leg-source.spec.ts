import {List} from "immutable";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandMoveFirstLegSource} from "./planner-command-move-first-leg-source";

describe("PlannerCommandMoveFirstLegSource", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);

    const oldLeg = new PlanLeg("12", "", legEnd1, legEnd2, node1, node2, 0, List());
    const newLeg = new PlanLeg("32", "", legEnd3, legEnd2, node3, node2, 0, List());

    setup.legs.add(oldLeg);
    setup.legs.add(newLeg);

    const plan = PlanUtil.plan(node1, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveFirstLegSource("12", "32");
    setup.context.execute(command);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("32");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.sourceNode.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("32", newLeg);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");

  });

});
