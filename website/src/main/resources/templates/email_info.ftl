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

		标题：${email.title}<br><br>
		正文：${email.context}<br>


	</body>
</html>