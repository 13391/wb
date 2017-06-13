/*TMODJS:{"version":21,"md5":"c0e3a5e5f056e4362522a698f320a14e"}*/
template('userGender',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,$index=$data.$index,$out='';$out+='<div class="description"> <div class="title">用户性别</div> <div class="detail"> <h3>在';
$out+=$escape(total);
$out+='位用户中</h3> ';
$each(groups,function(group,$index){
$out+=' ';
if(group.gender == 1){
$out+=' <p><span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">男性</span> 有';
$out+=$escape(group.count);
$out+='人，占比<span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(0));
$out+='%</span></p> ';
}
$out+=' ';
if(group.gender == 2){
$out+=' <p><span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">女性</span> 有';
$out+=$escape(group.count);
$out+='人，占比<span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(0));
$out+='%</span></p> ';
}
$out+=' ';
if(group.gender == 0){
$out+=' <p><span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">不明性别</span> 的有';
$out+=$escape(group.count);
$out+='人，占比<span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(0));
$out+='%</span></p> ';
}
$out+=' ';
});
$out+=' </div> </div> ';
return new String($out);
});