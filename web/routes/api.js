const Router = require('koa-router');
const { query } = require('../utils/async-db');
const request = require('superagent');
const util = require('util');

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

api.get('/getUserBySearch.json', async (ctx) => {
    let type = ctx.query.type;
    let username = ctx.query.username;
    let body = {};
    
    if (type === 'userinfo' && username) {
        let encodeUsername = encodeURIComponent(username);
        let url = `https://m.weibo.cn/api/container/getIndex?type=user&containerid=100103type%3D3%26q%3D${encodeUsername}`;
        let res = await request.get(url);

        if (res.body.cards.length) {
            let uid = res.body.cards[1].card_group[0].user.id;
            res = await request.get(`https://m.weibo.cn/api/container/getIndex?type=uid&value=${uid}&containerid=100505${uid}`);
            body = res.body
        }
    } else if (type === 'portrait' && username) {
        let sql = `select created_at, source from tweets where uid=(select uid from user where username='${username}')`;
        let ret = await query(sql);

        let createdData = [];
        let sourceData = [];
        let total = ret.length;

        for (let i = 0; i < 24; i++) {
            createdData[i] = 0;
        }
        ret.forEach((item) => {
            let date = new Date(item.created_at);
            let hour = date.getHours();
            createdData[hour]++; 
        });

        sql = `select source, count(source) as count from tweets where uid=(select uid from user where username='${username}') group by source order by count desc limit 10`;
        sourceData = await query(sql);
        body = {total, createdData, sourceData};
    }
    
    ctx.body = body;
});


module.exports = {api};