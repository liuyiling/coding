/**
 * Created by liuyiling on 17/6/1.
 */
function where(collection, source) {
    var arr = [];
    // What's in a name?
    for(var i = 0; i < collection.length; i++){
        var tempObject = collection[i];
        var needCount = Object.entries(source).length;
        var realCount = 0;
        for(var key in source){
            if(tempObject.hasOwnProperty(key) && tempObject[key] == source[key]){
                realCount++;
            }
        }

        if(needCount == realCount){
            arr.push(tempObject);
        }
    }

    return arr;
}

where([{ first: "Romeo", last: "Montague" }, { first: "Mercutio", last: null }, { first: "Tybalt", last: "Capulet" }], { last: "Capulet" });
