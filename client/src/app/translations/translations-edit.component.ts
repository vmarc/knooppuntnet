import {Component, Input} from "@angular/core";
import {saveAs} from "file-saver";
import {Subscriptions} from "../util/Subscriptions";
import {TranslationFile} from "./domain/translation-file";
import {XliffWriter} from "./domain/xliff-writer";
import {TranslationsService} from "./translations.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: "kpn-translations-edit",
  template: `

    Number of translations: {{translationCount()}},
    to be translated: {{toBeTranslatedCount()}}

    <mat-divider></mat-divider>
    <button mat-raised-button color="primary" (click)="export()">Export</button>
    <mat-divider></mat-divider>

    <kpn-translation-table [translationUnits]="translationFile.translationUnits">
    </kpn-translation-table>
  `,
  styles: [`
    mat-table {
      width: 100%;
    }
  `]
})
export class TranslationsEditComponent {

  private readonly subscriptions = new Subscriptions();

  @Input() translationFile: TranslationFile;

  constructor(private translationsService: TranslationsService) {
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
}
