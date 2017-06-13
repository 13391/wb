/*TMODJS:{"version":9,"md5":"c185ef1fe0d12737e6355d47ac8af85b"}*/
template('userSource',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,item=$data.item,idx=$data.idx,$out='';$out+='<div class="description"> <div class="title">微博发送来源</div> <div class="detail"> <p>在爬取的用户中以来源次数最多的设备或应用作为该用户的微博主要来源，共有 <span class="keyword">';
$out+=$escape(total);
$out+='</span> 种不同的来源，其中前三分别为： ';
$each(groups,function(item,idx){
$out+=' ';
if(idx < 3){
$out+='<span class="keyword">';
$out+=$escape(item.source);
$out+='</span>';
}
$out+=' ';
if(idx < 2){
$out+='、';
}
$out+=' ';
});
$out+=' </div> </div> ';
return new String($out);
});