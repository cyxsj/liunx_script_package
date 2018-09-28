<div class="el-header" >
	<div class="container" style="position: relative;">
		<ul class="nav navbar-nav navbar-right">
			<li><a href="/index">首页</a></li>
			<#if logininfo ??>
				<#--完成登录显示-->
				<li>
					  <a class="el-current-user" href="/personal.do">用户名: ${logininfo.username}</a>
				</li>
				<li><a  href="/recharge.do">账户充值</a></li>

				<li><a href="/actionMessage" id="hi"></a></li>

				<li><a  href="/logout.do">注销 </a></li>
			<#else >
				<li><a href="/login.html">登录</a></li>
				<li><a href="/register.html">快速注册</a></li>
			</#if>
			<li><a href="#">帮助</a></li>
		</ul>
	</div>

	<script type="text/javascript">
		$(function () {
			$.post("/getState",function (date) {
				$("#hi").html("系统信息:"+date)
            })
        })
	</script>
</div>
