<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<#include "common/links-tpl.ftl" />
        <link rel="stylesheet" type="text/css" href="/js/plugins/flipcountdown/jquery.flipcountdown.css" />
        <script type="text/javascript" src="/js/plugins/flipcountdown/jquery.flipcountdown.js"></script>
	</head>

	<script>
		$(function(){
		    $.each($(".countdown"),function(index,ele){
		        var time = $(ele).data("time");
                $(ele).flipcountdown({
                    size:'xs',
                    beforeDateTime:time
                });
			});
		});

	</script>
	<body>
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl" />

		<#assign currentNav = "index" />
		<!-- 网页导航 -->
		<#include "common/navbar-tpl.ftl" />

			<!-- 消息列表 -->
			<div class="panel el-panel">
				<div class="panel-title">
					<span class="pull-left">
						系统通知
					</span>
				</div>
				<table class="table el-table table-hover">
					<thead>
						<tr>
							<th>通知时间</th>
							<th>标题</th>
							<th>正文</th>
							<th>通知类型</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if actionMessage?size &gt; 0 >
							<#list actionMessage as data>
								<tr>
									<td>${(data.noteTime?string('yyyy-MM-dd HH:mm:ss'))!''}</td>
									<td>${data.title}</td>
									<td>${data.context}</td>
									<td class="text-info">
										${data.stateDisplay}
									</td>
									<td class="text-info">${data.readStateDisplay}</td>
                                    <td><a class="btn btn-danger btn-sm" href="email_info?id=${data.id}">查看</a></td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td colspan="7" align="center">
									<p class="text-danger">目前暂时没有邮件</p>
								</td>
							</tr>
						</#if>
					</tbody>
					
				</table>
			</div>

	</body>
</html>