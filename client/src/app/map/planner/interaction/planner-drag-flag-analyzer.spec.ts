import {List} from "immutable";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerDragFlagAnalyzer} from "./planner-drag-flag-analyzer";
import {FlagFeature} from "../features/flag-feature";

describe("PlannerNodeDragAnalyzer", () => {

  const node1 = PlanNode.withCoordinate("1001", "01", [1, 0]);
  const node2 = PlanNode.withCoordinate("1002", "02", [2, 0]);
  const node3 = PlanNode.withCoordinate("1003", "03", [3, 0]);
  const node4 = PlanNode.withCoordinate("1004", "03", [4, 0]);

  const leg12 = new PlanLeg("12", node1, node2, 0, List());
  const leg23 = new PlanLeg("23", node2, node3, 0, List());
  const leg34 = new PlanLeg("34", node3, node4, 0, List());

  it("start start-point drag", () => {

    const plan = Plan.create(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.start(node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node1.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1001");
  });

  it("start via-point 2 drag", () => {

    const plan = Plan.create(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node2.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node3.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1002");
  });

  it("start via-point 3 drag", () => {

    const plan = Plan.create(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node3.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node2.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1003");
  });

  it("cannot initiate drag of via node on plan with no legs", () => {
    const plan = Plan.create(node1, List());
    const flag = FlagFeature.via(node1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);
    expect(drag).toBeNull();
  });

  it("end-point", () => {

    const plan = Plan.create(node1, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(node4.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node4.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual("1004");
  });

});
