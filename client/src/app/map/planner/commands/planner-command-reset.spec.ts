import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandReset} from "./planner-command-reset";

describe("PlannerCommandReset", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);

    const leg1 = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.via("n2", [2, 2]), null, List());
    const leg2 = new PlanLeg("23", "", legEnd2, legEnd3, PlanFlag.end("n3", [3, 3]), null, List());

    setup.legs.add(leg1);
    setup.legs.add(leg2);

    setup.context.execute(new PlannerCommandAddStartPoint(node1, PlanFlag.start("n1", [1, 1])));
    setup.context.execute(new PlannerCommandAddLeg(leg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(leg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("n1", [1, 1]);
    setup.routeLayer.expectViaFlagExists("n2", [2, 2]);
    setup.routeLayer.expectEndFlagExists("n3", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("n3");
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);

    const resetCommand = new PlannerCommandReset();
    setup.context.execute(resetCommand);

    expect(setup.context.plan.sourceNode).toEqual(null);
    expect(setup.context.plan.sourceFlag).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);

    resetCommand.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("n1", [1, 1]);
    setup.routeLayer.expectViaFlagExists("n2", [2, 2]);
    setup.routeLayer.expectEndFlagExists("n3", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("n3");
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);
  });

});
