PROXY_CONFIG = {
  "/tiles/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "changeOrigin": true,
    "secure": false
  }
};

module.exports = PROXY_CONFIG;
