import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandReset} from "./planner-command-reset";

describe("PlannerCommandReset", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const sinkFlag1 = PlanFlag.via("viaFlag", [2, 2]);
    const sinkFlag2 = PlanFlag.end("endFlag", [3, 3]);

    const leg1 = PlanUtil.singleRoutePlanLeg("12", setup.node1, setup.node2, sinkFlag1, null);
    const leg2 = PlanUtil.singleRoutePlanLeg("23", setup.node2, setup.node3, sinkFlag2, null);

    setup.context.execute(new PlannerCommandAddStartPoint(setup.node1, sourceFlag));
    setup.context.execute(new PlannerCommandAddLeg(leg1));
    setup.context.execute(new PlannerCommandAddLeg(leg2));

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("viaFlag", [2, 2]);
    setup.markerLayer.expectEndFlagExists("endFlag", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("viaFlag");
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("endFlag");
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);

    const resetCommand = new PlannerCommandReset();
    setup.context.execute(resetCommand);

    expect(setup.context.plan.sourceNode).toEqual(null);
    expect(setup.context.plan.sourceFlag).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);

    resetCommand.undo(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("viaFlag", [2, 2]);
    setup.markerLayer.expectEndFlagExists("endFlag", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("viaFlag");
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("endFlag");
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);

  });

});
