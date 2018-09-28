//抽出公共的 	callback传入一个回调函数
function resultHandler(result,callback) {
    if (result.success){
        $.messager.confirm("温馨提示","保存成功",function () {
            //调用回调函数
            callback();
        })
    }else {
        $.messager.alert("温馨提示",result.msg);
    }
}