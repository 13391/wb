require(['./d3-mod/userGender',
        './d3-mod/userAddress',
        './d3-mod/userCompany',
        './d3-mod/userSchool',
        './d3-mod/userEducation',
        './d3-mod/userCredit',
        './d3-mod/userTag',
        './d3-mod/flare',
        './d3-mod/userInfo',
        './d3-mod/userSource'],
    function (userGender,
              userAddress,
              userCompany,
              userSchool,
              userEducation,
              userCredit,
              userTag,
              flare,
              userInfo,
              userSource) {

        userInfo.render();
        userGender.draw();
        flare.draw();
        userTag.draw();
        userCredit.draw();
        userEducation.draw();
        userSchool.draw();
        userAddress.draw();
        userCompany.draw();
        userSource.draw();

        $(document).on('click', '.list-nav .item', function () {
            $(this).addClass('active').siblings().removeClass('active');
        })
    });