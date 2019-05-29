import {Component, Input} from "@angular/core";
import {MatCheckboxChange} from "@angular/material";
import {PoiService} from "../../../../services/poi.service";

@Component({
  selector: "kpn-poi-group",
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        <mat-panel-title>
          <mat-checkbox (click)="$event.stopPropagation();" [checked]="isEnabled()" (change)="groupEnabledChanged($event)"></mat-checkbox>
          <span class="title">{{title}}</span>
           <span class="kpn-thin">(10/10)</span>
        </mat-panel-title>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>

        <div>
        </div>

        <div>
          <button mat-stroked-button (click)="showAllClicked()">Show all</button>
          <button mat-stroked-button (click)="hideAllClicked()">Hide all</button>
          <button mat-stroked-button (click)="defaultClicked()">Default</button>
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
  `,
  styles: [`
    .title {
      padding-left: 10px;
      padding-right: 20px;
    }
  `]
})
export class PoiGroupComponent {

  @Input() name;
  @Input() title;

  constructor(private poiService: PoiService) {
  }

  isEnabled(): boolean {
    return this.poiService.isGroupEnabled(this.name);
  }

  groupEnabledChanged(event: MatCheckboxChange) {
    this.poiService.updateGroupEnabled(this.name, event.checked);

  }

  showAllClicked() {
    this.poiService.updateGroupShowAll(this.name);
  }

  hideAllClicked() {
    this.poiService.updateGroupHideAll(this.name);
  }

  defaultClicked() {
    this.poiService.updateGroupDefault(this.name);
  }

}
