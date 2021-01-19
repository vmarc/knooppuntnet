import {Component} from '@angular/core';

@Component({
  selector: 'app-feature-4',
  template: `
    <div class="feature">
      <span>Feature 4</span>
      <app-feature-4-a></app-feature-4-a>
      <app-feature-4-b></app-feature-4-b>
      <app-feature-4-c></app-feature-4-c>
      <app-feature-4-d></app-feature-4-d>
    </div>
  `
})
export class Feature4Component {
}
