import {List} from "immutable";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {Plan} from "./plan";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";
import {PlanUtil} from "./plan-util";

describe("PlanUtil", () => {

  it("toUrlString - empty plan", () => {
    expect(PlanUtil.toUrlString(Plan.empty())).toEqual("");
  });

  it("toUrlString - plan with source only", () => {
    const startNode = PlanNode.create("10", "", new LatLonImpl("", ""));
    const plan = Plan.create(startNode, List());
    expect(PlanUtil.toUrlString(plan)).toEqual("a");
  });

  it("toUrlString - plan with multiple legs", () => {

    const startNode = PlanNode.create("10", "", new LatLonImpl("", ""));
    const viaNode1 = PlanNode.create("11", "", new LatLonImpl("", ""));
    const viaNode2 = PlanNode.create("12", "", new LatLonImpl("", ""));
    const endNode = PlanNode.create("13", "", new LatLonImpl("", ""));

    const route1 = new PlanRoute(startNode, viaNode1, 0, List(), List());
    const route2 = new PlanRoute(viaNode1, viaNode2, 0, List(), List());
    const route3 = new PlanRoute(viaNode2, endNode, 0, List(), List());

    const leg1 = new PlanLeg("", startNode, viaNode1, 0, List([route1]));
    const leg2 = new PlanLeg("", viaNode1, viaNode2, 0, List([route2]));
    const leg3 = new PlanLeg("", viaNode1, endNode, 0, List([route3]));

    const plan = Plan.create(startNode, List([leg1, leg2, leg3]));

    expect(PlanUtil.toUrlString(plan)).toEqual("a-b-c-d");
  });

  it("toNodeIds", () => {
    const nodeIds = PlanUtil.toNodeIds("a-b-c-d");
    expect(nodeIds.size).toEqual(4);
    expect(nodeIds.get(0)).toEqual("10");
    expect(nodeIds.get(1)).toEqual("11");
    expect(nodeIds.get(2)).toEqual("12");
    expect(nodeIds.get(3)).toEqual("13");
  });

});
