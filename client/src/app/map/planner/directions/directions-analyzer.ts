import {List} from "immutable";
import {Plan} from "../plan/plan";
import {PlanFragment} from "../plan/plan-fragment";
import {PlanInstruction} from "../plan/plan-instruction";

export class DirectionsAnalyzer {

  private previousFragment: PlanFragment = null;
  private previousStreet: string = null;
  private currentInstruction: PlanInstruction = null;
  private instructions: List<PlanInstruction> = List();

  analyze(plan: Plan): List<PlanInstruction> {
    if (plan.source != null) {
      this.addNodeInstruction(plan.source.nodeName);

      plan.legs.forEach(leg => {
        leg.routes.forEach(route => {
          route.segments.forEach(segment => {
            segment.fragments.forEach(fragment => {

              let street = "";
              if (fragment.streetIndex != null) {
                street = route.streets.get(fragment.streetIndex);
              }

              let command = "continue";
              if (this.previousFragment == null) {
                this.newInstruction();
                this.setInstructionHeading(this.calculateHeading(fragment.orientation));
                this.setInstructionStreet(street);
                this.setInstructionCommand(command);
                this.addInstructionDistance(fragment.meters);
                this.pushInstruction();
              } else {
                const delta = fragment.orientation - this.previousFragment.orientation;
                command = this.calculateCommand(delta);
                if (this.isDifferentStreet(this.previousStreet, street) || this.isHardTurn(command)) {
                  this.pushInstruction();
                  this.setInstructionCommand(command);
                  this.setInstructionStreet(street);
                  this.addInstructionDistance(fragment.meters);
                } else {
                  if (this.currentInstruction.distance === 0) {
                    this.setInstructionCommand(command);
                    this.setInstructionStreet(street);
                  }
                  this.addInstructionDistance(fragment.meters);
                }

              }
              this.previousStreet = street;
              this.previousFragment = fragment;
            });
          });
          this.pushInstruction();
          this.addNodeInstruction(route.sink.nodeName);
        });
      });
    }

    return this.instructions;
  }

  private newInstruction(): void {
    this.currentInstruction = new PlanInstruction(
      null,
      null,
      null,
      null,
      0
    );
  }

  private addNodeInstruction(nodeName: string): void {
    const instruction = new PlanInstruction(
      nodeName,
      null,
      null,
      null,
      null
    );
    this.addInstruction(instruction);
  }

  private setInstructionCommand(command: string): void {
    this.currentInstruction = new PlanInstruction(
      this.currentInstruction.node,
      command,
      this.currentInstruction.heading,
      this.currentInstruction.street,
      this.currentInstruction.distance
    );
  }

  private setInstructionHeading(heading: string): void {
    this.currentInstruction = new PlanInstruction(
      this.currentInstruction.node,
      this.currentInstruction.command,
      heading,
      this.currentInstruction.street,
      this.currentInstruction.distance
    );
  }

  private setInstructionStreet(street: string): void {
    this.currentInstruction = new PlanInstruction(
      this.currentInstruction.node,
      this.currentInstruction.command,
      this.currentInstruction.heading,
      street,
      this.currentInstruction.distance
    );
  }

  private addInstructionDistance(distance: number): void {
    this.currentInstruction = new PlanInstruction(
      this.currentInstruction.node,
      this.currentInstruction.command,
      this.currentInstruction.heading,
      this.currentInstruction.street,
      this.currentInstruction.distance + distance
    );
  }

  private pushInstruction(): void {
    if (this.currentInstruction.distance > 0) {
      this.addInstruction(this.currentInstruction);
    }
    this.newInstruction();
  }

  private addInstruction(instruction: PlanInstruction): void {
    this.instructions = this.instructions.push(instruction);
  }

  /*
  Calculates compass heading from heading in degrees where 0 is north, 90 is east, 180 is south and 270 is west.
 */
  private calculateHeading(headingDegrees: number): string {
    const slice = 360.0 / 16;
    if (headingDegrees < slice) {
      return "north";
    }
    if (headingDegrees < slice * 3) {
      return "north-east";
    }
    if (headingDegrees < slice * 5) {
      return "east";
    }
    if (headingDegrees < slice * 7) {
      return "south-east";
    }
    if (headingDegrees < slice * 9) {
      return "south";
    }
    if (headingDegrees < slice * 11) {
      return "south-west";
    }
    if (headingDegrees < slice * 13) {
      return "west";
    }
    if (headingDegrees < slice * 15) {
      return "north-west";
    }
    return "north";
  }

  private calculateCommand(delta: number): string {
    const absDelta = Math.abs(delta);
    if (absDelta < 11) {
      return "continue";
    }
    if (absDelta < 40) {
      return delta < 0 ? "turn-slight-left" : "turn-slight-right";
    }
    if (absDelta < 103) {
      return delta < 0 ? "turn-left" : "turn-right";
    }
    return delta < 0 ? "turn-sharp-left" : "turn-sharp-right";
  }

  private isDifferentStreet(street1: string, street2: string): boolean {
    if (street1 == null || street2 == null) {
      return false;
    }
    if (street1 === "" || street2 === "") {
      return false;
    }
    return street1 !== street2;
  }

  private isHardTurn(text: string): boolean {
    return text === "turn-left" ||
      text === "turn-right" ||
      text === "turn-sharp-left" ||
      text === "turn-sharp-right";
  }

}
