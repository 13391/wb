/*TMODJS:{"version":46,"md5":"05b6e23f493a2f8540a554cc7ca915a3"}*/
template('userPortrait',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,keywords=$data.keywords,item=$data.item,idx=$data.idx,$escape=$utils.$escape,introduction=$data.introduction,period=$data.period,sourceData=$data.sourceData,$out='';$out+='<div class="portrait"> <section class="introduction"> ';
$each(keywords,function(item,idx){
$out+=' <span class="keywords" style="color: ';
$out+=$escape(item.color);
$out+='; display: inline-block; font-size: ';
$out+=$escape(item.size);
$out+='px; transform: rotate(';
$out+=$escape(item.angle);
$out+='deg)">';
$out+=$escape(item.value);
$out+='</span> ';
});
$out+=' <p class="keyword">';
$out+=$escape(introduction);
$out+='</p> </section> <section class="created broken-line"> <div class="svg-container"></div> <span class="interpolation">linear</span> <span class="interpolation">linear-closed</span> <span class="interpolation">basis</span> <span class="interpolation">step-after</span> <span class="interpolation">bundle</span> <span class="interpolation">monotone</span> <span class="interpolation">cardinal</span> <div class="description"> <span>通过对该用户不同时间段发送微博数量进行分析，其每天最为活跃的时间段为 <span class="keyword">';
$out+=$escape(period);
$out+='~';
$out+=$escape(period+1);
$out+='</span>点</span> </div> </section> <section class="source"> <div class="svg-container"></div> <div class="description"> <span>该用户微博发送来源前三的设备或者应用是 ';
$each(sourceData,function(item,idx){
$out+=' ';
if(idx < 3){
$out+='<span class="keyword">';
$out+=$escape(item.source);
$out+='</span>';
if(idx<2){
$out+='、';
}
}
$out+=' ';
});
$out+=' </span> </div> </section> </div>';
return new String($out);
});