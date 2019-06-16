import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from "@angular/core";
import {TranslationUnit} from "./domain/translation-unit";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {TranslationLocation} from "./domain/translation-location";
import {List} from "immutable";

@Component({
  selector: "kpn-translation-unit",
  template: `

    <div *ngIf="translationUnit">
      id: {{translationUnit.id}},
      state: {{translationUnit.state}}
    </div>
    <div *ngIf="!translationUnit">
      Select translation
    </div>

    <form [formGroup]="form">
      <div class="text-areas">
        <div>
          <textarea readonly formControlName="source" rows="5"></textarea>
        </div>
        <div>
          <textarea formControlName="target" (keyup.control.enter)="onSave()" rows="5"></textarea>
          <button mat-stroked-button (click)="onCancel()" [disabled]="form.pristine">Cancel</button>
          <button mat-stroked-button (click)="onSave()" [disabled]="form.pristine">Save &amp; Next</button>
          <span class="hint">control+enter to save and go to next translation</span>
        </div>
      </div>
    </form>

    <kpn-translation-locations [locations]="locations()"></kpn-translation-locations>
  `,
  styles: [`

    mat-table {
      width: 100%;
    }

    .text-areas {
      margin-top: 10px;
      display: flex;
    }

    .text-areas > div {
      flex: 1;
    }

    .text-areas :first-child {
      margin-right: 20px;
    }
    
    .text-areas > div > textarea {
      width: 100%;
      font-size: 18px;
    }

    .text-areas > div > button {
      margin-right: 10px;
    }

    .hint {
      color: lightgrey;
      font-style: italic;
    }

  `]
})
export class TranslationUnitComponent implements OnChanges {

  @Input() translationUnit: TranslationUnit;
  @Output() edited = new EventEmitter<TranslationUnit>();

  readonly form: FormGroup;
  readonly source = new FormControl();
  readonly target = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["translationUnit"]) {
      if (this.translationUnit) {
        this.form.reset({
          source: this.translationUnit.source,
          target: this.translationUnit.target
        });
      } else {
        this.form.reset();
      }
    }
  }

  onCancel(): void {
    this.target.setValue(this.translationUnit.target);
  }

  onSave(): void {
    if (this.translationUnit) {
      const newTranslationUnit = new TranslationUnit(
        this.translationUnit.id,
        this.translationUnit.source,
        this.target.value,
        null,
        this.translationUnit.locations,
        true
      );
      this.edited.emit(newTranslationUnit);
    }
  }

  locations(): List<TranslationLocation> {
    if (this.translationUnit) {
      return this.translationUnit.locations;
    }
    return List();
  }

  private buildForm(): FormGroup {
    return this.fb.group(
      {
        source: this.source,
        target: this.target
      });
  }

}
