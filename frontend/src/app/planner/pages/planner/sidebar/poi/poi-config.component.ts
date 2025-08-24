import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { Subscriptions } from '@app/util/subscriptions';
import { ChangeDetectionStrategy } from '@angular/core';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'ui-poi-config',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div class="poi-config">
      <div class="col-icon">
        @if (icon) {
          <img width="32" height="37" [src]="icon" alt="icon" />
        }
      </div>

      <div class="col-name">
        {{ poiName() }}
      </div>

      <div>
        <div class="col-spacer"></div>
        <nz-radio-group [ngModel]="levelString()" (ngModelChange)="levelChanged($event)">
          <label nz-radio nzValue="0" title="Do not show this icon on the map" class="col-level-0">
          </label>
          <label
            nz-radio
            nzValue="11"
            [nzDisabled]="minLevel > 11"
            title="Show this icon on the map as of zoomlevel 11 and higher"
            class="col-level-11"
          >
          </label>
          <label
            nz-radio
            nzValue="12"
            [nzDisabled]="minLevel > 12"
            title="Show this icon on the map as of zoomlevel 12 and higher"
            class="col-level-12"
          >
          </label>
          <label
            nz-radio
            nzValue="13"
            [nzDisabled]="minLevel > 13"
            title="Show this icon on the map as of zoomlevel 13 and higher"
            class="col-level-13"
          >
          </label>
          <label
            nz-radio
            nzValue="14"
            [nzDisabled]="minLevel > 14"
            title="Show this icon on the map as of zoomlevel 14 and higher"
            class="col-level-14"
          >
          </label>
          <label
            nz-radio
            nzValue="15"
            [nzDisabled]="minLevel > 15"
            title="Show this icon on the map as of zoomlevel 15 and higher"
            class="col-level-15"
          >
          </label>
        </nz-radio-group>
      </div>
    </div>
  `,
  styles: `
    .poi-config {
      border-bottom: 1px solid lightgray;
      padding-top: 10px;
      padding-bottom: 10px;
    }
  `,
  imports: [MatRadioModule, NzRadioGroupComponent, NzRadioComponent, FormsModule],
})
export class PoiConfigComponent implements OnInit, OnDestroy {
  readonly poiId = input.required<string>();

  private readonly poiService = inject(OldPoiService);

  protected icon: string;
  protected minLevel = 0;
  protected level = 0;
  private readonly subscriptions = new Subscriptions();

  ngOnInit(): void {
    this.subscriptions.add(
      this.poiService.poiConfiguration.subscribe((poiConfiguration) => {
        const definition = poiConfiguration.poiDefinitionWithName(this.poiId());
        if (definition != null) {
          this.icon = 'assets/images/pois/' + definition.icon;
          this.minLevel = definition.minLevel;
        } else {
          console.log('DEBUG PoiConfigComponent definition not found name=' + this.poiId());
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  levelString(): string {
    return this.poiService.poiLevel(this.poiId());
  }

  levelChanged(event: MatRadioChange) {
    this.poiService.updatePoiLevel(this.poiId(), +event.value);
  }

  poiName() {
    return this.poiService.name(this.poiId());
  }
}
