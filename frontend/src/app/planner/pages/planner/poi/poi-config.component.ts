import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { PoiService } from '@app/services';
import { Subscriptions } from '@app/util';

@Component({
  selector: 'kpn-poi-config',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="poi-config">
      <div class="col-icon">
        @if (icon) {
          <img width="32" height="37" [src]="'assets/images/pois/' + icon" alt="icon" />
        }
      </div>

      <div class="col-name">
        {{ poiName() }}
      </div>

      <div>
        <div class="col-spacer"></div>
        <mat-radio-group [value]="levelString()" (change)="levelChanged($event)">
          <mat-radio-button
            value="0"
            title="Do not show this icon on the map"
            class="col-level-0"
          />
          <mat-radio-button
            value="11"
            [disabled]="minLevel > 11"
            title="Show this icon on the map as of zoomlevel 11 and higher"
            class="col-level-11"
          />
          <mat-radio-button
            value="12"
            [disabled]="minLevel > 12"
            title="Show this icon on the map as of zoomlevel 12 and higher"
            class="col-level-12"
          />
          <mat-radio-button
            value="13"
            [disabled]="minLevel > 13"
            title="Show this icon on the map as of zoomlevel 13 and higher"
            class="col-level-13"
          />
          <mat-radio-button
            value="14"
            [disabled]="minLevel > 14"
            title="Show this icon on the map as of zoomlevel 14 and higher"
            class="col-level-14"
          />
          <mat-radio-button
            value="15"
            [disabled]="minLevel > 15"
            title="Show this icon on the map as of zoomlevel 15 and higher"
            class="col-level-15"
          />
        </mat-radio-group>
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
  standalone: true,
  imports: [MatRadioModule],
})
export class PoiConfigComponent implements OnInit, OnDestroy {
  poiId = input.required<string>();
  name = input.required<string>();

  private readonly poiService = inject(PoiService);

  protected icon: string;
  protected minLevel = 0;
  protected level = 0;
  private readonly subscriptions = new Subscriptions();

  ngOnInit(): void {
    this.subscriptions.add(
      this.poiService.poiConfiguration.subscribe((poiConfiguration) => {
        const definition = poiConfiguration.poiDefinitionWithName(this.poiId());
        if (definition != null) {
          this.icon = definition.icon;
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
