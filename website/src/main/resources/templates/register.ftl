<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>蓝源Eloan-P2P平台->用户注册</title>
	<link rel="stylesheet" href="/js/bootstrap-3.3.2-dist/css/bootstrap.css" type="text/css" />
	<link rel="stylesheet" href="/css/core.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery/jquery-2.1.3.js"></script>
	<script type="text/javascript" src="/js/bootstrap-3.3.2-dist/js/bootstrap.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
	<script type="text/javascript" src="/js/jquery.bootstrap.min.js"></script>

	<style type="text/css">
		.el-register-form{
			width:600px;
			margin-left:auto;
			margin-right:auto;
			margin-top: 20px;
		}
		.el-register-form .form-control{
			width: 220px;
			display: inline;
		}
		input[name='verifycode'] + label{
			position: absolute;
			top: 7px;
			left:220px;
		}
	</style>
	<!--校验注册信息的条件-->
	<script src="register/register.js"></script>

	<!--<script type="text/javascript">
		function checkPhone(value) {
            if (!(/^1[3|4|5|7|8]\d{9}$/.test(value))) {
                return false;
            }
            return true;
		}

        $(function () {

			/*
			$("#registerForm").submit(function () {
				//使用ajax Sumit:该方法会直接以ajax方式触发表单的提交
				$("#registerForm").ajaxSubmit(function (result) {
					console.log(result);
                });
				return false;
            });*/


            $.validator.addMethod("checkPhoneNumber", function (value, element, params) {

                return checkPhone(value);
            }, "请输入正确的手机格式");


            $("#registerForm").validate({
				errorClass:"text-danger",
				//subnuthandler:表单验证码通过之后,触发回调,默认表单不会被提交我们只需要在回调方法中使用ajax提交表单即可
				submitHandler:function (form) {
					$(form).ajaxSubmit(function (result) {
						if (result.success){
						    $.messager.confirm("温馨提示","恭喜你,注册成功.",function () {
								location.href="/login.html";
                            });
						}else{
						    $.messager.alert("温馨提示",result.msg);
						}
                    })
                },

				//highlight:在列验证不通过的时候,触发回调方法,element就是验证不通过的
				highlight:function (element,errorClass) {
					$(element).closest("div.form-group").removeClass("has-success").addClass("has-error");
				},
				//unhighlight:在列验证通过的时候,触发
				unhighlight:function (element,errorClass) {
					$(element).closest("div.form-group").removeClass("has-error").addClass("has-success");
				},
				rules: {
					username:{
						//多个条件校验
						required:true,
						rangelength:[11,11],
						checkPhoneNumber:'checkPhoneNumber',
						remote:{
							url:"existUsername",
							type:"post"
						}
					},
					verifycode: {
						required: true,
						rangelength:[4,4]
					},
					password: {
						required: true,
						rangelength:[6,16]
					},
					confirmPwd: {
						rangelength:[6,16],
						equalTo:'#password'
					}
				},
				messages: {
					username: {
						required: "手机号必填",
						rangelength:"手机号码长度{0}位",
                        remote:"该号码已注册"
					},
					verifycode: {
						required: "验证码必填",
						rangelength:"验证码长度 {0} 位"
					},
					password: {
						required: "密码必填",
						rangelength:"密码长度{0} - {1}位"
					},
                    confirmPwd: {
						required: "确认密码必填",
						rangelength:"密码长度{0} - {1}位",
						equalTo:'两次输入密码不一致'
					}
				}
			});
            //发送验证码
            $("#sendVerifyCode").click(function () {
				//获取手机号
				var phoneNumber = $("#phoneNumber").val();
				//判断手机号
				if (!checkPhone(phoneNumber)){
				    $.messager.alert("温馨提示","老杨,你这手机号码有问题哦");
					return;
				}
				//发送按钮
				var sendBtn = $(this);
				//禁用按钮
				sendBtn.attr("disabled",true);
				//执行发送
				jQuery.post("/sendVerifyCode",{phoneNumber:phoneNumber},function (result) {
					//发送成功
					if (result.success){
					    $.messager.alert("温馨提示","验证码已经发送到您手机,请及时查收");
					    var time = 10;
					    var interval = window.setInterval(function () {
							time = time -1;
							if (time <= 0){
							    //清楚定时器
								window.clearInterval(interval);
								//恢复按钮
								sendBtn.attr("disabled",false);
							    sendBtn.html("发送验证码");
							    return;
							}
							sendBtn.html(time+"秒后再发送");
                        },1000);
					}else {
					    $.messager.alert("温馨提示",result.msg);
                        sendBtn.attr("disabled",false);
                        sendBtn.html("发送验证码");
					}
                })
            })
        });
	</script>-->
