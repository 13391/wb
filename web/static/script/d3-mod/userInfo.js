/**
 * Created by Jiavan on 2017/5/23.
 */
define(function () {

    function render() {
        $.ajax({
            url: '/api/getUserInfo.json',
            success: function (res) {
                res = JSON.parse(res);
                $('body').css('background-image', 'url("' + res.userInfo.cover_image_phone + '")');
                $('.chart-user .content').html(template('userInfo', res.userInfo));
            }
        });
    }

    var document = window.document;
    $(document).on('click', '.search-info', function () {
        var input = $('.chart-user input').val();
        $.ajax({
            url: '/api/getUserBySearch.json',
            data: {
                username: input,
                type: 'userinfo'
            },
            success: function (res) {
                if (Object.keys(res).length) {
                    res = JSON.parse(res);
                    $('body').css('background-image', 'url("' + res.userInfo.cover_image_phone + '")');
                    $('.chart-user .content').html(template('userInfo', res.userInfo));
                } else {
                    window.alert('微博用户不存在!');
                }
            }
        });
    });

    $(document).on('click', '.search-portrait', function () {
        var input = $('.chart-user input').val();
        $.ajax({
            url: '/api/getUserBySearch.json',
            data: {
                username: input,
                type: 'portrait'
            },
            success: function (res) {
                if (Object.keys(res).length) {
                    res = JSON.parse(res);
                    $('body').css('background-image', 'url("' + res.userInfo.cover_image_phone + '")');
                    $('.chart-user .content').html(template('userInfo', res.userInfo));
                } else {
                    window.alert('微博用户不存在!');
                }
            }
        });
    });

    return {
        render: render
    };
});