require(['./d3-mod/userGender', 
        './d3-mod/userAddress', 
        './d3-mod/userCompany',
        './d3-mod/userSchool',
        './d3-mod/userEducation',
        './d3-mod/userCredit'], 
    function (
        userGender, 
        userAddress, 
        userCompany, 
        userSchool,
        userEducation,
        userCredit) {

    userCredit.draw();
    userEducation.draw();
    userSchool.draw();
    userGender.draw();
    userAddress.draw();
    userCompany.draw();
});