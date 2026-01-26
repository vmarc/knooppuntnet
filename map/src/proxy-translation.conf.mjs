export default [
  {
    context: ["/api/**", "/admin-api/**"],
    target: "https://knooppuntnet.nl",
    secure: false,
  },
  {
    context: ["/websocket/*"],
    target: "ws://knooppuntnet.nl",
    secure: false,
    ws: true,
  },
  {
    context: ["/tiles/**", "/images/**", "/assets/**"],
    target: "https://experimental.knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
];
