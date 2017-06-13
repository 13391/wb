/*TMODJS:{"version":11,"md5":"ac28b1f47e8898e5e2e3e046c4190696"}*/
template('userCompany',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,idx=$data.idx,$out='';$out+='<div class="description"> <div class="title">用户所在的公司</div> <div class="detail"> <p>用户就职于<span class="keyword">';
$out+=$escape(total);
$out+='</span>家不同的公司，其中所在公司人数前三的公司是 ';
$each(groups,function(group,idx){
$out+=' ';
if(idx < 3){
$out+='<span class="keyword">';
$out+=$escape(group.company);
$out+='</span><span>[';
$out+=$escape(group.count);
$out+='人]</span>';
}
$out+=' ';
});
$out+=' </p> </div> </div> ';
return new String($out);
});