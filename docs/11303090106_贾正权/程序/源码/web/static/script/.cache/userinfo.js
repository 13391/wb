/*TMODJS:{"version":22,"md5":"f07ceb3dcb24a5b17fdbe4ff04f366a4"}*/
template('userInfo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,profile_url=$data.profile_url,profile_image_url=$data.profile_image_url,id=$data.id,screen_name=$data.screen_name,description=$data.description,gender=$data.gender,urank=$data.urank,followers_count=$data.followers_count,follow_count=$data.follow_count,$out='';$out+='<div class="userinfo"> <div class="avatar"> <a href="';
$out+=$escape(profile_url);
$out+='" target="_blank"><img src="';
$out+=$escape(profile_image_url);
$out+='"></a> </div> <ul> <li><span>ID</span> ';
$out+=$escape(id);
$out+='</li> <li><span>用户名</span> ';
$out+=$escape(screen_name);
$out+='</li> <li><span>简介</span> ';
$out+=$escape(description);
$out+='</li> <li><span>性别</span> ';
if(gender=='m'){
$out+='男';
}else{
$out+='女';
}
$out+='</li> <li><span>等级</span> ';
$out+=$escape(urank);
$out+='</li> <li><span>粉丝</span> ';
$out+=$escape(followers_count);
$out+='</li> <li><span>关注</span> ';
$out+=$escape(follow_count);
$out+='</li> </ul> </div>';
return new String($out);
});