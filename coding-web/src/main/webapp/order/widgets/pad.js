define(function(require, exports, module) {

    var Mask = require('./mask');

    var PadClass = function() {
        this.initialize.apply(this, arguments);

    };

    PadClass.prototype = {

        initialize: function(params) {

            this.params = params;


            this.domInit();
            
        },

        domInit: function(){

            var params = this.params;

            var panel = $('<div class="pad-panel animated"></div>'),
                title = this.params.title ? $('<div class="title">' + this.params.title + '</div>') : '',
                content = $('<div class="content"></div>').append(params.content);

            if(params.height){
                panel.css({
                    height: params.height
                });
            }
            title && panel.append(title);
            panel.append(content);

            this.mask = new Mask({
                content: panel,
                tapMaskCallback: params.tapMaskCallback,
                fadeIn: params.fadeIn || true,
                afterHide: params.afterHide
            });

            this._panel = panel;

        },

        show: function () {
            this.mask.show();
            this._panel.show();
            this._panel.removeClass('slideOutDown').addClass('slideInUp');
        },

        hide: function () {
            var self = this;
            this.mask.hide();
            this._panel.removeClass('slideInUp').addClass('slideOutDown');
            this.delayAct(function () {
                self._panel.hide();
            });
        },

        delayAct: function(fn, delay) {
            var self = this;
            this.timer = setTimeout(function () {
                fn && fn();
                clearTimeout(self.timer);
            }, delay || 1000);
        }


    };

    module.exports = PadClass;

});



