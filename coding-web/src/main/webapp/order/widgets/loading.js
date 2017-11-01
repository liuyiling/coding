define(function(require, exports, module) {
    var LoadingClass = function() {
        this.initialize.apply(this, arguments);
    };

    LoadingClass.prototype = {

        initialize: function() {
            var that = this;
            that.loadingInit();
        },

        loadingInit: function(){
            this.loading = $('<div class="loading-mask" style="display: none;"><div class="loading">加载中...</div></div>');
            $('body').append(this.loading);
        },


        show: function(){
            this.loading.show();
        },

        hide: function(){
            this.loading.hide();
        }
    };

    module.exports = LoadingClass
    
});



