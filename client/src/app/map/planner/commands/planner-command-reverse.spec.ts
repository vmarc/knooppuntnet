import {List} from "immutable";
import {PlanFragment} from "../../../kpn/api/common/planner/plan-fragment";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {PlanSegment} from "../../../kpn/api/common/planner/plan-segment";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanReverser} from "../plan/plan-reverser";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandReverse} from "./planner-command-reverse";

describe("PlannerCommandReverse", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);

    const fragment1 = new PlanFragment(0, 0, -1, node2.coordinate, node2.latLon);
    const segment1 = new PlanSegment(0, "", null, List([fragment1]));
    const route1 = new PlanRoute(node1, node2, 0, List([segment1]), List());
    const leg1 = new PlanLeg("12", "1001-1002", legEnd1, legEnd2, PlanFlag.via("n2", node2), null, List([route1]));

    const fragment2 = new PlanFragment(0, 0, -1, node3.coordinate, node3.latLon);
    const segment2 = new PlanSegment(0, "", null, List([fragment2]));
    const route2 = new PlanRoute(node2, node3, 0, List([segment2]), List());
    const leg2 = new PlanLeg("23", "1002-1003", legEnd2, legEnd3, PlanFlag.end("n3", node3), null, List([route2]));

    setup.legs.add(leg1);
    setup.legs.add(leg2);

    setup.context.execute(new PlannerCommandAddStartPoint(node1, PlanFlag.start("n1", node1)));
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
    setup.routeLayer.expectStartFlagExists("n1", [1, 1]);
    setup.routeLayer.expectViaFlagExists("n2", [2, 2]);
    setup.routeLayer.expectEndFlagExists("n3", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
  });

});
