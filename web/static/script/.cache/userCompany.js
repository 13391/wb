/*TMODJS:{"version":8,"md5":"d65b02cca988010b936ea6646126e7d1"}*/
template('userCompany',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,groups=$data.groups,group=$data.group,idx=$data.idx,$escape=$utils.$escape,$out='';$out+='<div class="description"> <div class="title">用户所在的公司</div> <div class="detail"> <p>用户集合中所在公司人数前三的公司是 ';
$each(groups,function(group,idx){
$out+=' ';
if(idx < 3){
$out+='<span class="keyword">';
$out+=$escape(group.company);
$out+='</span><span>[';
$out+=$escape(group.count);
$out+=']人</span>';
}
$out+=' ';
});
$out+=' </p> </div> </div> ';
return new String($out);
});