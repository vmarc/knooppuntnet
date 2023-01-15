import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Fact } from '@api/custom/fact';

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
})
export class FactCommaListComponent {
  @Input() title: string;
  @Input() facts: Fact[];
  @Input() icon: string;

  hasFacts(): boolean {
    return this.facts && this.facts.length > 0;
  }
}
