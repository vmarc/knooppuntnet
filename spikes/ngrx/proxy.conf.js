PROXY_CONFIG = {
  "/api/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  },
  "/json-api/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  }
};

module.exports = PROXY_CONFIG;
