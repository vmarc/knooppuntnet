export class TranslationUnit {

  constructor(public id: string,
              public source: string,
              public target: string,
              public state: string,
              public sourceFile: string,
              public lineNumber: string) {
  }

}
