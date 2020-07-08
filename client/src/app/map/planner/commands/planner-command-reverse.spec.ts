import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanReverser} from "../plan/plan-reverser";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandReverse} from "./planner-command-reverse";

describe("PlannerCommandReverse", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const sinkFlag1 = PlanFlag.via("sinkFlag1", [2, 2]);
    const sinkFlag2 = PlanFlag.end("sinkFlag2", [3, 3]);

    const leg1 = PlanUtil.singleRoutePlanLeg("12", setup.node1, setup.node2, sinkFlag1, null);
    const leg2 = PlanUtil.singleRoutePlanLeg("23", setup.node2, setup.node3, sinkFlag2, null);

    setup.legs.add(leg1);
    setup.legs.add(leg2);

    setup.context.execute(new PlannerCommandAddStartPoint(setup.node1, sourceFlag));
    setup.context.execute(new PlannerCommandAddLeg(leg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(leg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("sinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("sinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");

    const reverseCommand = new PlanReverser(setup.context).reverse(setup.context.plan);
    setup.context.execute(reverseCommand);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectPlanFlagExists(setup.context.plan.sourceFlag);
    setup.routeLayer.expectPlanFlagExists(setup.context.plan.legs.get(0).sinkFlag);
    setup.routeLayer.expectPlanFlagExists(setup.context.plan.legs.get(1).sinkFlag);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1003");
    expect(setup.context.plan.sourceFlag.coordinate).toEqual([3, 3]);
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).sinkFlag.coordinate).toEqual([2, 2]);
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(1).sinkFlag.coordinate).toEqual([1, 1]);
    expect(setup.context.plan.legs.get(1).sinkFlag.flagType).toEqual(PlanFlagType.End);

    reverseCommand.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("sinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("sinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");

  });

});
