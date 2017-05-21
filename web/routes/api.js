const Router = require('koa-router');
const { query } = require('../utils/async-db');

let api = new Router();
api.get('/getUserGender.json', async (ctx) => {
    let sql = 'select gender, count(gender) as count from user group by gender order by count desc';
    ctx.body = await query(sql);
});

api.get('/getUserAddress.json', async (ctx) => {
    let sql = 'select province, count(province) as count from user_deal group by province order by count';
    ctx.body = await query(sql);
});

api.get('/getUserCompany.json', async (ctx) => {
    let sql = 'select company, count(company) as count from user group by company order by count desc limit 10';
    ctx.body = await query(sql);
});

api.get('/getUserSchool.json', async (ctx) => {
    let sql = 'select school, count(school) as count from user group by school order by count desc limit 10';
    ctx.body = await query(sql);
});

api.get('/getUserEducation.json', async (ctx) => {
    let sql = 'select education, count(education) as count from user_deal group by education';
    ctx.body = await query(sql);
});

api.get('/getUserCredit.json', async (ctx) => {
    let sql = 'select credit, count(credit) as count from user group by credit';
    ctx.body = await query(sql);
});


module.exports = {api};