const Router = require('koa-router');
const { query } = require('../utils/async-db');
const request = require('superagent');

let api = new Router();
api.get('/getUserGender.json', async (ctx) => {
    let sql = 'select gender, count(gender) as count from user group by gender order by count desc';
    let groups = await query(sql);
    sql = 'select count(*) as count from user';
    let total = await query(sql);
    total = total[0].count;

    groups.forEach((item) => {
        item.rate = item.count / total;
    });

    ctx.body = {
        total,
        groups
    };
});

api.get('/getUserAddress.json', async (ctx) => {
    let sql = 'select province, count(province) as count from user_deal group by province order by count';
    ctx.body = await query(sql);
});

api.get('/getUserCompany.json', async (ctx) => {
    let sql = 'select company, count(company) as count from user group by company order by count desc limit 10';
    let groups = await query(sql);
    sql = 'select count(distinct(company)) as count from user';
    let total = await query(sql);
    total = total[0].count;

    ctx.body = {total, groups};
});

api.get('/getUserSchool.json', async (ctx) => {
    let sql = 'select school, count(school) as count from user group by school order by count desc limit 10';
    let groups = await query(sql);
    sql = 'select count(school) as count from user';
    let total = await query(sql);
    total = total[0].count;
    sql = 'select count(distinct (school)) as count from user';
    let schoolCount = await query(sql);
    schoolCount = schoolCount[0].count;

    groups.forEach((item) => {
        item.rate = item.count / total;
    });

    ctx.body = {
        total,
        schoolCount,
        groups
    };
});

api.get('/getUserEducation.json', async (ctx) => {
    let sql = 'select education, count(education) as count from user_deal group by education';
    let groups = await query(sql);
    sql = 'select count(education) as count from user_deal';
    let total = await query(sql);
    total = total[0].count;

    groups.forEach((item) => {
        item.rate = item.count / total;
    });

    ctx.body = {total, groups};
});

api.get('/getUserCredit.json', async (ctx) => {
    let sql = 'select credit, count(credit) as count from user group by credit';
    let groups = await query(sql);
    sql = 'select count(credit) as count from user';
    let total = await query(sql);
    total = total[0].count;

    groups.forEach((item) => {
        item.rate = item.count / total;
    });

    ctx.body = {
        total,
        groups
    };
});

api.get('/getUserTag.json', async (ctx) => {
    let sql = 'select tags, count(tags) as count from user group by tags order by count desc limit 10';
    let groups = await query(sql);
    
    sql = 'select count(tags) as total from user';
    let total = await query(sql);
    total = total[0].total;
    sql = 'select count(distinct(tags)) as count from user';
    let tags = await query(sql);
    tags = tags[0].count;

    groups.forEach((item) => {
        item.rate = item.count / total;
    });

    ctx.body = {
        total,
        tags,
        groups
    };
});

api.get('/getUserInfo.json', async (ctx) => {
    let res = await request.get('https://m.weibo.cn/api/container/getIndex?type=uid&value=3855494782&containerid=1005053855494782');
    ctx.body = res.body;
});


module.exports = {api};