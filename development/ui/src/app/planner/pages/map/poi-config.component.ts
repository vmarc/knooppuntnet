import {Component, forwardRef, Input} from '@angular/core';
import {NG_VALUE_ACCESSOR} from "@angular/forms";
import {MatRadioChange} from "@angular/material";

export const POI_CONFIG_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => PoiConfigComponent),
  multi: true,
};

@Component({
  selector: 'kpn-poi-config',
  template: `
    <table>

      <tr>
        
        <td class="col-icon"><img [src]="'/assets/images/pois/' + icon"/></td>

        <td class="col-name">{{name}}</td>

        <mat-radio-group [value]="levelString()" (change)="levelChanged($event)">
          <td>
            <mat-radio-button
              value="0"
              title="Do not show this icon on the map"
              class="col-level-0">
            </mat-radio-button>
          </td>
          <td>
            <mat-radio-button
              value="13"
              [disabled]="minLevel > 13"
              title="Show this icon on the map as of zoomlevel 13 and higher"
              class="col-level-13">
            </mat-radio-button>
          </td>
          <td>
            <mat-radio-button
              value="14"
              [disabled]="minLevel > 14"
              title="Show this icon on the map as of zoomlevel 14 and higher"
              class="col-level-14">
            </mat-radio-button>
          </td>
          <td>
            <mat-radio-button
              value="15"
              [disabled]="minLevel > 15"
              title="Show this icon on the map as of zoomlevel 15 and higher"
              class="col-level-15">
            </mat-radio-button>
          </td>
          <td>
            <mat-radio-button
              value="16"
              title="Show this icon on the map as of zoomlevel 16 and higher"
              class="col-level-16">
            </mat-radio-button>
          </td>
        </mat-radio-group>
      </tr>
    </table>
  `,
  providers: [POI_CONFIG_VALUE_ACCESSOR],
  styles: [`
    /deep/ .mat-radio-button.mat-radio-disabled .mat-radio-outer-circle {
      border-color: rgba(0, 0, 0, 0.10);
    }
  `]
})
export class PoiConfigComponent {

  @Input() icon: string;
  @Input() name: string;
  @Input() minLevel: number = 13;

  level: number = 0;

  private onChange: (_: number) => void;

  levelString(): string {
    if (this.level) {
      return this.level.toString();
    }
    return "";
  }

  levelChanged(event: MatRadioChange) {
    this.onChange(+event.value);
  }

  registerOnChange(fn: (_: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: (_: number) => void): void {
  }

  setDisabledState(isDisabled: boolean): void {
  }

  writeValue(newLevel: number): void {
    this.level = newLevel;
  }

}
