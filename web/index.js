const Koa = require('koa');
const static = require('koa-static');
const path = require('path');
const {query} = require('./utils/async-db');
const Router = require('koa-router');
const {api} = require('./routes/api');

const app = new Koa();
const staticPath = './static';

// 静态资源中间件
app.use(static(path.join(__dirname, staticPath)));

let router = new Router();
router.use('/api', api.routes(), api.allowedMethods());

// 路由中间件
app.use(router.routes()).use(router.allowedMethods());

app.listen(3000);
console.log('static-use-middleware is starting at port 3000');