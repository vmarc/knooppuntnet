import {BehaviorSubject, Observable} from "rxjs";
import {List} from "immutable";
import {PlannerCommandStack} from "./planner-command-stack";
import {PlannerCommand} from "./planner-command";

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

  private commandCount_: number = 0;
  private commands: List<PlannerCommand> = List();

  private canUndo_ = new BehaviorSubject<boolean>(false);
  private canRedo_ = new BehaviorSubject<boolean>(false);

  /*
    Number of commands in the command stack, NOT including that commands
    that are available for "redo" operations.
  */
  public get commandCount(): number {
    return this.commandCount_;
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
    this.commandCount_++;
    this.updateCanUndoRedo();
  }

  /*
    Return the command on the top of the logical stack to make it available for
    an undo() operation. The actual command remains on the physical stack to keep
    it available for a redo() operation.
  */
  public undo(): PlannerCommand {
    if (this.commandCount > 0) {
      this.commandCount_--;
      const command = this.commands.get(this.commandCount);
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  public redo(): PlannerCommand {
    if (this.commandCount < this.commands.size) {
      const command = this.commands.get(this.commandCount);
      this.commandCount_++;
      this.updateCanUndoRedo();
      return command;
    }
    return null;
  }

  public get canUndo(): Observable<boolean> {
    return this.canUndo_;
  }

  public get canRedo(): Observable<boolean> {
    return this.canRedo_;
  }

  private updateCanUndoRedo() {
    this.updateCanUndo();
    this.updateCanRedo();
  }

  private updateCanUndo() {
    const newValue = this.commandCount > 0;
    if (this.canUndo_.value !== newValue) {
      this.canUndo_.next(newValue);
    }
  }

  private updateCanRedo() {
    const newValue = this.commands.size > this.commandCount;
    if (this.canRedo_.value !== newValue) {
      this.canRedo_.next(newValue);
    }
  }
}
