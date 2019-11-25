const proxy = require('http-proxy-middleware');
module.exports = function(app) {
  app.use(
    '/peers',
    proxy({
      target: 'http://localhost:8080',
      changeOrigin: true,
    })
  );
};