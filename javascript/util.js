
/**
 * JavaScript 常用工具方法
 * Create Time : 2016年12月22日
 */

//---

/**
 * 压缩字符串
 */

function cutStr (str,len){
    var temp,
        icount = 0;
        pattern = /[^\x00-\xff]/,
        strre = "";
    for(var i = 0 ; i < str.length;i++){
        if(icount < len - 1){
            temp = str.substr(i , 1);
            if(pattern.exec(temp) == null){
                icount++;
            }else{
                icount = icount + 2;
            }

            strre += temp;
        }else{
            break;
        }
    }
    return strre + "...";
}