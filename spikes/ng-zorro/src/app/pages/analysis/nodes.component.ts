import {Component} from '@angular/core';

@Component({
  selector: 'app-nodes',
  template: `
    <app-header></app-header>
    <p>
      Nodes page
    </p>
    <button nz-button nzType="primary">Primary Button</button>
    <button nz-button nzType="default">Default Button</button>
    <button nz-button nzType="dashed">Dashed Button</button>
    <button nz-button nzType="text">Text Button</button>
    <a nz-button nzType="link">Link Button</a>
  `,
  styles: [`
    [nz-button] {
      display: block;
      margin-bottom: 12px;
    }
  `]
})
export class NodesComponent {
}
