import { OnChanges } from '@angular/core';
import {
  ChangeDetectionStrategy,
  Component,
  Input,
  SimpleChanges,
} from '@angular/core';
import { List } from 'immutable';
import { DirectionsAnalyzer } from '../planner/directions/directions-analyzer';
import { Plan } from '../planner/plan/plan';
import { PlanInstruction } from '../planner/plan/plan-instruction';

@Component({
  selector: 'kpn-plan-instructions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let instruction of instructions">
      <kpn-plan-instruction [instruction]="instruction"></kpn-plan-instruction>
      <mat-divider></mat-divider>
    </div>
  `,
  styles: [``],
})
export class PlanInstructionsComponent implements OnChanges {
  @Input() plan: Plan;
  instructions: List<PlanInstruction>;

  ngOnChanges(changes: SimpleChanges): void {
    this.instructions = new DirectionsAnalyzer().analyze(this.plan);
  }
}
