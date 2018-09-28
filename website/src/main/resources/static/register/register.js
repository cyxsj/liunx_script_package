
    function checkPhone(value) {
        if (!(/^1[3|4|5|7|8]\d{9}$/.test(value))) {
            return false;
        }
        return true;
    }

$(function () {

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
        //jQuery等同于$符号
        jQuery.post("/sendVerifyCode",{phoneNumber:phoneNumber},function (result) {
            //发送成功
            if (result.success){
                $.messager.alert("温馨提示","验证码已经发送到您手机,请及时查收");
                //按钮倒计时
                var time = 60;
                //开启定时器
                //setInterval 每隔1000ms执行一次
                var interval = window.setInterval(function () {
                    time = time -1;
                    if (time <= 0){
                        //清除定时器
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
