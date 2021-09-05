PROXY_CONFIG = {
  '/api/**': {
    target: 'https://knooppuntnet.nl',
    secure: false,
  },
  '/tiles/**': {
    target: 'https://knooppuntnet.nl',
    secure: false,
  },
  '/images/**': {
    target: 'https://knooppuntnet.nl',
    secure: true,
  },
};

module.exports = PROXY_CONFIG;
