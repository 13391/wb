const mysql = require('mysql');

const pool = mysql.createPool({
    host: '127.0.0.1',
    port: 3306,
    user: 'root',
    password: 'jiavan',
    database: 'weibo_portrait'
});

let query = (sql, values) => {
    console.log(sql);
    return new Promise((resolve, reject) => {
        pool.getConnection((error, connection) => {
            if (error) {
                reject(error);
            } else {
                connection.query(sql, values, (error, result) => {
                    if (error) {
                        reject(error);
                    } else {
                        resolve(result);
                    }
                });
                connection.release();
            }
        });
    });
};

module.exports = {query};