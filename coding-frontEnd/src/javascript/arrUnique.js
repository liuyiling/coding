/**
 * Created by liuyiling on 17/6/1.
 */
function unique(arr) {
    return arr.filter(function(item, index){
        // indexOf返回第一个索引值，
        // 如果当前索引不是第一个索引，说明是重复值
        return arr.indexOf(item) === index;
    });
}