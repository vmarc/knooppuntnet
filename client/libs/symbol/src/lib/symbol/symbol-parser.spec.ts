import { SymbolParser } from './symbol-parser';

describe('parser', () => {
  const parse = (value: string) => {
    return new SymbolParser().parse(value);
  };

  it(`parse wiki example 'green:white:green_bar'`, () => {
    expect(parse('green:white:green_bar')).toEqual({
      waycolor: 'green',
      background: {
        color: 'white',
      },
      foreground: {
        color: 'green',
        shape: 'bar',
      },
    });
  });

  it(`parse wiki example 'red:white:red_bar:FW:gray'`, () => {
    expect(parse('red:white:red_bar:FW:gray')).toEqual({
      waycolor: 'red',
      background: {
        color: 'white',
      },
      foreground: {
        color: 'red',
        shape: 'bar',
      },
      text: 'FW',
      textcolor: 'gray',
    });
  });

  it(`parse wiki example 'black:black:X29:white'`, () => {
    expect(parse('black:black:X29:white')).toEqual({
      waycolor: 'black',
      background: {
        color: 'black',
      },
      text: 'X29',
      textcolor: 'white',
    });
  });

  it(`parse wiki example 'red:white:green_circle:1:black'`, () => {
    expect(parse('red:white:green_circle:1:black')).toEqual({
      waycolor: 'red',
      background: {
        color: 'white',
      },
      foreground: {
        color: 'green',
        shape: 'circle',
      },
      text: '1',
      textcolor: 'black',
    });
  });

  it(`parse wiki example 'red:red_dot'`, () => {
    expect(parse('red:red_dot')).toEqual({
      waycolor: 'red',
      foreground: {
        color: 'red',
        shape: 'dot',
      },
    });
  });

  it(`parse wiki example 'red::red_dot'`, () => {
    expect(parse('red::red_dot')).toEqual({
      waycolor: 'red',
      foreground: {
        color: 'red',
        shape: 'dot',
      },
    });
  });

  it(`parse wiki example 'blue:shell_modern'`, () => {
    expect(parse('blue:shell_modern')).toEqual({
      waycolor: 'blue',
      foreground: {
        color: 'yellow',
        shape: 'shell_modern',
      },
    });
  });

  it(`parse wiki example 'blue:blue:shell_modern'`, () => {
    expect(parse('blue:blue:shell_modern')).toEqual({
      waycolor: 'blue',
      background: {
        color: 'blue',
      },
      foreground: {
        color: 'yellow',
        shape: 'shell_modern',
      },
    });
  });

  it(`parse wiki example 'green:white:yellow_bar:green_stripe'`, () => {
    expect(parse('green:white:yellow_bar:green_stripe')).toEqual({
      waycolor: 'green',
      background: {
        color: 'white',
      },
      foreground: {
        color: 'yellow',
        shape: 'bar',
      },
      foreground2: {
        color: 'green',
        shape: 'stripe',
      },
    });
  });

  it(`parse wiki example 'blue:yellow:white_diamond:blue_diamond_right'`, () => {
    expect(parse('blue:yellow:white_diamond:blue_diamond_right')).toEqual({
      waycolor: 'blue',
      background: {
        color: 'yellow',
      },
      foreground: {
        color: 'white',
        shape: 'diamond',
      },
      foreground2: {
        color: 'blue',
        shape: 'diamond_right',
      },
    });
  });

  // it(`parse 'xxx'`, () => {
  //   expect(parse('xxx')).toEqual({
  //     waycolor: '',
  //     background: {
  //       color: '',
  //       shape: '',
  //     },
  //     foreground: {
  //       color: '',
  //       shape: '',
  //     },
  //     foreground2: {
  //       color: '',
  //       shape: '',
  //     },
  //     text: '',
  //     textcolor: '',
  //   });
  // });
});
