import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-icon-happy",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-icon svgIcon="happy"></mat-icon>
  `,
  styles: [`
    mat-icon {
      width: 18px;
      height: 18px;
    }
  `]
})
export class IconHappyComponent {
}
