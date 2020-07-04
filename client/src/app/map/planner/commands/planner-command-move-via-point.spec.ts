import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandMoveViaPoint} from "./planner-command-move-via-point";

describe("PlannerCommandMoveViaPoint", () => {

  it("move via point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
    const node4 = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);
    const legEnd4 = PlanUtil.legEndNode(+node4.nodeId);

    const oldLeg1 = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.via("n2", node2), null, List());
    const oldLeg2 = new PlanLeg("23", "", legEnd2, legEnd3, PlanFlag.via("n3", node3), null, List());
    const newLeg1 = new PlanLeg("14", "", legEnd1, legEnd4, PlanFlag.via("n4", node4), null, List());
    const newLeg2 = new PlanLeg("43", "", legEnd4, legEnd3, PlanFlag.via("n3", node3), null, List());

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg1);
    setup.legs.add(newLeg2);

    const plan = new Plan(node1, PlanFlag.start("n1", node1), List([oldLeg1, oldLeg2]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveViaPoint(oldLeg1.featureId, oldLeg2.featureId, newLeg1.featureId, newLeg2.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectViaFlagExists(node4.featureId, [4, 4]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("14", newLeg1);
    setup.routeLayer.expectRouteLegExists("43", newLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("14");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1004");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("43");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1004");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectViaFlagExists(node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");

  });

});
