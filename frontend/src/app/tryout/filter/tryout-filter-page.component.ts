import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { TryoutFilterAlternative1Component } from './tryout-filter-alternative-1.component';
import { TryoutFilterAlternative2Component } from './tryout-filter-alternative-2.component';

@Component({
  selector: 'kpn-tryout-filter-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <h1>Tryout filter</h1>
      <kpn-tryout-filter-alternative-1 />
      <kpn-tryout-filter-alternative-2 />
    </kpn-old-page>
  `,
  standalone: true,
  imports: [OldPageComponent, TryoutFilterAlternative1Component, TryoutFilterAlternative2Component],
})
export class TryoutFilterPageComponent {}
