define(function(require, exports, module) {
    var DOC = document;

    var supportLocalStorage = (function () {
        var test = 'test';
        try {
            localStorage.setItem(test, test);
            localStorage.removeItem(test);
            return true;
        } catch (e) {
            return false;
        }
    })();

    var supportSessionStorage = (function () {
        var test = 'test';
        try {
            sessionStorage.setItem(test, test);
            sessionStorage.removeItem(test);
            return true;
        } catch (e) {
            return false;
        }
    })();

    var timeTag = 0;

    var Util = {

        /**
         * 返回是否是空对象，空数组也返回 true
         * @param obj
         * @returns {boolean}
         */
        isEmptyObject : function (obj) {
            var name;
            for (name in obj) return false;
            return true;
        },

        /**
         * locStorage util
         */
        locStorage   : {
            set        : function (key, value) {
                if (supportLocalStorage) {
                    localStorage.setItem(key, value);
                } else if (supportSessionStorage) {
                    sessionStorage.setItem(key, value);
                }
            },
            get        : function (key) {
                if (supportLocalStorage) {
                    return localStorage.getItem(key);
                } else if (supportSessionStorage) {
                    return sessionStorage.getItem(key);
                }
            },
            removeItem : function (key) {
                if (supportLocalStorage) {
                    return localStorage.removeItem(key);
                } else if (supportSessionStorage) {
                    return sessionStorage.removeItem(key);
                }
            },
            isSupport  : function (key) {
                return supportLocalStorage || supportSessionStorage;
            }
        },

        /**
         * parse json 报错处理
         */
        json         : {
            parse : function (data) {
                var result = null;

                try {
                    result = JSON.parse(data);
                } catch (e) {

                } finally {
                    return result;
                }
            }
        },


        /**
         * 从location.search || location.hash 中获取查询参数对象，默认从location.search中获取
         * @param search : 第一个字符串类型参数，解析这里输入的url search对象
         * @param opt : 第二个对象型参数，目前只有 skipXssFilter : [true | false] 参数，表示是否跳过xss转义，默认false不跳过
         * @returns {{}}
         */
        getSearchObj : function (search, opt) {
            var args = Array.prototype.slice.call(arguments);
            var search = typeof args[0] == 'string' ? args[0] + '' : '';
            var _search = search || location.search;
            var queryIndex = _search.indexOf("?");
            var obj = {};
            var that = this;
            var opt = args[args.length - 1],
                opt = typeof opt == 'object' && opt;
            var isSkipXssFilter = opt && opt.skipXssFilter || false;
            if (queryIndex > -1) {
                var searches = _search.substring(queryIndex + 1, _search.length).split("&");
                $.each(searches, function (k, v) {
                    var pair = v.split("=");
                    pair[1] = isSkipXssFilter ? decodeURIComponent(pair[1]) : that.xssFilter(decodeURIComponent(pair[1]));
                    (pair[0] + '' != '') && (obj[pair[0].toString()] = pair[1]);
                });
            }
            return obj;
        },

        /**
         * reflow
         */
        reflow       : function () {
            document.body.style.zoom = '1.0001';
            setTimeout(function () {
                document.body.style.zoom = '1';
            }, 100);
        },

        /**
         * xssFilter
         */
        xssFilter : function (str) {
            str = str.replace(/&/g, '&amp;');
            str = str.replace(/</g, '&lt;');
            str = str.replace(/>/g, '&gt;');
            str = str.replace(/'/g, '&acute;');
            str = str.replace(/"/g, '&quot;');
            str = str.replace(/\|/g, '&brvbar;');
            return str;
        },

        getRequestParam : function (param, uri) {
            var value;
            uri = uri || window.location.href;
            value = uri.match(new RegExp('[\?\&]' + param + '=([^\&\#]*)([\&\#]?)', 'i'));
            return value ? decodeURIComponent(value[1]) : value;
        },

        getRequestParams : function (query) {
            var search = query || location.search.substring(1);
            return search ? JSON.parse('{"' + search.replace(/&/g, '","').replace(/=/g, '":"') + '"}', function (key, value) {
                    return key === "" ? value : decodeURIComponent(value);
                }) : {};
        },

        //获取绝对地址
        getAbsoultePath : function (href) {
            var link = document.createElement('a');
            link.href = href;
            return (link.protocol + '//' + link.host + link.pathname + link.search + link.hash);
        },

        /**
         * 缓存图片后执行回调，用于需要确保图片已经出现时执行后续任务，如动画。
         * @param src   [img1src, img2src, ...] || imgSrc
         * @param callback
         */
        preloadImage : function (imgSrc, callback, context) {
            var context = context || null,
                len     = 0;

            if (imgSrc) {
                if (!(Object.prototype.toString.call(imgSrc) === "[object Array]")) {
                    imgSrc = [imgSrc];
                }

                len = imgSrc.length;

                //todo 图片多时注意并发问题
                for (var i = 0; i < len; i++) {
                    loadImage(imgSrc[i]);
                }
            } else {
                callback.apply(context);
            }

            function loadImage (src) {
                var img = new Image();
                img.onload = img.onerror = function () {
                    complete();
                };
                img.src = src;
            }

            function complete () {
                if (--len <= 0) {
                    callback.apply(context);
                }
            }

        },

        /**
         * 监视css文件是否加载完成
         * @param fn 回调方法
         * .loadcssdom {
         *    position: absolute;
         *    float:left;
         *    bottom: -1px;
         *    height: 1px;
         *    width: 1px;
         *    left: 10px;
         *    opacity: 0;
         *    z-index: 100000;
         *    display: block !important;
         * }
         * */
        monitorCSSloaded:{
            addDom:function(){
                var div = $('<div style="display: none" id="J_loadCss" class="loadcssdom"></div>');
                $('body').append(div);
                return div;
            },
            monitor:function(fn){
                setTimeout(function(){
                    fn($('#J_loadCss').offset().left==10);
                },320)
            }
        },
        /**
         * @method 获取唯一标识串
         * @return guid
         * */
        getGuid:function(){
            return new Date().getTime() + '' + parseInt(Math.random() * 1000000);
        },

        /**
         * 去抖函数
         * @param func
         * @param wait
         * @returns {Function}
         */
        debounce: function(func, wait, immediate) {
            var timeout, result;
            return function() {
                var context = this, args = arguments;
                var later = function() {
                    timeout = null;
                    if (!immediate) result = func.apply(context, args);
                };
                var callNow = immediate && !timeout;
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
                if (callNow) result = func.apply(context, args);
                return result;
            };
        }
    };

    module.exports = Util;
    
});