import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddPlan} from "./planner-command-add-plan";
import {PlannerCommandMoveViaPoint} from "./planner-command-move-via-point";

describe("PlannerCommandMoveViaPoint", () => {

  it("move via point - do, undo and redo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const oldViaFlag = PlanFlag.via("oldViaFlag", [2, 2]);
    const newViaFlag = PlanFlag.via("newViaFlag", [4, 4]);
    const sinkFlag = PlanFlag.end("sinkFlag", [3, 3]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg("12", setup.node1, setup.node2, oldViaFlag, null);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg("23", setup.node2, setup.node3, sinkFlag, null);
    const newLeg1 = PlanUtil.singleRoutePlanLeg("14", setup.node1, setup.node4, newViaFlag, null);
    const newLeg2 = PlanUtil.singleRoutePlanLeg("43", setup.node4, setup.node3, sinkFlag, null);

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg1);
    setup.legs.add(newLeg2);

    const plan = new Plan(setup.node1, sourceFlag, List([oldLeg1, oldLeg2]));
    setup.context.execute(new PlannerCommandAddPlan(plan));

    const command = new PlannerCommandMoveViaPoint(oldLeg1.featureId, oldLeg2.featureId, newLeg1.featureId, newLeg2.featureId);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("newViaFlag", [4, 4]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("14", newLeg1);
    setup.routeLayer.expectRouteLegExists("43", newLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("14");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("43");

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("oldViaFlag", [2, 2]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");

    command.do(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("newViaFlag", [4, 4]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("14", newLeg1);
    setup.routeLayer.expectRouteLegExists("43", newLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("14");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("43");

  });

});
