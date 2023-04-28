import { List } from 'immutable';
import { PlannerCommand } from './planner-command';

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
export class PlannerCommandStack {
  private _commands: List<PlannerCommand> = List();

  private _commandCount = 0;

  /*
    Number of commands in the command stack, NOT including that commands
    that are available for "redo" operations.
  */
  get commandCount(): number {
    return this._commandCount;
  }

  private _canUndo = false;

  get canUndo(): boolean {
    return this._canUndo;
  }

  private _canRedo = false;

  get canRedo(): boolean {
    return this._canRedo;
  }

  /*
    Number of commands in the command stack, also including that commands
    that are available for "redo" operations.
  */
  get size(): number {
    return this._commands.size;
  }

  /*
    Adds a command to the stack. Any commands that were available for redo() will be lost now.
  */
  push(command: PlannerCommand) {
    if (this._commands.size > this.commandCount) {
      this._commands = this._commands.setSize(this.commandCount);
    }
    this._commands = this._commands.push(command);
    this._commandCount++;
    this.updateCanUndoRedo();
  }

  /*
    Return the command on the top of the logical stack to make it available for
    an undo() operation. The actual command remains on the physical stack to keep
    it available for a redo() operation.
  */
  undo(): PlannerCommand {
    if (this.commandCount > 0) {
      this._commandCount--;
      const command = this._commands.get(this.commandCount);
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  redo(): PlannerCommand {
    if (this.commandCount < this._commands.size) {
      const command = this._commands.get(this.commandCount);
      this._commandCount++;
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  last(): PlannerCommand {
    return this._commands.get(this.commandCount - 1);
  }

  private updateCanUndoRedo() {
    this.updateCanUndo();
    this.updateCanRedo();
  }

  private updateCanUndo() {
    this._canUndo = this.commandCount > 0;
  }

  private updateCanRedo() {
    this._canRedo = this._commands.size > this.commandCount;
  }
}
