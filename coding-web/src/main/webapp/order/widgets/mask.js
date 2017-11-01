define(function(require, exports, module) {
    var _body = $('body');

    var MaskClass = function() {
        this.initialize.apply(this, arguments);

    };

    MaskClass.prototype = {

        initialize: function(params) {

            this.params = params;

            this.domInit();
            this.bindEvent();
            
        },

        domInit: function(){
            
            var panel = $('<div class="mask-panel animated" style="display: none;"></div>'),
                mask = $('<div class="mask-page"></div>');

            panel.append(mask);
            panel.append(this.params.content);

            _body.append(panel);

            this._panel = panel;
            this._mask = mask;

        },

        bindEvent: function(){
            var tapMaskCallback = this.params.tapMaskCallback;

            if(tapMaskCallback){
                this._mask.on('click', function(){
                    tapMaskCallback && tapMaskCallback();
                });
            }
        },

        show: function(){
            // this.switchPointEvents();
            if(this.params.fadeIn == true){
                this._panel.show();
                this._panel.removeClass('fadeOut').addClass('fadeIn');
            } else {
                this._panel.show();
            }

        },

        hide: function(){
            var self = this;
            if(this.params.fadeIn == true){
                this._panel.removeClass('fadeIn').addClass('fadeOut');
                this.delayAct(function(){
                    self._panel.hide();
                });
            } else {
                this._panel.hide();
            }

            var afterHide = this.params.afterHide;

            afterHide && afterHide();
        },

        delayAct: function(fn, delay) {
            var self = this;
            this.timer = setTimeout(function () {
                fn && fn();
                clearTimeout(self.timer);
            }, delay || 1000);
        },

        switchPointEvents: function () {
            var self = this;
            this._mask.css({
                'pointer-events': 'none'
            });
            this.delayAct(function () {
                self._mask.css({
                    'pointer-events': 'auto'
                });
            }, 400);
        }
    };

    module.exports = MaskClass;

});



