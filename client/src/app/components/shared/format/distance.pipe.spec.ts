import {DistancePipe} from './distance.pipe';

describe('DistancePipe', () => {
  it('transform', () => {
    const pipe = new DistancePipe();
    expect(pipe).toBeTruthy();
    expect(pipe.transform(null)).toEqual('-');
    expect(pipe.transform(100)).toEqual('100m');
    expect(pipe.transform(999)).toEqual('999m');
    expect(pipe.transform(1000)).toEqual('1km');
    expect(pipe.transform(1049)).toEqual('1km');
    expect(pipe.transform(1050)).toEqual('1,1km');
    expect(pipe.transform(1100)).toEqual('1,1km');
    expect(pipe.transform(9949)).toEqual('9,9km');
    expect(pipe.transform(9950)).toEqual('10km');
    expect(pipe.transform(10000)).toEqual('10km');
  });
});
