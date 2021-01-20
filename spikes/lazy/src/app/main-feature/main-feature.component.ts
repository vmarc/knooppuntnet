import {Component} from '@angular/core';

@Component({
  selector: 'app-main-feature',
  template: `
    <div>
      <div>
        Main Feature (router lazy loaded)
      </div>
      <p>
        <a [routerLink]="" (click)="selection = 'feature1'">Feature 1</a> |
        <a [routerLink]="" (click)="selection = 'feature2'">Feature 2</a> |
        <a [routerLink]="" (click)="selection = 'feature3'">Feature 3</a> |
        <a [routerLink]="" (click)="selection = 'feature4'">Feature 4</a>
      </p>
      <p *ngIf="selection.length === 0">Click feature link</p>
      <app-feature-1-lazy-load *ngIf="selection === 'feature1'"></app-feature-1-lazy-load>
      <app-feature-2-lazy-load *ngIf="selection === 'feature2'"></app-feature-2-lazy-load>
      <app-feature-3-lazy-load *ngIf="selection === 'feature3'"></app-feature-3-lazy-load>
      <app-feature-4-lazy-load *ngIf="selection === 'feature4'"></app-feature-4-lazy-load>
    </div>
  `
})
export class MainFeatureComponent {
  selection = '';
}
