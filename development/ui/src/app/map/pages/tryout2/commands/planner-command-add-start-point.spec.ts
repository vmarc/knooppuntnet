import {Plan} from "../plan/plan";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddStartPoint", () => {

  it("add start point - do and undo", () => {

    const coordinate1 = [1, 1];
    const node1: PlanNode = new PlanNode("1001", "01", coordinate1);

    const command = new PlannerCommandAddStartPoint(node1);

    const context = jasmine.createSpyObj(
      "context",
      [
        "plan",
        "addStartNodeFlag",
        "removeStartNodeFlag",
        "updatePlan"
      ]
    );

    context.plan.and.returnValue(Plan.empty());

    command.do(context);

    expect(context.addStartNodeFlag).toHaveBeenCalledWith("1001", [1, 1]);

    const newPlan: Plan = context.updatePlan.calls.mostRecent().args[0];
    expect(newPlan.source.nodeId).toEqual("1001");
    expect(newPlan.legs.size).toEqual(0);

    command.undo(context);

    expect(context.removeStartNodeFlag).toHaveBeenCalledWith("1001");

    const undonePlan: Plan = context.updatePlan.calls.mostRecent().args[0];
    expect(undonePlan.source).toEqual(null);
    expect(newPlan.legs.size).toEqual(0);
  });

});
