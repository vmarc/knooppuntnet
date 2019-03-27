import {PlannerCommand, PlannerCommandStack, PlannerCommandStackImpl, PlannerContext} from "./command-stack";

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
  let canUndo: boolean;
  let canRedo: boolean;

  beforeEach(() => {
    stack = new PlannerCommandStackImpl();
    stack.canUndo.subscribe(newValue => canUndo = newValue);
    stack.canRedo.subscribe(newValue => canRedo = newValue);
  });

  it("empty stack", () => {
    expect(stack.commandCount).toEqual(0);
    expect(stack.size).toEqual(0);
    expect(canUndo).toEqual(false);
    expect(canRedo).toEqual(false);
    expect(stack.undo()).toEqual(null);
    expect(stack.redo()).toEqual(null);
  });

  it("push/pop command", () => {

    const command = new TestCommand("a");

    stack.push(command);
    expect(stack.commandCount).toEqual(1);
    expect(stack.size).toEqual(1);
    expect(canUndo).toEqual(true);
    expect(canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command);
    expect(stack.commandCount).toEqual(0);
    expect(stack.size).toEqual(1);
    expect(canUndo).toEqual(false);
    expect(canRedo).toEqual(true);
  });

  it("undo/redo", () => {

    const command1 = new TestCommand("1");
    const command2 = new TestCommand("2");

    stack.push(command1);
    stack.push(command2);

    expect(stack.commandCount).toEqual(2);
    expect(stack.size).toEqual(2);
    expect(canUndo).toEqual(true);
    expect(canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command2);
    expect(stack.commandCount).toEqual(1);
    expect(stack.size).toEqual(2);
    expect(canUndo).toEqual(true);
    expect(canRedo).toEqual(true);

    expect(stack.redo()).toEqual(command2);
    expect(stack.commandCount).toEqual(2);
    expect(stack.size).toEqual(2);
    expect(canUndo).toEqual(true);
    expect(canRedo).toEqual(false);

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
    expect(canUndo).toEqual(true);
    expect(canRedo).toEqual(false);

    expect(stack.undo()).toEqual(command3);
    expect(stack.undo()).toEqual(command1);

  });

});
