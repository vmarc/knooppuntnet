import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { PlanInstruction } from '../../../domain/plan/plan-instruction';
import { PlannerTranslations } from '../../../util/planner-translations';
import { PlanInstructionCommandComponent } from './plan-instruction-command.component';

@Component({
  selector: 'kpn-plan-instruction',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="instruction.node" class="node">
      <div *ngIf="instruction.node.length <= 3" class="node-number">
        {{ instruction.node }}
      </div>
      <div *ngIf="instruction.node.length > 3" class="node-number-long">
        {{ instruction.node }}
      </div>
    </div>
    <div *ngIf="instruction.colour" class="colour">
      {{ translate('follow-colour') }}
      {{ colour(instruction.colour) }}
    </div>
    <div *ngIf="!instruction.node && !instruction.colour" class="instruction">
      <kpn-plan-instruction-command
        [command]="instruction.command"
      ></kpn-plan-instruction-command>
      <div>
        <div *ngIf="instruction.heading">
          <span class="kpn-label"
            >{{ translate('head') }}
            {{ translate('heading-' + instruction.heading) }}</span
          >
          <span *ngIf="instruction.street">
            {{ instruction.street }}
          </span>
        </div>
        <div *ngIf="!instruction.heading">
          <span *ngIf="instruction.street" class="kpn-label">{{
            translate('command-' + instruction.command)
          }}</span>
          <span>
            {{ instruction.street }}
          </span>
          <span *ngIf="!instruction.street">
            {{ translate('command-' + instruction.command) }}
          </span>
        </div>
        <div class="kpn-meters">
          {{ instruction.distance }}
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .instruction {
        display: inline-flex;
        flex-direction: row;
        padding-top: 10px;
        padding-bottom: 10px;
      }

      kpn-plan-instruction-command {
        padding-right: 20px;
      }

      .node {
        display: inline-block;
        border-color: grey;
        border-radius: 50%;
        border-style: solid;
        border-width: 3px;
        width: 40px;
        height: 40px;
        margin-top: 10px;
        margin-bottom: 10px;
      }

      .node-number {
        width: 40px;
        height: 40px;
        font-size: 20px;
        font-weight: 800;
        line-height: 40px;
        text-align: center;
        vertical-align: middle;
        color: #666666;
      }

      .node-number-long {
        width: 250px;
        margin-left: 55px;
        font-size: 16px;
        font-weight: 800;
        line-height: 40px;
        vertical-align: middle;
        color: #666666;
      }

      .colour {
        padding-top: 10px;
        padding-bottom: 10px;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, PlanInstructionCommandComponent],
})
export class PlanInstructionComponent {
  @Input() instruction: PlanInstruction;

  translate(key: string): string {
    return PlannerTranslations.translate(key);
  }

  colour(colour: string): string {
    return PlannerTranslations.colour(colour);
  }
}
