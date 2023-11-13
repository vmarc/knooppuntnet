PROXY_CONFIG = {
  '/api/**': {
    target: 'https://experimental.knooppuntnet.nl',
    secure: false,
  },
  '/tiles/**': {
    target: 'https://experimental.knooppuntnet.nl',
    secure: false,
  },
  '/images/**': {
    target: 'https://experimental.knooppuntnet.nl',
    secure: true,
  },
};

module.exports = PROXY_CONFIG;
