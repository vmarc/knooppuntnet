import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {I18nService} from "../../../i18n/i18n.service";
import {FilterOption} from "../../../kpn/filter/filter-option";
import {FilterOptionGroup} from "../../../kpn/filter/filter-option-group";

@Component({
  selector: "kpn-filter-checkbox-group",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div class="group-name">{{groupName()}}</div>
      <mat-checkbox
        *ngFor="let option of group.options"
        [checked]="isSelected()"
        (change)="selectedChanged($event)">
        {{optionName(option)}}<span class="option-count">{{option.count}}</span>
      </mat-checkbox>
    </div>
  `
})
export class FilterCheckboxGroupComponent {

  @Input() group: FilterOptionGroup;

  constructor(private i18nService: I18nService) {
  }

  isSelected() {
    return false;
  }

  selectedChanged(event: MatCheckboxChange) {
  }

  groupName(): string {
    return this.i18nService.translation(`@@filter.${this.group.name}`);
  }

  optionName(option: FilterOption): string {
    return this.i18nService.translation(`@@filter.${option.name}`);
  }

}
