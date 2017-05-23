/*TMODJS:{"version":9,"md5":"190fb83485e23bd3431dfb61635bf1e0"}*/
template('userTag',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,tags=$data.tags,$each=$utils.$each,groups=$data.groups,group=$data.group,idx=$data.idx,$out='';$out+='<div class="description"> <div class="title">用户标签</div> <div class="detail"> <p>在用户集合中具有标签共有 <span class="keyword">';
$out+=$escape(total);
$out+='</span> 位用户，共有 <span class="keyword">';
$out+=$escape(tags);
$out+=' </span>种不同的标签分类，其中相同标签用户数量前三的分别为：</p> ';
$each(groups,function(group,idx){
$out+=' ';
if(idx < 3){
$out+='<span> <span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">';
$out+=$escape(group.tags);
$out+='</span> 有';
$out+=$escape(group.count);
$out+='人，占比 <span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(2));
$out+='%</span></span> ';
if(idx !== 2){
$out+='、';
}
$out+=' ';
}
$out+=' ';
});
$out+=' </div> </div> ';
return new String($out);
});