/*TMODJS:{"version":20,"md5":"72a9934fb525955822672ab4a7788bff"}*/
template('userSchool',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,schoolCount=$data.schoolCount,$each=$utils.$each,groups=$data.groups,group=$data.group,idx=$data.idx,$out='';$out+='<div class="description"> <div class="title">用户毕业高校数量排名</div> <div class="detail"> <p>在用户集合中共有 <span class="keyword">';
$out+=$escape(total);
$out+='</span> 位用户具有学校属性，共有 <span class="keyword">';
$out+=$escape(schoolCount);
$out+=' </span>所不同的学校，其中前五位分别为:</p> ';
$each(groups,function(group,idx){
$out+=' ';
if(idx < 5){
$out+='<span> <span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">';
$out+=$escape(group.school);
$out+='</span> 有';
$out+=$escape(group.count);
$out+='人，占比 <span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(2));
$out+='%</span></span> ';
if(idx!==4){
$out+='、';
}
$out+=' ';
}
$out+=' ';
});
$out+=' </div> </div> ';
return new String($out);
});