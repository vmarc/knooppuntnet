import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {FactLevel} from "./fact-level";

@Component({
  selector: "kpn-fact-level",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="isInfo()" class="info circle"></div>
    <div *ngIf="isError()" class="error circle"></div>
    <div *ngIf="isOther()" class="other circle"></div>
  `,
  styles: [`

    .circle {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
    }

    .info {
      background: rgb(102, 187, 106); /* material green400 */
    }

    .error {
      background: rgb(239, 83, 80); /* material red400 */
    }

    .other {
      background: rgb(255, 167, 38); /* material orange400 */
    }

  `]
})
export class FactLevelComponent {

  @Input() factLevel: FactLevel;

  isInfo(): boolean {
    return this.factLevel === FactLevel.info;
  }

  isError(): boolean {
    return this.factLevel === FactLevel.error;
  }

  isOther(): boolean {
    return this.factLevel === FactLevel.other;
  }

}
