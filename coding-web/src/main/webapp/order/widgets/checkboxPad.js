define(function(require, exports, module) {
    var _body = $('body');
    var ACTIVE = 'active';

    var Pad = require('./pad');
    var Checkbox = require('./checkbox');

    var CheckboxPadClass = function() {
        this.initialize.apply(this, arguments);

    };

    CheckboxPadClass.prototype = {

        initialize: function(params) {

            this.params = params;

            if(!params.checkboxList || !params.checkboxList.length) {
                console.error('需要传递checkboxList字段')
            }

            this.domInit();

        },

        domInit: function(){

            var params = this.params;

            var checkboxGroup = new Checkbox(params);
            var checkboxList = checkboxGroup.getNode();

            var pad = new Pad({
                height: 50 * (this.params.checkboxList.length + 1) + 'px',
                fadeIn: true,
                title: params.title,
                content: checkboxList,
                tapMaskCallback: function(){
                    pad.hide();
                },
                afterHide: function(){
                    // console.log('mask hide');
                }
            });

            this.pad = pad;
            this.checkboxList = checkboxList;

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

    module.exports = CheckboxPadClass;

});



