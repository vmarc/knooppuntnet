import {List} from "immutable";
import {FlagFeature} from "../features/flag-feature";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";

describe("PlannerNodeDragAnalyzer", () => {

  const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 0]);
  const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 0]);
  const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 0]);
  const node4 = PlanUtil.planNodeWithCoordinate("1004", "03", [4, 0]);

  const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
  const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
  const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);
  const legEnd4 = PlanUtil.legEndNode(+node4.nodeId);

  const leg12 = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.oldVia(node2), null, List());
  const leg23 = new PlanLeg("23", "", legEnd2, legEnd3, PlanFlag.oldVia(node3), null, List());
  const leg34 = new PlanLeg("34", "", legEnd3, legEnd4, PlanFlag.oldVia(node4), null, List());

  it("start start-point drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.start(node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node1.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1001");
  });

  it("start via-point 2 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node2.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node3.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1002");
  });

  it("start via-point 3 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node3.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node2.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1003");
  });

  it("cannot initiate drag of via node on plan with no legs", () => {
    const plan = new Plan(node1, List());
    const flag = FlagFeature.via(node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);
    expect(drag).toBeNull();
  });

  it("end-point", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node4.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node4.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1004");
  });

});
