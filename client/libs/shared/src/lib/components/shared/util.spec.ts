import { Util } from './util';

describe('util', () => {
  it('toInteger', () => {
    expect(Util.toInteger(null)).toEqual(0);
    expect(Util.toInteger(undefined)).toEqual(0);
    expect(Util.toInteger('0')).toEqual(0);
    expect(Util.toInteger('1')).toEqual(1);
    expect(Util.toInteger('123')).toEqual(123);
    expect(Util.toInteger('1.2')).toEqual(0);
    expect(Util.toInteger('bla')).toEqual(0);
  });
});
