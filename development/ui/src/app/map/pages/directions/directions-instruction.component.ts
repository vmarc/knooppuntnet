import {Component, Input} from "@angular/core";
import {DirectionsInstruction} from "../../../kpn/shared/directions/directions-instruction";

@Component({
  selector: "kpn-directions-instruction",
  template: `
    <div *ngIf="instruction.node" class="node">
      <div class="node-number">{{instruction.node}}</div>        
    </div>
    <div *ngIf="!instruction.node" class="instruction">
      <kpn-directions-sign [sign]="instruction.sign"></kpn-directions-sign>
      <div>
        <div>
          {{instruction.text}}
        </div>

        <div *ngIf="instruction.streetName">
          streetName = {{instruction.streetName}}
        </div>

        <div>
          {{instruction.distance}}m
        </div>

        <div *ngIf="instruction.annotationText">
          annotationText = {{instruction.annotationText}}
        </div>

        <div *ngIf="instruction.annotationImportance">
          annotationImportance = {{instruction.annotationImportance}}
        </div>

        <div *ngIf="instruction.exitNumber">
          exitNumber = {{instruction.exitNumber}}
        </div>

        <div *ngIf="instruction.turnAngle">
          turnAngle = {{instruction.turnAngle}}
        </div>
      </div>
    </div>
  `,
  styles: [`
    .instruction {
      display: inline-flex;
      flex-direction: row;
      padding-top: 10px;
      padding-bottom: 10px;
    }

    kpn-directions-sign {
      padding-right: 20px;
    }

    .node {
      display: inline-block;
      border-color: gray;
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

  `]
})
export class DirectionsInstructionComponent {

  @Input() instruction: DirectionsInstruction;

}
