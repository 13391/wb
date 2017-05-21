/*TMODJS:{"version":5,"md5":"b35a15706e7e8e7d59aabb33b2f53cea"}*/
template('userEducation',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,$index=$data.$index,$out='';$out+='<div class="description"> <div class="title">用户受教育程度</div> <div class="detail"> <h3>在';
$out+=$escape(total);
$out+='位用户中</h3> ';
$each(groups,function(group,$index){
$out+=' <p>';
$out+=$escape(group.education);
$out+='有';
$out+=$escape(group.count);
$out+='人，占比';
$out+=$escape((group.rate * 100).toFixed(2));
$out+='%</p> ';
});
$out+=' </div> </div> ';
return new String($out);
});