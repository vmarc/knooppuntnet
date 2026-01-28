import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MapMode } from '@app/mapold/domain/map-mode';
import { State } from '@app/state/state';
import { NzSegmentedComponent } from 'ng-zorro-antd/segmented';

@Component({
  selector: 'ui-map-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-segmented
      [nzOptions]="options"
      [ngModel]="selectedOption()"
      (nzValueChange)="handleValueChange($event)"
    />
  `,
  imports: [NzSegmentedComponent, FormsModule],
})
export class MapModeComponent {
  private readonly state = inject(State);
  protected readonly options = [
    { label: $localize`:@@map-mode.standard:Standard`, value: 'standard' },
    { label: $localize`:@@map-mode.surface:Surface`, value: 'surface' },
    { label: $localize`:@@map-mode.survey:Survey`, value: 'survey' },
    { label: $localize`:@@map-mode.analysis:Analysis`, value: 'analysis' },
    { label: $localize`:@@map-mode:Segments`, value: 'route-segments' },
    { label: $localize`:@@map-mode:Paths`, value: 'route-paths' },
  ];
  protected readonly selectedOption = computed(() => this.state.map.mode());

  handleValueChange(mode: string | number): void {
    this.state.map.updateMode(mode as MapMode);
  }
}
