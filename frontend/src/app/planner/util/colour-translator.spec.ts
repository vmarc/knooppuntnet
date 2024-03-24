import { ColourTranslator } from './colour-translator';

describe('ColourTranslator', () => {
  const translations = new Map<string, string>([
    ['white', 'wit'],
    ['red', 'rood'],
    ['or', 'of'],
  ]);

  const expectTranslation = (source: string, expected: string) => {
    expect(new ColourTranslator(translations).translate(source)).toEqual(expected);
  };

  it('translate', () => {
    expectTranslation('white', 'wit');
    expectTranslation('red', 'rood');
    expectTranslation('blue', 'blue');
    expectTranslation('white;red;blue', 'wit of rood of blue');
    expectTranslation('white-red', 'wit-rood');
    expectTranslation('red-blue', 'rood-blue');
    expectTranslation('white-red;red-blue', 'wit-rood of rood-blue');
  });
});
