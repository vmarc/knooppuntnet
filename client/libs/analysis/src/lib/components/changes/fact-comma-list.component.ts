import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Fact } from '@api/custom';
import { FactNameComponent } from '@app/analysis/fact';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-fact-comma-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="hasFacts()" class="kpn-detail kpn-line">
      {{ title }}:&nbsp;
      <div class="kpn-comma-list">
        <span *ngFor="let fact of facts">
          <kpn-fact-name [fact]="fact" />
        </span>
      </div>
      <kpn-icon-happy *ngIf="icon === 'happy'" />
      <kpn-icon-investigate *ngIf="icon === 'investigate'" />
    </div>
  `,
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    FactNameComponent,
    IconHappyComponent,
    IconInvestigateComponent,
  ],
})
export class FactCommaListComponent {
  @Input() title: string;
  @Input() facts: Fact[];
  @Input() icon: string;

  hasFacts(): boolean {
    return this.facts && this.facts.length > 0;
  }
}
