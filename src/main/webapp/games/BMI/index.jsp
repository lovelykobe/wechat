<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>BMI</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="Alex" name="author" />
<meta charset="UTF-8">
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css">
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script
	src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
<script src="${path }/static/js/public.js"></script>
<script type="text/javascript">
        function compute(){
            //将计算结果清空
            $("#bmiIndex").html();
            $("#bmiDesc").html();

            //体重
            var mass = $("#mass").val();
            //身高
            var body = $("#body").val();
            //BMI指数
            var bmiIndex = (mass/(body*body)).toFixed(2);
            //健康状况
            var bmiDesc = "";
            
            if(bmiIndex < 18.5){
                bmiDesc = "偏瘦";
            }else if(bmiIndex >= 18.5 && bmiIndex < 24){
                bmiDesc = "正常";
            }else if(bmiIndex >= 24 && bmiIndex < 28){
                bmiDesc = "偏胖";
            }else if(bmiIndex > 28){
                bmiDesc = "肥胖";
            }
            //填充计算结果
            $("#bmiIndex").html(bmiIndex);
            $("#bmiDesc").html(bmiDesc);
            //跳转到计算结果页面
            window.location = window.location.href+"#result";

        }
    </script>
</head>
<body>
	<div data-role="page" id="bmi">
		<div data-role="header" data-theme="b">
			<h1>BMI计算器</h1>
		</div>
		<div data-role="content">
			<form action="" method="post" ,action="">
				<label for="mass">体重:</label> <input type="text" name="mass"
					id="mass" placeholder="单位：千克"> <label for="body">身高:</label>
				<input type="text" name="body" id="body" placeholder="单位：米">
			</form>
			<a href="#" onclick="compute()" data-role="button" data-theme="e">计算</a>
		</div>
		<div data-role="footer" data-position="fixed">
			<h4>技术支持：王狗狗</h4>
		</div>
	</div>
	<div data-role="page" id="result">
		<div data-role="header" data-theme="b">
			<h1>BMI计算器</h1>
		</div>
		<div data-role="content">
			<div data-role="collapsible" data-collapsed="false" data-theme="d"
				data-content-theme="c" data-collapsed-icon="arrow-r"
				data-expanded-icon="arrow-d" data-iconpos="right">
				<h3>计算结果</h3>
				<p>
					BMI指数：<span id="bmiIndex" style="color:#0000FF"></span>
				</p>
				<p>
					健康情况：<span id="bmiDesc" style="color:#0000FF"></span>
				</p>
			</div>
		</div>
		<div data-role="footer" data-position="fixed">
			<h4>技术支持：王狗狗</h4>
		</div>
	</div>
</body>
</html>
