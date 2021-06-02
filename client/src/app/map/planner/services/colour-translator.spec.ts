import { Map as TranslationMap } from 'immutable';
import { ColourTranslator } from './colour-translator';

describe('ColourTranslator', () => {
  const translations = TranslationMap({
    white: 'wit',
    red: 'rood',
  });

  const expectTranslation = (source: string, expected: string) => {
    expect(new ColourTranslator(translations).translate(source)).toEqual(
      expected
    );
  };

  it('translate', () => {
    expectTranslation('white', 'wit');
    expectTranslation('red', 'rood');
    expectTranslation('blue', 'blue');
    expectTranslation('white;red;blue', 'wit / rood / blue');
    expectTranslation('white-red', 'wit-rood');
    expectTranslation('red-blue', 'rood-blue');
    expectTranslation('white-red;red-blue', 'wit-rood / rood-blue');
  });
});
