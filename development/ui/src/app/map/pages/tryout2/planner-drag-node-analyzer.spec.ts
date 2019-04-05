import {List} from "immutable";
import Coordinate from 'ol/coordinate';
import {PlannerDragNodeAnalyzer} from "./planner-drag-node-analyzer";
import {PlanLeg} from "./plan/plan-leg";
import {PlanNode} from "./plan/plan-node";
import {Plan} from "./plan/plan";

describe("PlannerNodeDragAnalyzer", () => {

  const coordinate1 = new Coordinate([1, 0]);
  const coordinate2 = new Coordinate([2, 0]);
  const coordinate3 = new Coordinate([3, 0]);
  const coordinate4 = new Coordinate([4, 0]);

  const node1 = new PlanNode("1001", "01", coordinate1);
  const node2 = new PlanNode("1002", "02", coordinate2);
  const node3 = new PlanNode("1003", "03", coordinate3);
  const node4 = new PlanNode("1004", "03", coordinate4);

  const leg12 = new PlanLeg("12", node1, node2, List());
  const leg23 = new PlanLeg("23", node2, node3, List());
  const leg34 = new PlanLeg("34", node3, node4, List());

  it("cannot initiate drag on empty plan", () => {
    const plan = Plan.empty();
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("start-node-flag-1001", "1001");
    expect(drag).toBeNull();
  });

  it("cannot initiate drag on plan with no legs", () => {
    const plan = new Plan(node1, List());
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("start-node-flag-1001", "1001");
    expect(drag).toBeNull();
  });

  it("start start-point drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("start-node-flag-1001", "1001");

    expect(drag.anchor1).toEqual(coordinate1);
    expect(drag.anchor2).toEqual(coordinate1);
    expect(drag.oldNode.nodeId).toEqual("1001");
  });

  it("start via-point 2 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("leg-12-via-node-1002", "1002");

    expect(drag.anchor1).toEqual(coordinate1);
    expect(drag.anchor2).toEqual(coordinate3);
    expect(drag.oldNode.nodeId).toEqual("1002");
  });

  it("start via-point 3 drag", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("leg-23-via-node-1003", "1003");

    expect(drag.anchor1).toEqual(coordinate2);
    expect(drag.anchor2).toEqual(coordinate4);
    expect(drag.oldNode.nodeId).toEqual("1003");
  });

  it("end-point", () => {

    const plan = new Plan(node1, List([leg12, leg23, leg34]));
    const drag = new PlannerDragNodeAnalyzer(plan).dragStarted("leg-34-via-node-1004", "1004");

    expect(drag.anchor1).toEqual(coordinate4);
    expect(drag.anchor2).toEqual(coordinate4);
    expect(drag.oldNode.nodeId).toEqual("1004");
  });

});
