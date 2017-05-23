/*TMODJS:{"version":14,"md5":"7d6dfb26befa815e19a90fd9f7596e18"}*/
template('userCredit',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,total=$data.total,$each=$utils.$each,groups=$data.groups,group=$data.group,$index=$data.$index,$out='';$out+='<div class="description"> <div class="title">用户信用情况</div> <div class="detail"> <p>在拥有信用记录的 <span class="keyword">';
$out+=$escape(total);
$out+='</span> 位用户中</p> ';
$each(groups,function(group,$index){
$out+=' ';
if(group.count !== 0){
$out+='<p> <span style="color: ';
$out+=$escape(group.color);
$out+='; font-weight: bold">';
$out+=$escape(group.credit);
$out+='</span> 有';
$out+=$escape(group.count);
$out+='人，占比 <span class="keyword">';
$out+=$escape((group.rate * 100).toFixed(3));
$out+='%</span></p>';
}
$out+=' ';
});
$out+=' </div> </div> ';
return new String($out);
});