import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-plan-instruction-command",
  template: `
    <mat-icon svgIcon={{command}} class="command-icon"></mat-icon>
  `,
  styles: [`

    mat-icon {
      width: 40px;
      height: 40px;
    }

    /deep/ .command-icon > svg {
      fill: #666666;
      stroke: #666666;
    }

  `]
})
export class PlanInstructionCommandComponent {
  @Input() command: string;
}
