define(function(require, exports, module) {
    var _body = $('body');
    var ACTIVE = 'active';

    var Pad = require('./pad');
    var Radio = require('./radio');

    var RadioPadClass = function() {
        this.initialize.apply(this, arguments);

    };

    RadioPadClass.prototype = {

        initialize: function(params) {

            this.params = params;

            if(!params.radioList || !params.radioList.length) {
                console.error('需要传递radioList字段')
            }

            this.domInit();

        },

        domInit: function(){

            var params = this.params;

            var radioGroup = new Radio(params);
            var radioList = radioGroup.getNode();

            var pad = new Pad({
                height: 50 * (this.params.radioList.length + 1) + 'px',
                fadeIn: true,
                title: params.title,
                content: radioList,
                tapMaskCallback: function(){
                    pad.hide();
                },
                afterHide: function(){
                    // console.log('mask hide');
                }
            });

            this.pad = pad;
            this.radioList = radioList;

        },

        // fireFirstItem: function () {
        //     var self = this;
        //     var timer = setTimeout(function () {
        //         self.radioList.children().eq(0).trigger('click');
        //         clearTimeout(timer);
        //     }, 10);
        // },

        show: function () {
            this.pad.show();
        },

        hide: function () {
            this.pad.hide();
        }

    };

    module.exports = RadioPadClass;

});



