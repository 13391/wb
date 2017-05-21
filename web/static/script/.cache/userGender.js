/*TMODJS:{"version":18,"md5":"0d8bb16f2d4ae6c74cadb392f5cea90f"}*/
template('userGender',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,$index=$data.$index,$out='';$out+='<div class="description"> <div class="title">用户性别</div> <div class="detail"> <h3>在';
$out+=$escape(total);
$out+='位用户中</h3> ';
$each(groups,function(group,$index){
$out+=' ';
if(group.gender == 1){
$out+=' <p>男性有';
$out+=$escape(group.count);
$out+='人，占比<span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(0));
$out+='%</span></p> ';
}
$out+=' ';
if(group.gender == 2){
$out+=' <p>女性有';
$out+=$escape(group.count);
$out+='人，占比<span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(0));
$out+='%</span></p> ';
}
$out+=' ';
if(group.gender == 0){
$out+=' <p>不明性别的有';
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