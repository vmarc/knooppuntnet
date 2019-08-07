import {Component, Input} from "@angular/core";
import {FilterOptionGroup} from "../../../kpn/filter/filter-option-group";
import {MatCheckboxChange} from "@angular/material";

@Component({
  selector: "kpn-filter-checkbox-group",
  template: `
    <div>
      <div class="group-name">{{group.name}}</div>
      <mat-checkbox
          *ngFor="let option of group.options"
          [checked]="isSelected()"
          (change)="selectedChanged($event)">
        {{option.name}}<span class="option-count">{{option.count}}</span>
      </mat-checkbox>
    </div>
  `
})
export class FilterCheckboxGroupComponent {

  @Input() group: FilterOptionGroup;

  isSelected() {
    return false;
  }

  selectedChanged(event: MatCheckboxChange) {
  }
}
