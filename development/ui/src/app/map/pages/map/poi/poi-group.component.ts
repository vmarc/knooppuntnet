import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-poi-group',
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        <mat-panel-title>
          {{name}} <span class="kpn-thin">&nbsp;&nbsp;(10/10)/Disabled</span>
        </mat-panel-title>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>

        <div>
          <mat-checkbox>Enable this group</mat-checkbox>
        </div>

        <div>
          <button mat-stroked-button>Show all</button>
          <button mat-stroked-button>Hide all</button>
          <button mat-stroked-button>Default</button>
        </div>

        <div>
          <div class="col-spacer"></div>
          <div class="col-level-0">NO</div>
          <div class="col-level-11">11</div>
          <div class="col-level-12">12</div>
          <div class="col-level-13">13</div>
          <div class="col-level-14">14</div>
          <div class="col-level-15">15</div>
        </div>
        <ng-content></ng-content>
      </ng-template>
    </mat-expansion-panel>
  `
})
export class PoiGroupComponent {
  @Input() name;
}
