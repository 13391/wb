/**
 * Created by Jiavan on 2017/5/23.
 */
define(function () {

    function render() {
        $.ajax({
            url: '/api/getUserInfo.json',
            success: function (res) {
                res = JSON.parse(res);
                $('.chart-user .content').html(template('userInfo', res.userInfo));
            }
        });
    }

    return {
        render: render
    };
});