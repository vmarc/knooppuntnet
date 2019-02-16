import {Component, forwardRef, Input, OnInit} from '@angular/core';
import {NG_VALUE_ACCESSOR} from "@angular/forms";
import {MatRadioChange} from "@angular/material";
import {MapService} from "../../../../components/ol/map.service";

export const POI_CONFIG_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => PoiConfigComponent),
  multi: true,
};

@Component({
  selector: 'kpn-poi-config',
  template: `
    <div class="poi-config">

      <div class="col-icon"><img *ngIf="icon" width="32" height="37" [src]="'/assets/images/pois/' + icon" alt="icon"/></div>

      <div class="col-name">
        <kpn-poi-name [name]="formControlName"></kpn-poi-name>
      </div>

      <div>
        <div class="col-spacer"></div>
        <mat-radio-group [value]="levelString()" (change)="levelChanged($event)">
          <mat-radio-button
            value="0"
            title="Do not show this icon on the map"
            class="col-level-0">
          </mat-radio-button>
          <mat-radio-button
            value="13"
            [disabled]="minLevel > 13"
            title="Show this icon on the map as of zoomlevel 13 and higher"
            class="col-level-13">
          </mat-radio-button>
          <mat-radio-button
            value="14"
            [disabled]="minLevel > 14"
            title="Show this icon on the map as of zoomlevel 14 and higher"
            class="col-level-14">
          </mat-radio-button>
          <mat-radio-button
            value="15"
            [disabled]="minLevel > 15"
            title="Show this icon on the map as of zoomlevel 15 and higher"
            class="col-level-15">
          </mat-radio-button>
          <mat-radio-button
            value="16"
            title="Show this icon on the map as of zoomlevel 16 and higher"
            class="col-level-16">
          </mat-radio-button>
        </mat-radio-group>
      </div>
    </div>
  `,
  providers: [POI_CONFIG_VALUE_ACCESSOR],
  styles: [`

    /deep/ .mat-radio-button.mat-radio-disabled .mat-radio-outer-circle {
      border-color: rgba(0, 0, 0, 0.10);
    }

    .poi-config {
      border-bottom: 1px solid lightgray;
      padding-top: 10px;
      padding-bottom: 10px;
    }

  `]
})
export class PoiConfigComponent implements OnInit {

  @Input() formControlName: string;
  @Input() name: string;

  icon: string;
  minLevel: number = 13;
  level: number = 0;

  constructor(private mapService: MapService) {
  }

  ngOnInit(): void {
    this.mapService.poiConfiguration.subscribe(poiConfiguration => {
      const definition = poiConfiguration.definitionWithName(this.formControlName);
      if (definition != null) {
        this.icon = definition.icon;
        this.minLevel = definition.minLevel;
      } else {
        console.log("DEBUG PoiConfigComponent definition not found name=" + this.formControlName);
      }
    });
  }

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
