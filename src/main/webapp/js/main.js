
function alert(text, time, fn){
	var d = new iDialog();
	var args = {
		classList: "alert",
		title:"",
		close:"",
		content:'<div class="icon success"></div><div style="padding:10px 30px;line-height:23px;text-align:center;font-size:16px;color:#ffffff;">'+text+'</div>'
	};
	var timer = null;
	if(fn){
		args.btns = [
			{id:"", name:"确定", onclick:"fn.call();", fn: function(self){
				!fn.call()&&self.die();
				time&&clearTimeout(timer);
			}}
		];
	}
	d.open(args);
	if(time){
		timer = setTimeout(function(){
			d.die();
			clearTimeout(timer);
		}, time);
	}
}

function confirm(text, fn1, fn2){
	var d = new iDialog();
	var args = {
		classList: "tip",
		title:"",
		close:"",
		content:text
	};
	args.btns = [
		{id:"", name:"确定", onclick:"fn.call();", fn: function(self){
			fn1&&fn1.call(this);
			self.die();
		}},
		{id:"", name:"取消", onclick:"fn.call();", fn: function(self){
			fn2&&fn2.call(this);
			self.die();
		}}
	];
	d.open(args);
}



function tip(text, time){
	var d = new iDialog();
	d.open({
		classList: "tip",
		title:"",
		close:"",
		content:text,
		btns:[
			{id:"", name:"确定", onclick:"fn.call();", fn: function(self){
				console.log("queding");
			}},
			{id:"", name:"取消", onclick:"fn.call();", fn: function(self){
				self.die();
			}}
		]
	});
}

function loading(type){
	if(type){
		window.loader = new iDialog();
		window.loader.open({
			classList: "loading",
			title:"",
			close:"",
			content:''
		});
	}else{
		//setTimeout(function(){
			window.loader.die();
			delete window.loader;
		//}, 100);
	}
	
}



var my = (function(){
	_my = function(){

	}
	_my.prototype = {
		changeImg: function(thi, evt, _req){
			var that = this;
			if(thi.files.length<1){
				return that;
			}
			// if(thi.files[0].size>=1024*1024*1 && !confirm("(文件过大，建议编辑后上传。)\n是否继续?")){
			// 	thi.setAttribute("data-upload-state", "0%");
			// 	return that;
			// }
			setTimeout(function(){
				that.uploadImg(thi, _req);
			}, 500);
			return that;
		},
		uploadImg: function(thi, _req){
			var that = this;
			var req = {
				id:6,
				username:"webAdd",
				header_img_id : thi.files[0]||{},
				form_action:"/Webnewmemberscore/uploadhead",
				base_url: "http://115.28.20.245:3000/headers/"
			}
			for(var k in _req){
				req[k] = _req[k];
			}
			var xhr = window.ajax2(req.form_action, {
		    	type:"POST",
		    	async: true,
		    	data: req,
		    	timeout:10000*6,
		    	callback: function(res){
		    		if(0 == res.result){
		    			alert(res.message);
		    		}else{
		    			thi.parentNode.querySelectorAll("img")[0].src = req.base_url + res.data.header_img_id;
		    		}
		    		setTimeout(function(){
		    			thi.parentNode.removeAttribute("data-upload-state");
		    		}, 500);
		    	}
		    });
			xhr.onprogress = function(p){
				thi.parentNode.setAttribute("data-upload-state", Math.floor(p)+"%");
			};
			return that;
		}
	}

	return new _my();
})();


function getVCode(thi, evt, formId, teleName){
	if(formId){
		var form = document.getElementById(formId);
		var req = {
			telephone: $.trim(form[teleName].value)
		}
		if(!req.telephone){
			alert("请输入手机号", 1000);return;
		}
	}else{
		var req = {};
	}
	
	thi.setAttribute("disabled", "disabled");
	thi.value = "60秒后可重新获取";
	$.ajax({
		url: "data/getVCode.json",
		type:"post",
		data:req,
		dataType:"JSON",
		success: function(res){
			if(1 == res.result){
				var seconds = 60;//seconds
				var ticker = function(){
					setTimeout(function(){
						seconds --;
						if(seconds>0){
							thi.value = seconds+"秒后可重新获取";
							ticker();
						}else{
							thi.removeAttribute("disabled");
							thi.value = "获取验证码";
						}
					},1000);
				}
				ticker();
			}else{
				alert("失败", 1500);
			}
		}
	});

}



function getLocation(privId, cityId, areaId){
	var areaArr = [];
	try{
		areaArr.push(aLocation["0"][privId]);
		areaArr.push(aLocation["0,"+privId][cityId]);
		areaArr.push(aLocation["0,"+privId+","+cityId][areaId]);
	}catch(e){

	}finally{
		return areaArr.join(' ');
	}
}



var integral = (function(){
	var _integral = function(reqArg){
		this.pageIndex = 0;
		this.pageSize = 10;
		this.maxPage = Infinity;
		this.TPL = '<tr><td>{info} </td><td>{time} </td><td>{integral}</td></tr>';
		this.req = {
			pageIndex : this.pageIndex,
			pageSize: this.pageSize
		};
		for(var k in reqArg){
			this.req[k] = reqArg[k];
		}

	}
	_integral.prototype = {
		getData: function(fn){
			var that = this;
			that.req.pageIndex ++;
			loading(true);
			$.ajax({
				url: that.url||"data/integralList.json",
				type:"post",
				data:that.req,
				dataType:"JSON",
				success: function(res){
					loading(false);
					if(1 == res.result){
						that.total = res.total;
						that.maxPage = Math.ceil(res.total/that.pageSize );
						fn.call(that, that.formatData(res.data));
					}else{
						that.total = res.total || that.total;
						alert("失败", 1500);
					}
				}
			});
			return that;
		},
		nextPage: function(fn){
			var that = this;
			that.getData(fn);
			return that;
		},
		formatData: function(data){
			var that = this;
			return iTemplate.makeList(that.TPL, data, function(k, v){

			});
		}
	}

	return _integral;
})();
