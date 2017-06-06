/**
 * Created by liuyiling on 17/6/1.
 */
function myReplace(str, before, after) {

    //before的第一个字符大写,atfer的第一个字符小写
    if(before.charCodeAt(0) <= 90 && after.charCodeAt(0) > 90){
        var tempArr = after.split("");
        tempArr[0] = after.charAt(0).toString().toUpperCase();
        after = tempArr.join("");
    }

    var sourceArr = str.split(" ");
    for(var i = 0; i < sourceArr.length; i++){
        var sourceWord = sourceArr[i];
        if(sourceWord == before){
            sourceArr[i] = after;
        }
    }

    return sourceArr.join(" ");
    //return str.replace(str, before, after);
}

myReplace("He is Sleeping on the couch", "Sleeping", "sitting");
