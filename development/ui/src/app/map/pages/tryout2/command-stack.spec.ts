import {PlannerCommand} from "./planner-command";
import {PlannerContext} from "./planner-context";
import {PlannerCommandStack} from "./planner-command-stack";
import {PlannerCommandStackImpl} from "./planner-command-stack-impl";

class TestCommand implements PlannerCommand {

  constructor(public name: string) {
  }

  do(context: PlannerContext) {
  }

  undo(context: PlannerContext) {
  }
}

describe("PlannerCommandStack", () => {

  let stack: PlannerCommandStack;

  beforeEach(() => {
    stack = new PlannerCommandStackImpl();
  });

  it("empty stack", () => {
    expect(stack.commandCount).toEqual(0);
    expect(stack.size).toEqual(0);
    expect(stack.canUndo).toEqual(false);
    expect(stack.canRedo).toEqual(false);
    expect(stack.undo()).toEqual(null);
    expect(stack.redo()).toEqual(null);
  });

  it("push/pop command", () => {

    const command = new TestCommand("a");

    stack.push(command);
    expect(stack.commandCount).toEqual(1);
    expect(stack.size).toEqual(1);
    expect(stack.canUndo).toEqual(true);
    expect(stack.canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command);
    expect(stack.commandCount).toEqual(0);
    expect(stack.size).toEqual(1);
    expect(stack.canUndo).toEqual(false);
    expect(stack.canRedo).toEqual(true);
  });

  it("undo/redo", () => {

    const command1 = new TestCommand("1");
    const command2 = new TestCommand("2");

    stack.push(command1);
    stack.push(command2);

    expect(stack.commandCount).toEqual(2);
    expect(stack.size).toEqual(2);
    expect(stack.canUndo).toEqual(true);
    expect(stack.canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command2);
    expect(stack.commandCount).toEqual(1);
    expect(stack.size).toEqual(2);
    expect(stack.canUndo).toEqual(true);
    expect(stack.canRedo).toEqual(true);

    expect(stack.redo()).toEqual(command2);
    expect(stack.commandCount).toEqual(2);
    expect(stack.size).toEqual(2);
    expect(stack.canUndo).toEqual(true);
    expect(stack.canRedo).toEqual(false);

  });

  it("undo/push", () => {

    const command1 = new TestCommand("1");
    const command2 = new TestCommand("2");
    const command3 = new TestCommand("3");

    stack.push(command1);
    stack.push(command2);

    expect(stack.undo()).toEqual(command2);
    stack.push(command3);

    expect(stack.commandCount).toEqual(2);
    expect(stack.size).toEqual(2);
    expect(stack.canUndo).toEqual(true);
    expect(stack.canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command3);
    expect(stack.undo()).toEqual(command1);

  });

});
