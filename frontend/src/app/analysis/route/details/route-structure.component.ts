import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'kpn-route-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
        @for (structureString of structureStrings(); track structureString) {
          <tr>
            <td>
              <span [innerHTML]="formatted(structureString)"></span>
            </td>
          </tr>
        }
      </tbody>
    </table>
  `,
  standalone: true,
  imports: [],
})
export class RouteStructureComponent {
  structureStrings = input<string[] | undefined>();

  private readonly sanitizer = inject(DomSanitizer);

  formatted(structureString: string): SafeHtml {
    let html = structureString;
    html = html.replace(/forward/g, '<b>forward</b>');
    html = html.replace(/backward/g, '<b>backward</b>');
    html = html.replace(/unused/g, '<b>unused</b>');
    html = html.replace(/tentacle/g, '<b>tentacle</b>');
    html = html.replace(/broken/g, "<span style='color:red'>broken</span>");
    html = html.replace(/\\+/g, ' + ');
    html = html.replace(/\\-/g, ' - ');
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
