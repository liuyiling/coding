/**
 * Created by liuyiling on 17/5/31.
 */
function palindrome(str) {
    // Good luck!
    var tempStr = str.replace(/[\W_]/g, '').toLowerCase();
    var tempArr = tempStr.split("");
    var resultArr = tempArr.reverse();
    var resultStr = resultArr.join("");

    return tempStr === resultStr;
}

palindrome("eye");

function largestOfFour(arr) {
    // You can do this!
    var result = [];

    for(var tempArr in arr){
        var max = 0;
        for(var tempNum in tempArr){
            if(tempNum >= max){
                max = tempNum;
            }
        }
        result.push(max);
    }

    return result;
}

largestOfFour([[4, 5, 1, 3], [13, 27, 18, 26], [32, 35, 37, 39], [1000, 1001, 857, 1]]);