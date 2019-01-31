import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-icon',
  template: `
    <img [src]="'/assets/images/icons/' + file" title="{{name}}" alt="" class="image">
  `,
  styles: [`
    .image {
      width: 24px;
      height: 24px;
      vertical-align: middle;
    }
  `]
})
export class IconComponent {
  @Input() name: string;
  @Input() file: string;
}
