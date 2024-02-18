export class ColourTranslator {
  constructor(private readonly translations: Map<string, string>) {}

  translate(colour: string): string {
    const splitted = colour.split(';');
    const translatedColours = splitted.map((colourKey) => {
      const colourKeyElements = colourKey.split('-');
      const translatedColourElements = colourKeyElements.map((colourKeyElement) =>
        this.translateColour(colourKeyElement)
      );
      return translatedColourElements.join('-');
    });
    const or = this.translations.get('or');
    return translatedColours.join(` ${or} `);
  }

  private translateColour(key: string): string {
    if (this.translations) {
      const translation = this.translations.get(key);
      if (translation) {
        return translation;
      }
      return key;
    }
    return '';
  }
}
