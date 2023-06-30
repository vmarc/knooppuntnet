import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SymbolShape } from './symbol-shape';
import { SymbolComponent } from './symbol.component';

@Component({
  selector: 'kpn-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="page">
      <h1>Symbols</h1>
      <h2>Shapes</h2>
      <div class="symbols">
        <div *ngFor="let symbol of symbols" class="symbol">
          <div>{{ symbol }}</div>
          <kpn-symbol [shape]="symbol"></kpn-symbol>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .page {
        padding: 1em;
        width: 100%;
      }

      .symbols {
        display: flex;
        flex-wrap: wrap;
      }

      .symbol {
        padding: 0.5em;
        border: 1px solid lightgray;
        margin: 0 0 1em 1em;
      }
    `,
  ],
  standalone: true,
  imports: [NgFor, SymbolComponent],
})
export class SymbolsComponent {
  readonly symbols = SymbolShape.shapes();
}