</head>
<body>
<!-- 网页头信息 -->
<div class="el-header" >
	<div class="container" style="position: relative;">
		<ul class="nav navbar-nav navbar-right">
			<li><a href="/">首页</a></li>
			<li><a href="/login.html">登录</a></li>
			<li><a href="#">帮助</a></li>
		</ul>
	</div>
</div>

<!-- 网页导航 -->
<div class="navbar navbar-default el-navbar">
	<div class="container">
		<div class="navbar-header">
			<a href=""><img alt="Brand" src="/images/logo.png"></a>
			<span class="el-page-title">用户注册</span>
		</div>
	</div>
</div>

<!-- 网页内容 -->
<div class="container">
	<form id="registerForm" class="form-horizontal el-register-form"  action="/userRegister" method="post" >
		<p class="h4" style="margin: 10px 10px 20px;color:#999;">请填写注册信息，点击“提交注册”即可完成注册！</p>

		<#--推荐码注册-->
		<#--<input type="hidden" name="id" value="${id}"/>-->
		<input type="hidden" name="recommend" value="${recommend}"/>

		<div class="form-group">

			<label class="control-label col-sm-2">手机号码</label>
			<div class="col-sm-10">
				<input type="text" placeholder="手机号码" autocomplete="off" name="username" value="18899715015" class="form-control" id="phoneNumber"/>
				<p class="help-block">请使用输入正确的手机号码</p>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">手机验证码</label>
			<div class="col-sm-10">
				<input type="text" placeholder="手机验证码" style="width: 100px" autocomplete="off" name="verifycode" class="form-control" id="verifycode"/>
				<button type="button" class="btn btn-success" id="sendVerifyCode">
					发送验证码
				</button>
				<p class="help-block">请输入4位数验证码</p>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">密&emsp;码</label>
			<div class="col-sm-10">
				<input type="password" placeholder="密码" autocomplete="off" name="password" value="111111" id="password" class="form-control" />
				<p class="help-block">密码为6~16位字符组成,采用数字、字母、符号安全性更高</p>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-2">确认密码</label>
			<div class="col-sm-10">
				<input type="password" autocomplete="off" name="confirmPwd" value="111111" class="form-control" />
				<p class="help-block">请再次填写密码</p>
			</div>
		</div>
		<div class="form-gorup">
			<div class="col-sm-offset-2">
				<button id="formSubmitBtn" type="submit" class="btn btn-success">
					同意协议并注册
				</button>
				&emsp;&emsp;
				<a href="/login.html" class="text-primary">已有账号，马上登录</a>

				<p style="padding-left: 50px;margin-top: 15px;">
					<a href="#">《使用协议说明书》</a>
				</p>
			</div>
		</div>
	</form>
</div>
<!-- 网页版权 -->
<div class="container-foot-2">
	<div class="context">
		<div class="left">
			<p>专注于高级Java开发工程师的培养</p>
			<p>版权所有：&emsp;2015广州小码哥教育科技有限公司</p>
			<p>地&emsp;&emsp;址：&emsp;广州市天河区棠下荷光三横路盛达商务园D座5楼</p>
			<p>电&emsp;&emsp;话： 020-29007520&emsp;&emsp;
				邮箱：&emsp;service@520it.com</p>
			<p>
				<a href="http://www.miitbeian.gov.cn" style="color: #ffffff">ICP备案
					：粤ICP备字1504547</a>
			</p>
			<p>
				<a href="http://www.gzjd.gov.cn/wlaqjc/open/validateSite.do" style="color: #ffffff">穗公网安备：44010650010086</a>
			</p>
		</div>
		<div class="right">
			<a target="_blank" href="http://weibo.com/ITxiaomage"><img
					src="/images/sina.png"></a>
		</div>
		<div class="clearfix"></div>
	</div>
</div>
</body>
</html>