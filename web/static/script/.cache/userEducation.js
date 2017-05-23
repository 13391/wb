/*TMODJS:{"version":7,"md5":"6f9dc5a6f10f504f073641464788b4de"}*/
template('userEducation',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,$index=$data.$index,$out='';$out+='<div class="description"> <div class="title">用户受教育程度</div> <div class="detail"> <h3>在';
$out+=$escape(total);
$out+='位用户中</h3> ';
$each(groups,function(group,$index){
$out+=' <p><span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">';
$out+=$escape(group.education);
$out+='</span> 有';
$out+=$escape(group.count);
$out+='人，占比 <span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(2));
$out+='%</span></p> ';
});
$out+=' </div> </div> ';
return new String($out);
});