PROXY_CONFIG = {
  '/api/**': {
    target: 'http://localhost:9006',
    secure: false,
  },
  '/admin-api/**': {
    target: 'http://localhost:9006',
    secure: false,
  },
  '/tiles/**': {
    target: 'https://experimental.knooppuntnet.nl',
    changeOrigin: true,
    secure: false,
  },
  '/tiles-history/**': {
    target: 'https://experimental.knooppuntnet.nl',
    changeOrigin: true,
    secure: false,
  },
  '/images/**': {
    target: 'https://experimental.knooppuntnet.nl',
    changeOrigin: true,
    secure: false,
  },
  '/videos/**': {
    target: 'https://experimental.knooppuntnet.nl',
    changeOrigin: true,
    secure: false,
  },
};

module.exports = PROXY_CONFIG;
