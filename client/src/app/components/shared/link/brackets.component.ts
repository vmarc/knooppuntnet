import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-brackets",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="bracket">(</span>
    <ng-content></ng-content>
    <span class="bracket">)</span>
  `,
  styles: [`
    .bracket {
      color: gray;
    }
  `]
})
export class BracketsComponent {
}
