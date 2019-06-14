export class TranslationUnit {

  constructor(readonly id: string,
              readonly source: string,
              readonly target: string,
              readonly state: string,
              readonly sourceFile: string,
              readonly lineNumber: string) {
  }

}
