import {List} from "immutable";
import {PlannerMapFeature} from "../features/planner-map-feature";
import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";

describe("PlannerNodeDragAnalyzer", () => {

  const coordinate1 = [1, 0];
  const coordinate2 = [2, 0];
  const coordinate3 = [3, 0];
  const coordinate4 = [4, 0];

  const node1 = PlanNode.create("1001", "01", coordinate1);
  const node2 = PlanNode.create("1002", "02", coordinate2);
  const node3 = PlanNode.create("1003", "03", coordinate3);
  const node4 = PlanNode.create("1004", "03", coordinate4);

  const leg12 = new PlanLeg("12", node1, node2, 0, List());
  const leg23 = new PlanLeg("23", node2, node3, 0, List());
  const leg34 = new PlanLeg("34", node3, node4, 0, List());

  it("start start-point drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = PlannerMapFeature.startFlag(node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(coordinate1);
    expect(drag.anchor2).toEqual(coordinate1);
    expect(drag.oldNode.nodeId).toEqual("1001");
  });

  it("start via-point 2 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = PlannerMapFeature.flag(PlanFlagType.Via, node2.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(coordinate1);
    expect(drag.anchor2).toEqual(coordinate3);
    expect(drag.oldNode.nodeId).toEqual("1002");
  });

  it("start via-point 3 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = PlannerMapFeature.flag(PlanFlagType.Via, node3.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(coordinate2);
    expect(drag.anchor2).toEqual(coordinate4);
    expect(drag.oldNode.nodeId).toEqual("1003");
  });

  it("cannot initiate drag of via node on plan with no legs", () => {
    const plan = new Plan(node1, List());
    const flag = PlannerMapFeature.flag(PlanFlagType.Via, node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);
    expect(drag).toBeNull();
  });

  it("end-point", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const flag = PlannerMapFeature.flag(PlanFlagType.Via, node4.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(coordinate4);
    expect(drag.anchor2).toEqual(coordinate4);
    expect(drag.oldNode.nodeId).toEqual("1004");
  });

});
