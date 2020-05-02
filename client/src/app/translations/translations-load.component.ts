import {ChangeDetectionStrategy} from "@angular/core";
import {Component, EventEmitter, Output} from "@angular/core";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {TranslationsService} from "./translations.service";
import {XliffParser} from "./domain/xliff-parser";
import {Router} from "@angular/router";
import {TranslationFile} from "./domain/translation-file";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-translations-load",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div class="section-title">
      Load translations from github:
    </div>
    <div class="section-body">
      <form [formGroup]="form">
        <mat-radio-group formControlName="language">
          <mat-radio-button disabled="true">English</mat-radio-button>
          <mat-radio-button [checked]="language.value === 'nl'" value="nl">Dutch</mat-radio-button>
          <mat-radio-button [checked]="language.value === 'de'" value="de">German</mat-radio-button>
          <mat-radio-button [checked]="language.value === 'fr'" value="fr">French</mat-radio-button>
        </mat-radio-group>
        <button mat-stroked-button (click)="loadMessages()">Load from github</button>
      </form>
    </div>

    <div class="section-title">
      Load translations from local file:
    </div>

    <div class="section-body">
      <input id="selectedFile" type="file" (change)="fileChangeListener($event)">
      <button mat-stroked-button (click)="loadLocalFile()" class="action">Load from file</button>
    </div>
  `,
  styles: [`

    .section-title {
      margin-bottom: 5px;
    }

    .section-body {
      margin-left: 30px;
      margin-bottom: 30px;
    }

    mat-radio-button {
      display: block;
      padding: 5px;
    }

    #selectedFile {
      display: none;
    }

    button {
      margin-top: 10px;
    }

  `]
})
export class TranslationsLoadComponent {

  @Output() translationFile = new EventEmitter<TranslationFile>();

  readonly form: FormGroup;
  readonly language = new FormControl("nl");

  constructor(private fb: FormBuilder,
              private router: Router,
              private translationsService: TranslationsService) {
    this.form = this.buildForm();
  }

  loadMessages(): void {
    this.translationsService.loadTranslationFile(this.language.value).subscribe(translationFile => {
      console.log(`${translationFile.translationUnits.size} translations loaded (language=${this.language.value})`);
      this.translationFile.emit(translationFile);
    });
  }

  loadLocalFile(): void {
    document.getElementById("selectedFile").click();
  }

  fileChangeListener(event: Event): void {
    this.readThis(event.target);
  }

  private readThis(inputValue: any): void {
    const file: File = inputValue.files[0];
    const fileReader: FileReader = new FileReader();
    fileReader.onloadend = (e) => {
      const xmlString: string = fileReader.result.toString();
      const translationFile = new XliffParser().parse(xmlString);
      console.log(`${translationFile.translationUnits.size} translations loaded from local file`);
      this.translationFile.emit(translationFile);
    };
    fileReader.readAsText(file);
  }

  private buildForm(): FormGroup {
    return this.fb.group(
      {
        language: this.language
      });
  }

}
