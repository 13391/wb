const Koa = require('koa');
const static = require('koa-static');
const path = require('path');
const { query } = require('./utils/async-db');

const app = new Koa();
const staticPath = './static';

app.use(static(path.join(__dirname, staticPath)));

app.use(async (ctx) => {
    let sql = 'select * from tweets_deal limit 10';
    let data = await query(sql);
    ctx.body = data;
});

app.listen(3000);
console.log('[demo] static-use-middleware is starting at port 3000');