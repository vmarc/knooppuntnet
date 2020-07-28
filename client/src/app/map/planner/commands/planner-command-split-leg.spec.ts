import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddPlan} from "./planner-command-add-plan";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";

describe("PlannerCommandSplitLeg", () => {

  it("split leg - do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const endFlag = PlanFlag.end("endFlag", [2, 2]);
    const viaFlag = PlanFlag.via("viaFlag", [3, 3]);

    const oldLeg = PlanUtil.singleRoutePlanLeg("12", setup.node1, setup.node2, endFlag, null);
    const newLeg1 = PlanUtil.singleRoutePlanLeg("13", setup.node1, setup.node3, viaFlag, null);
    const newLeg2 = PlanUtil.singleRoutePlanLeg("32", setup.node3, setup.node2, endFlag, null);

    const plan = new Plan(setup.node1, sourceFlag, List([oldLeg]));
    setup.context.execute(new PlannerCommandAddPlan(plan));

    const command = new PlannerCommandSplitLeg(oldLeg, newLeg1, newLeg2);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("viaFlag", [3, 3]);
    setup.markerLayer.expectEndFlagExists("endFlag", [2, 2]);

    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("13", newLeg1);
    setup.routeLayer.expectRouteLegExists("32", newLeg2);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(0).sinkFlag.coordinate).toEqual([3, 3]);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("32");
    expect(setup.context.plan.legs.get(1).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(1).sinkFlag.coordinate).toEqual([2, 2]);

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectEndFlagExists("endFlag", [2, 2]);

    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(0).sinkFlag.coordinate).toEqual([2, 2]);

  });

});
