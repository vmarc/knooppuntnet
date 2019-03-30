import {Component, OnDestroy, OnInit} from '@angular/core';
import {PlannerService} from "../planner.service";
import {Plan} from "../pages/tryout2/plan";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-plan',
  template: `

    <div class="undo-redo-buttons">
      <button mat-raised-button (click)="undo()" [disabled]="!undoEnabled()">Undo</button>
      <button mat-raised-button (click)="redo()" [disabled]="!redoEnabled()">Redo</button>
    </div>

    <div *ngIf="plan.source !== null" class="node user-selected">
      <div class="text">
        {{plan.source.nodeName}}
      </div>
    </div>

    <div *ngFor="let leg of plan.legs">
      <div *ngIf="leg.fragments.isEmpty()">
        <div class="leg">
          Calculating...
        </div>
        <div class="node">
          <div class="text">
            {{leg.sink.nodeName}}
          </div>
        </div>
      </div>
      <div *ngFor="let legFragment of leg.fragments; let i = index">
        <div class="leg">
          {{legFragment.meters}} m
        </div>
        <div class="node" [class.server-selected]="i < leg.fragments.size - 1">
          <div class="text">
            {{legFragment.sink.nodeName}}
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`

    .undo-redo-buttons {
      padding-top: 15px;
      padding-bottom: 15px;
    }

    .undo-redo-buttons :not(:last-child) {
      margin-right: 10px;
    }

    .leg {
      padding-top: 5px;
      padding-bottom: 5px;
      padding-left: 35px;
    }

    .node {
      display: inline-block;
      border-color: gray;
      border-radius: 50%;
      border-style: solid;
      border-width: 3px;
      width: 30px;
      height: 30px;
    }

    .server-selected {
      border-width: 1px;
      padding-left: 2px;
    }

    .text {
      width: 30px;
      margin-top: 5px;
      text-align: center;
    }

  `]
})
export class PlanComponent implements OnInit, OnDestroy {

  plan: Plan;
  planSubscription: Subscription;

  constructor(private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.planSubscription = this.plannerService.context.planObserver.subscribe(plan => this.plan = plan);
  }

  ngOnDestroy(): void {
    if (this.planSubscription) {
      this.planSubscription.unsubscribe();
    }
  }

  undo() {
    this.plannerService.engine.undo();
  }

  redo() {
    this.plannerService.engine.redo();
  }

  undoEnabled(): boolean {
    return this.plannerService.context.commandStack.currentCanUndo;
  }

  redoEnabled(): boolean {
    return this.plannerService.context.commandStack.currentCanRedo;
  }

}
