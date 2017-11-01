define(function(require, exports, module) {
    var DOC = document;

    var Common = function() {
        this.initialize.apply(this, arguments);
    };

    Common.prototype = {

        initialize: function() {
            var that = this;
            that.init();
        },

        init: function(){
            this.back();
        },


        back: function(){
            $(DOC).delegate('.J_HistoryBack', 'click', function(){
                history.back();
            });
        }
    };
    new Common();

    module.exports = Common;
    
});