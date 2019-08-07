import {Component, Input} from "@angular/core";
import {FilterOptionGroup} from "../../../kpn/filter/filter-option-group";
import {MatRadioChange} from "@angular/material";

@Component({
  selector: "kpn-filter-radio-group",
  template: `
    <div>
      <div class="group-name">{{group.name}}</div>
      <mat-radio-group [value]="selection()" (change)="selectionChanged($event)">
        <mat-radio-button
            *ngFor="let option of group.options"
            value="{{option.name}}">
          <span class="option-name">{{option.name}}</span>
          <span class="option-count">{{option.count}}</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>
  `,
  styleUrls: ["./filter.scss"]
})
export class FilterRadioGroupComponent {

  @Input() group: FilterOptionGroup;

  selection() {
    const selectedOption = this.group.options.find(option => option.selected);
    return selectedOption == null ? null : selectedOption.name;
  }

  selectionChanged(event: MatRadioChange) {
    const option = this.group.options.find(option => option.name == event.value);
    if (option) {
      option.updateState();
    }
  }

}
