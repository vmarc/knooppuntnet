import {List} from "immutable";
import {PlannerCommand} from "./planner-command";
import {PlannerCommandStack} from "./planner-command-stack";

/*
  Collection with planner commands that have been executed. We keep the details of these
  commands to be able to support undo() and redo() operations.  The actual execution of
  the commands has to be done outside of this class.

  How to call:

    push:
      stack.push(command);
      command.do(context);

    undo:
      command = stack.undo();
      command.undo(context);

    redo:
      command = stack.redo();
      command.do(context);

*/
export class PlannerCommandStackImpl implements PlannerCommandStack {

  private _commandCount: number = 0;
  private commands: List<PlannerCommand> = List();

  private _canUndo = false;
  private _canRedo = false;

  /*
    Number of commands in the command stack, NOT including that commands
    that are available for "redo" operations.
  */
  public get commandCount(): number {
    return this._commandCount;
  }

  /*
    Number of commands in the command stack, also including that commands
    that are available for "redo" operations.
  */
  public get size(): number {
    return this.commands.size;
  }

  /*
    Adds a command to the stack. Any commands that were available for redo() will be lost now.
  */
  public push(command: PlannerCommand) {
    if (this.commands.size > this.commandCount) {
      this.commands = this.commands.setSize(this.commandCount);
    }
    this.commands = this.commands.push(command);
    this._commandCount++;
    this.updateCanUndoRedo();
  }

  /*
    Return the command on the top of the logical stack to make it available for
    an undo() operation. The actual command remains on the physical stack to keep
    it available for a redo() operation.
  */
  public undo(): PlannerCommand {
    if (this.commandCount > 0) {
      this._commandCount--;
      const command = this.commands.get(this.commandCount);
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  public redo(): PlannerCommand {
    if (this.commandCount < this.commands.size) {
      const command = this.commands.get(this.commandCount);
      this._commandCount++;
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  public get canUndo(): boolean {
    return this._canUndo;
  }

  public get canRedo(): boolean {
    return this._canRedo;
  }

  private updateCanUndoRedo() {
    this.updateCanUndo();
    this.updateCanRedo();
  }

  private updateCanUndo() {
    this._canUndo = this.commandCount > 0;
  }

  private updateCanRedo() {
    this._canRedo = this.commands.size > this.commandCount;
  }
}
