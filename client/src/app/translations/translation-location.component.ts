import {Component, Input, OnInit} from "@angular/core";
import {TranslationsService} from "./translations.service";
import {TranslationLocation} from "./domain/translation-location";

@Component({
  selector: "kpn-translation-location",
  template: `
    <div>
      <div class="title">
        {{location.sourceFile}}, line: {{location.lineNumber}}
      </div>
      <div class="body">
        <pre>{{sourceCode}}</pre>
      </div>
    </div>
  `,
  styles: [`

    .title {
      padding-top: 10px;
      font-size: 0.8em;
    }

    .body {
      padding: 3px;
      background-color: #f8f8f8;
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
