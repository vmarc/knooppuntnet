import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import * as Sentry from "@sentry/angular";
import {Severity} from "@sentry/types/dist/severity";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-base-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>

      <div id="extraFunctions" (click)="toggleExtraFunctions()" class="extra-functions">
      </div>

      <ul *ngIf="extraFunctionsEnabled">
        <li>
          Sentry.io error reporting demo
          <ul>
            <li>
              <p>
                <a id="#throwErrorLink" (click)="throwError()">throw error</a>
              </p>
            </li>
            <li>
              <p>
                <a id="#undefinedFieldLink" (click)="undefinedField()">undefined field</a>
              </p>
            </li>
            <li>
              <p>
                <a id="#undefinedFieldWithinScopeLink" (click)="undefinedFieldWithinScope()">
                  undefined field within scope
                </a>
              </p>
            </li>
          </ul>
        </li>
        <li>
          <p>
            <a routerLink="/poi/areas">point of interest areas</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/status">status</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/demo">video demos</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/pieter">tryout gpx to osm matching</a>
          </p>
        </li>
      </ul>
    </kpn-sidebar>
  `,
  styles: [`
    .extra-functions {
      float: right;
      width: 20px;
      height: 20px;
    }
  `]
})
export class BaseSidebarComponent {

  extraFunctionsEnabled = false;

  field: string;
  fieldLength = 0;

  constructor() {
    Sentry.setContext("test-context", {
      field1: "field-value-1",
      field2: "field-value-2"
    });
  }

  throwError(): void {
    throw new Error("Sentry.io demo error");
  }

  undefinedField(): void {
    this.fieldLength = this.field.length;
  }

  undefinedFieldWithinScope(): void {

    this.addCustomBreadcrumb();

    try {
      this.undefinedField();
    } catch (e) {
      Sentry.withScope(scope => {
        scope.setTag("tag1", "tag-value-1");
        scope.setTag("tag2", "tag-value-2");
        scope.setExtra("extra-1", "extra-value-1");
        scope.setExtra("field-length", "" + this.fieldLength);
        const eventId = Sentry.captureException(e);
        Sentry.showReportDialog({
          eventId: eventId,
          successMessage: "Dankuwel: de informatie is nu verstuurd naar Sentry..."
        });
      });
    }
  }

  toggleExtraFunctions(): void {
    this.extraFunctionsEnabled = !this.extraFunctionsEnabled;
  }

  addCustomBreadcrumb(): void {
    Sentry.addBreadcrumb({
      type: "knooppuntnet",
      level: Severity.Info,
      message: "this is a custom breadcrumb",
      category: "action",
      data: {
        "breadcrumb-data-1": "breadcrumb-data-1-value",
        "breadcrumb-data-2": "breadcrumb-data-2-value",
      }
    });
  }

}
