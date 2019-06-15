import {Component, Input, OnInit} from "@angular/core";
import {TranslationsService} from "./translations.service";
import {TranslationLocation} from "./domain/translation-location";

@Component({
  selector: "kpn-translation-location",
  template: `
    <div>
      {{location.sourceFile}}, line: {{location.lineNumber}}
      <pre>{{sourceCode}}</pre>
    </div>
  `,
  styles: [`

    pre {
      background-color: lightgrey;
    }

  `]
})
export class TranslationLocationComponent implements OnInit {

  @Input() location: TranslationLocation;

  sourceCode: string;

  constructor(private translationsService: TranslationsService) {
  }

  ngOnInit(): void {
    this.translationsService.loadSource(this.location).subscribe(sourceCode => {
      this.sourceCode = sourceCode;
    });
  }

}
