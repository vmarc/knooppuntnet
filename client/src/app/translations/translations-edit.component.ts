import {Component, Input} from "@angular/core";
import {saveAs} from "file-saver";
import {Subscriptions} from "../util/Subscriptions";
import {TranslationFile} from "./domain/translation-file";
import {XliffWriter} from "./domain/xliff-writer";
import {TranslationsService} from "./translations.service";
import {TranslationUnit} from "./domain/translation-unit";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: "kpn-translations-edit",
  template: `

    Number of translations: {{translationCount()}},
    to be translated: {{toBeTranslatedCount()}}

    <mat-divider></mat-divider>
    <button mat-raised-button color="primary" (click)="export()">Export</button>
    <mat-divider></mat-divider>

    <div *ngIf="translationUnit">
      <div>
        {{translationUnit.sourceFile}}
      </div>
      <div>
        line: {{translationUnit.lineNumber}}
      </div>
      <div>
        state: {{translationUnit.state}}
      </div>
      <div>
        id: {{translationUnit.id}}
      </div>

      <form [formGroup]="form">
        <div class="text-areas">
          <textarea readonly formControlName="source"></textarea>
          <textarea formControlName="target" (keyup.control.enter)="onControlEnter()"></textarea>
        </div>
      </form>

      <pre>{{sourceCode}}</pre>
    </div>


    <kpn-translation-table
      [translationUnits]="translationFile.translationUnits"
      (selected)="selectedTranslactionUnitChanged($event)">
    </kpn-translation-table>
  `,
  styles: [`
    mat-table {
      width: 100%;
    }

    .text-areas {
      display: flex;
    }

    .text-areas > textarea {
      margin: 8px;
      padding: 4px;
      flex-grow: 1;
      font-size: 18px;
    }
  `]
})
export class TranslationsEditComponent {

  private readonly subscriptions = new Subscriptions();

  @Input() translationFile: TranslationFile;

  readonly form: FormGroup;
  readonly source = new FormControl();
  readonly target = new FormControl();

  sourceCode: string;

  translationUnit: TranslationUnit;

  constructor(private fb: FormBuilder,
              private translationsService: TranslationsService) {
    this.form = this.buildForm();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  export() {
    console.log("Export");
    const content = new XliffWriter().write(this.translationFile);
    const blob = new Blob([content], {type: "application/xml"});
    let filename = "messages." + this.translationFile.targetLanguage + ".xlf";
    saveAs(blob, filename);
  }

  translationCount(): number {
    return this.translationFile.translationUnits.size;
  }

  toBeTranslatedCount(): number {
    return this.translationFile.translationUnits.filter(tu => tu.state === "new").size;
  }

  selectedTranslactionUnitChanged(translationUnit: TranslationUnit): void {
    this.translationUnit = translationUnit;
    if (translationUnit !== null) {
      this.translationsService.loadSource(translationUnit).subscribe(sourceCode => this.sourceCode = sourceCode);
      this.source.setValue(translationUnit.source);
      this.target.setValue(translationUnit.target);
    } else {
      this.sourceCode = "";
      this.source.reset();
      this.target.reset();
    }
  }

  onControlEnter() {

  }

  private buildForm(): FormGroup {
    return this.fb.group(
      {
        source: this.source,
        target: this.target
      });
  }

}
